/**
 * 
 */
package com.gokoders.login.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.gokoders.login.model.UserRegistration;


/**
 * @author gokoders
 *
 */
@FeignClient(name = "flight-registration-service")
public interface RegistrationUserClientProxy {
	
	@PostMapping("/registerUserLogin")
	public UserRegistration registeredUserLogin(@RequestBody UserRegistration userRegistration);
	
	@GetMapping("/users")
    public List<UserRegistration> getAllRegisteredUsers();
	
	@GetMapping("/user/{id}")
	public UserRegistration getRegisterUserById(@PathVariable(value = "id") String userId);
	
	@GetMapping("/fetchUser/{uname}")
    public UserRegistration getRegisterUserByUserName(@PathVariable(value = "uname") String userName);
	
	@PostMapping("/addUser")
	public UserRegistration registerUser(@RequestBody UserRegistration userRegistration);
	
	@DeleteMapping("/deleteUser/{id}")
    public void deleteRegisterUser(@PathVariable(value = "id") String userId);
	
	@PostMapping("/changePassword")
    public UserRegistration updatePassword(@RequestBody UserRegistration userRegistration);
}
