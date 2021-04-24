package com.gokoders.registration.controller;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gokoders.registration.exception.UserResourceException;
import com.gokoders.registration.model.UserRegistration;
import com.gokoders.registration.repository.UserRegistrationRepository;
import com.gokoders.registration.utility.PasswordGenerator;


/**
 * 
 */

/**
 * @author gokoders
 *
 */
@RestController
public class UserRegistrationController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationController.class);
	
	@Autowired
	private UserRegistrationRepository userRegistrationRepository;
	
	@GetMapping("/users")
    public List<UserRegistration> getAllRegisteredUsers() {
		/* List<UserRegistration> registrations = new ArrayList<>();*/
		LOGGER.info("getAllRegisteredUsers -> All Users fetched");
		/* Iterable<UserRegistration> userRegIterator = userRegistrationRepository.findAll();
		userRegIterator.forEach(registrations::add); */
		return userRegistrationRepository.findAll();
    }
	
	@PostMapping("/addUser")
    public UserRegistration registerUser(@RequestBody UserRegistration userRegistration) {
        LOGGER.info("registerUser -> New User Registered");
        return userRegistrationRepository.save(userRegistration);
    }
	
	@GetMapping("/user/{id}")
    public UserRegistration getRegisterUserById(@PathVariable(value = "id") String userId) {
    	UserRegistration userRegistration = userRegistrationRepository.findById(userId)
    			.orElseThrow(() -> new UserResourceException("UserName not found", userId));
    	LOGGER.info("getRegisterUserById -> Fetch the user with userId as : {}", userId);
        return userRegistration;
    }
	
	@GetMapping("/fetchUser/{uname}")
    public UserRegistration getRegisterUserByUserName(@PathVariable(value = "uname") String userName) {
    	UserRegistration userRegistration = userRegistrationRepository.findByUserName(userName);
    	if(null == userRegistration) {
    		new UserResourceException("UserName not found", userName);
    	}
    	LOGGER.info("getRegisterUserByUserName -> Fetch the user with userName as : {}", userName);
        return userRegistration;
    }
	
	@DeleteMapping("/deleteUser/{id}")
    public void deleteRegisterUser(@PathVariable(value = "id") String userId) {
		UserRegistration userRegistration = userRegistrationRepository.findById(userId)
				.orElseThrow(() -> new UserResourceException("UserId not found", userId));
	    userRegistrationRepository.delete(userRegistration);
    	LOGGER.info("deleteRegisterUser -> Delete the user of userId : {}", userId);
    }
	
	@PostMapping("/registerUserLogin")
    public UserRegistration registeredUserLogin(@RequestBody UserRegistration userRegistration) {
		UserRegistration fetchedUserRegistration = null;
		if(StringUtils.hasText(userRegistration.getUserName())) {
	    	fetchedUserRegistration = userRegistrationRepository.findByUserName(userRegistration.getUserName());
	    	if(null != fetchedUserRegistration && 
	    			(!fetchedUserRegistration.getPassword().equals(userRegistration.getPassword()))) {
				LOGGER.info("registeredUserLogin -> Password is not correct for the user : {}", userRegistration.getUserName());
				return null;
	    	}
		} else {
			LOGGER.info("registeredUserLogin -> User is not registered : {}", userRegistration.getUserName());
			return null;
		}
        return fetchedUserRegistration;
    }
	
	@PostMapping("/changePassword")
    public UserRegistration updatePassword(@RequestBody UserRegistration userRegistration) {
    	UserRegistration registration = userRegistrationRepository.findByUserName(userRegistration.getUserName());
    	if(null != registration) {
    		if((StringUtils.hasText(userRegistration.getPassword()) && StringUtils.hasText(userRegistration.getConfirmPassword())) 
    				&& (userRegistration.getPassword().equals(userRegistration.getConfirmPassword()))) {
    			registration.setPassword(userRegistration.getPassword());
        		registration.setConfirmPassword(userRegistration.getConfirmPassword());
        		userRegistrationRepository.save(registration);
    		} else if(StringUtils.hasText(userRegistration.getUserName())) {
    			// Send New Temporary Password as Notification to the User - Need to implement this piece of code
    			String generateRandomPassword = PasswordGenerator.generateRandomPassword(10);
    			registration.setPassword(generateRandomPassword);
    			registration.setConfirmPassword(generateRandomPassword);
    			userRegistrationRepository.save(registration);
    		}
    	} else {
    		throw new UserResourceException("UserName not found", userRegistration.getUserName());
    	}
    	LOGGER.info("updatePassword -> Password is updated for the userName : {}", userRegistration.getUserName());
        return registration;
    }
}
