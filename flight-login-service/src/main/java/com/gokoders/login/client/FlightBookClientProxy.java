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

import com.gokoders.login.model.FlightBookDetails;

/**
 * @author gokoders
 *
 */
@FeignClient(name = "flight-book-service")
public interface FlightBookClientProxy {
	
	@GetMapping("/bookFlights")
    public List<FlightBookDetails> getAllBookings();
	
	@PostMapping("/bookFlight")
    public FlightBookDetails saveBookingDetails(@RequestBody FlightBookDetails flightBookDetails);
	
	@GetMapping("/bookFlight/{id}")
    public FlightBookDetails getBookingFlightDetailsById(@PathVariable(value = "id") String bookId);
	
	@DeleteMapping("/deleteBookFlight/{id}")
    public void deleteBookingFlightDetails(@PathVariable(value = "id") String bookId);

}
