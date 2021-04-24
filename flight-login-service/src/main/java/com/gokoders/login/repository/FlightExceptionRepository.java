/**
 * 
 */
package com.gokoders.login.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gokoders.login.exception.FlightException;

/**
 * @author gokoders
 *
 */
public interface FlightExceptionRepository  extends MongoRepository<FlightException, String>{
	
	public FlightException findByUserName(String userName);

}
