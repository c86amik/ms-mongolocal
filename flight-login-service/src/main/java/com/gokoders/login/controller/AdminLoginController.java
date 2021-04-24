/**
 * 
 */
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
 * @author gokoders
 *
 */
@Controller
@RequestMapping(path = "/admin")
public class AdminLoginController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminLoginController.class);
	
	@Autowired
	private RegistrationUserClientProxy registrationUserClientProxy;
	@Autowired
	private FlightExceptionRepository flightExceptionRepository;
	@Autowired
    private EmailService emailService;
	
	@RequestMapping(path = "/users", method = RequestMethod.GET)
    public String getAllRegisteredUsers(Model model) {
		LOGGER.info("getAllRegisteredUsers -> All Users fetched");
		model.addAttribute("users", registrationUserClientProxy.getAllRegisteredUsers());
        return "userLists";
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
        return "userAdminDetail";
    }
	
	@RequestMapping(path = "/deleteUser/{id}/{uname}", method = RequestMethod.GET)
    public String deleteRegisterUserByUserIdAndUserName(@PathVariable(value = "id") String userId, 
    		@PathVariable(value = "uname") String userName) {
		UserRegistration userRegistration = null;
		FlightException flightException = null;
		if(StringUtils.hasText(userId)) {
	    	userRegistration = registrationUserClientProxy.getRegisterUserById(userId);
	    	if(null == userRegistration) {
    			flightException = new FlightException(userId, userName, 
						"UserName not found", userName, "deleteRegisterUserByUserIdAndUserName()", "LoginController");
    			flightExceptionRepository.save(flightException);
    		}
	    	registrationUserClientProxy.deleteRegisterUser(userId);
		}
    	LOGGER.info("deleteRegisterUserByUserIdAndUserName -> Delete the user of userId : {}", userId);
        return "redirect:/admin/users";
    }
	
	@RequestMapping(path = "/addUser", method = RequestMethod.GET)
    public String createUser(Model model) {
        model.addAttribute("user", new UserRegistration());
        LOGGER.info("createUser -> New User Registration");
        return "userAdminDetail";
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
				return "redirect:/admin/users";
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
								userRegistration.setAdmin(true);
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
				}
				else if(StringUtils.hasText(userRegistration.getPassword()) 
						&& StringUtils.hasText(userRegistration.getConfirmPassword())) {
					if(userRegistration.getPassword().equals(userRegistration.getConfirmPassword())) {
						userRegistration.setAdmin(true);
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
				NotificationMail mail = createNotificationMail(userRegistration);
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
	
	private NotificationMail createNotificationMail(UserRegistration userRegistration) {
		NotificationMail notificationMail = new NotificationMail();
		notificationMail.setFrom("amikevergreen.whitepeaksoft@gmail.com");
		notificationMail.setTo(userRegistration.getEmail());
		notificationMail.setSubject("Successful Registration for User " + userRegistration.getFirstName() + " " + userRegistration.getMiddleName() + " " + userRegistration.getLastName());
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("fullName", userRegistration.getFirstName() + " " + userRegistration.getMiddleName() + " " + userRegistration.getLastName());
		model.put("userName", userRegistration.getUserName());
		model.put("password", userRegistration.getPassword());
		model.put("signature", "From WhitePeakSoft Team");
		notificationMail.setModel(model);
		
		return notificationMail;
	}
}
