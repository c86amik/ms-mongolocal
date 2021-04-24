package com.gokoders.login.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gokoders.login.client.RegistrationUserClientProxy;
import com.gokoders.login.exception.FlightException;
import com.gokoders.login.model.NotificationMail;
import com.gokoders.login.model.UserRegistration;
import com.gokoders.login.repository.FlightExceptionRepository;
import com.gokoders.login.service.EmailService;

import freemarker.template.TemplateException;


/**
 * 
 */

/**
 * @author gokoders
 *
 */
@Controller
@RequestMapping(path = "/login")
public class LoginController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private RegistrationUserClientProxy registrationUserClientProxy;
	@Autowired
	private FlightExceptionRepository flightExceptionRepository;
	@Autowired
    private EmailService emailService;
	
	@RequestMapping(path = "/logUser", method = RequestMethod.GET)
    public String loginPage(Model model) {
        model.addAttribute("loginUser", new UserRegistration());
        LOGGER.info("loginPage -> Registered User Login");
        return "userLogin";
    }
	
	@RequestMapping(path = "/logUser", method = RequestMethod.POST)
    public String welcomeUser(Model model, UserRegistration userRegistration) {
		FlightException flightException = null;
        LOGGER.info("welcomeUser -> Registered User successfully logged in");
        UserRegistration fetchedUserDetails = registrationUserClientProxy.registeredUserLogin(userRegistration);
        if(null != fetchedUserDetails) {
        	if(fetchedUserDetails.isAdmin()) {
        		return "redirect:/admin/users";
        	} else {
        		return "redirect:/flight/flights/" + "user";
        	}
        } else {
        	model.addAttribute("loginUser", new UserRegistration());
        	LOGGER.info("welcomeUser -> Password is not correct for the user : {}", userRegistration.getUserName());
        	flightException = new FlightException(null, userRegistration.getUserName(), 
					"is not correct for the user " + userRegistration.getUserName(), "Password", "welcomeUser()", "LoginController");
        	flightExceptionRepository.save(flightException);
        	return "redirect:/error/" + userRegistration.getUserName();
        }
    }
	
	@RequestMapping(path = "/addUser", method = RequestMethod.GET)
    public String createUser(Model model) {
        model.addAttribute("user", new UserRegistration());
        LOGGER.info("createUser -> New User Registration");
        return "userDetail";
    }
	
	@RequestMapping(path = "/addUser", method = RequestMethod.POST)
    public String registerUser(UserRegistration userRegistration) {
		String exceptionParameters = "";
		FlightException flightException = null;
		boolean validUserDetails = false;
		if(null != userRegistration) {
			if(StringUtils.hasText(userRegistration.getId())) {
				UserRegistration alreadyRegisteredUser = registrationUserClientProxy.getRegisterUserById(userRegistration.getId());
				userRegistration.setPassword(alreadyRegisteredUser.getPassword());
				userRegistration.setConfirmPassword(alreadyRegisteredUser.getConfirmPassword());
				userRegistration.setAdmin(alreadyRegisteredUser.isAdmin());
				registrationUserClientProxy.registerUser(userRegistration);
				return "redirect:/flight/flights/" + "user"; // It will later move to flight search page
			} else {
				List<UserRegistration> userRegistrations = registrationUserClientProxy.getAllRegisteredUsers();
				if(!userRegistrations.isEmpty()) {
					for(UserRegistration registration : userRegistrations) {
						if(registration.getUserName().equals(userRegistration.getUserName())) {
							flightException = new FlightException(null, userRegistration.getUserName(), 
									"UserName is same", userRegistration.getUserName(), "registerUser()", "UserRegistrationController");
							flightExceptionRepository.save(flightException);
							exceptionParameters = userRegistration.getUserName();
							LOGGER.info("FlightException occured for this UserName : {}", exceptionParameters);
							validUserDetails = false;
							break;
						} else if(StringUtils.hasText(userRegistration.getPassword()) 
								&& StringUtils.hasText(userRegistration.getConfirmPassword())) {
							if(userRegistration.getPassword().equals(userRegistration.getConfirmPassword())) {
								userRegistration.setAdmin(false);
								validUserDetails = true;
							} else {
								flightException = new FlightException(null, userRegistration.getUserName(), 
										"and ConfirmPassword are not same", "Password", "registerUser()", "UserRegistrationController");
								flightExceptionRepository.save(flightException);
								exceptionParameters = userRegistration.getUserName();
								LOGGER.info("FlightException occured for this UserName : {}", exceptionParameters);
								validUserDetails = false;
							}
						}
					}
				} else if(StringUtils.hasText(userRegistration.getPassword()) 
						&& StringUtils.hasText(userRegistration.getConfirmPassword())) {
					if(userRegistration.getPassword().equals(userRegistration.getConfirmPassword())) {
						userRegistration.setAdmin(false);
						validUserDetails = true;
					} else {
						flightException = new FlightException(null, userRegistration.getUserName(), 
								"and ConfirmPassword are not same", "Password", "registerUser()", "UserRegistrationController");
						flightExceptionRepository.save(flightException);
						exceptionParameters = userRegistration.getUserName();
						LOGGER.info("FlightException occured for this UserName : {}", exceptionParameters);
						validUserDetails = false;
					}
				}
			}
			if(validUserDetails) {
				registrationUserClientProxy.registerUser(userRegistration);
				NotificationMail mail = createNotificationMail(userRegistration, "register");
				try {
					emailService.sendEmail(mail, "registration-template.ftl");
				} catch (MessagingException | IOException | TemplateException e) {
					LOGGER.error("Exception thrown while sending mail : " + e.getMessage());
				}
			} else {
				return "redirect:/error/" + exceptionParameters;
			}
		}
        LOGGER.info("registerUser -> New User Registered");
        return "redirect:/login/logUser";
	}
	
	@RequestMapping(path = "/user/{id}/{uname}", method = RequestMethod.GET)
	public String getRegisterUserByUserIdAndUserName(Model model, @PathVariable(value = "id") String userId, 
    		@PathVariable(value = "uname") String userName) {
    	UserRegistration userRegistration = null;
    	FlightException flightException = null;
    	if(StringUtils.hasText(userId)) {
    		userRegistration = registrationUserClientProxy.getRegisterUserById(userId);
    		if(null == userRegistration) {
    			flightException = new FlightException(userId, userName, 
						"UserName not found", userName, "getRegisterUserByUserIdAndUserName()", "LoginController");
    			flightExceptionRepository.save(flightException);
    		}
    	}
    	model.addAttribute("user", userRegistration);
    	LOGGER.info("getRegisterUserByUserIdAndUserName -> Fetch the user with userId as : {}", userId);
        return "userDetail";
    }
	
	@RequestMapping(path = "/changePassword", method = RequestMethod.GET)
    public String changePassword(Model model) {
		model.addAttribute("user", new UserRegistration());
        LOGGER.info("changePassword -> Change Password page for the Registered User");
        return "changePassword";
	}
	
	@RequestMapping(path = "/forgotPassword", method = RequestMethod.GET)
    public String forgotPassword(Model model) {
		model.addAttribute("user", new UserRegistration());
        LOGGER.info("forgotPassword -> Forgot Password page for the Registered User");
        return "forgotPassword";
	}
	
	@SuppressWarnings("null")
	@RequestMapping(path = "/updatePassword", method = RequestMethod.POST)
    public String updatePassword(UserRegistration userRegistration) {
		FlightException flightException = null;
		String exceptionParameters = "";
		if(null != userRegistration) {
			if(StringUtils.hasText(userRegistration.getUserName())) {
				UserRegistration updatedUserRegistration = registrationUserClientProxy.updatePassword(userRegistration);
				if(null != updatedUserRegistration) {
					LOGGER.info("updatePassword -> Password {} updated for the Registered User {}", userRegistration.getPassword(), userRegistration.getUserName());
					NotificationMail mail = createNotificationMail(updatedUserRegistration, "password");
					try {
						emailService.sendEmail(mail, "password-template.ftl");
					} catch (MessagingException | IOException | TemplateException e) {
						LOGGER.error("Exception thrown while sending mail : " + e.getMessage());
					}
				} else {
					return "redirect:/error/" + exceptionParameters;
				}
			}
		} else {
			flightException = new FlightException(null, userRegistration.getUserName(), 
					"user is not registered", userRegistration.getUserName(), "updatePassword()", "UserRegistrationController");
			flightExceptionRepository.save(flightException);
			exceptionParameters = userRegistration.getUserName();
			LOGGER.info("FlightException occured for this UserName : {}", exceptionParameters);
			return "redirect:/error/" + exceptionParameters;
		}
        LOGGER.info("updatePassword -> New User Registration");
        return "redirect:/login/logUser";
	}
	
	@RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout() {
        LOGGER.info("logout -> Logout an User from the application");
        return "redirect:/login/logUser";
	}
	
	private NotificationMail createNotificationMail(UserRegistration userRegistration, String identifier) {
		NotificationMail notificationMail = new NotificationMail();
		notificationMail.setFrom("amikevergreen.whitepeaksoft@gmail.com");
		notificationMail.setTo(userRegistration.getEmail());
		if("register".equals(identifier)) {
			notificationMail.setSubject("Successful Registration for User " + userRegistration.getFirstName() + " " + userRegistration.getMiddleName() + " " + userRegistration.getLastName());
		} else if("password".equals(identifier)) {
			notificationMail.setSubject("New Password for " + userRegistration.getFirstName() + " " + userRegistration.getMiddleName() + " " + userRegistration.getLastName());
		}
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("fullName", userRegistration.getFirstName() + " " + userRegistration.getMiddleName() + " " + userRegistration.getLastName());
		model.put("userName", userRegistration.getUserName());
		model.put("password", userRegistration.getPassword());
		model.put("signature", "From WhitePeakSoft Team");
		notificationMail.setModel(model);
		
		return notificationMail;
	}
}
