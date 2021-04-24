/**
 * 
 */
package com.gokoders.flight.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.gokoders.flight.model.FlightSearchDetails;

/**
 * @author gokoders
 *
 */
@Repository
public interface FlightSearchDetailsRepository extends MongoRepository<FlightSearchDetails, String>{
	
	@Query("{sourceAirportName: '?0', destinationAirportName: '?1'}")
	public List<FlightSearchDetails> findBySourceAirportNameAndDestinationAirportName(String sourceAirportName, String destinationAirportName);
	
	@Query("{bookTicketPrice: {$gt: '?0'}, bookTicketPrice: {$lte: '?1'}}")
	public List<FlightSearchDetails> findByPriceWithinRange(Double minPrice, Double maxPrice);
}
