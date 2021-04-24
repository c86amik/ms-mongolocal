/**
 * 
 */
package com.gokoders.registration.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.gokoders.registration.model.UserRegistration;

/**
 * @author gokoders
 *
 */
@Repository
public interface UserRegistrationRepository extends MongoRepository<UserRegistration, String>{
	
	public UserRegistration findByUserName(String userName);

}
