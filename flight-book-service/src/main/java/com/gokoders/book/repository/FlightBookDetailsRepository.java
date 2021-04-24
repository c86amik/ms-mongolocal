/**
 * 
 */
package com.gokoders.book.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.gokoders.book.model.FlightBookDetails;

/**
 * @author gokoders
 *
 */
@Repository
public interface FlightBookDetailsRepository extends MongoRepository<FlightBookDetails, String>{
}
