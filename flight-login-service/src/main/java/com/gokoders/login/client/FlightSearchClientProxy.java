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

import com.gokoders.login.model.FlightSearchDetails;


/**
 * @author gokoders
 *
 */
@FeignClient(name = "flight-search-service")
public interface FlightSearchClientProxy {
	
	@GetMapping("/flights")
    public List<FlightSearchDetails> getAllFlights();
	
	@PostMapping("/addFlight")
    public FlightSearchDetails saveFlightDetails(@RequestBody FlightSearchDetails flightSearchDetails);
	
	@GetMapping("/flight/{id}")
    public FlightSearchDetails getFlightDetailsById(@PathVariable(value = "id") String flightId);
	
	@PostMapping("/flightSearch")
    public List<FlightSearchDetails> getFlightsBySourceAndDestinationAirportNames(@RequestBody FlightSearchDetails searchDetails);
	
	@GetMapping("/flight/{minPrice}{maxPrice}")
    public List<FlightSearchDetails> getFlightsWithinPrice(@PathVariable(value = "minPrice") Double minPrice, 
    		@PathVariable(value = "maxPrice") Double maxPrice);
	
	@DeleteMapping("/deleteFlight/{id}")
    public void deleteFlightDetails(@PathVariable(value = "id") String flightId);

}
