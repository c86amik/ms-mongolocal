package com.gokoders.book.controller;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gokoders.book.exception.FlightBookException;
import com.gokoders.book.model.FlightBookDetails;
import com.gokoders.book.repository.FlightBookDetailsRepository;


/**
 * 
 */

/**
 * @author gokoders
 *
 */
@RestController
public class FlightBookDetailsController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FlightBookDetailsController.class);
	
	@Autowired
	private FlightBookDetailsRepository flightBookDetailsRepository;
	
	@GetMapping("/bookFlights")
    public List<FlightBookDetails> getAllBookings() {
		LOGGER.info("getAllBookings -> All Flight Bookings are fetched");
		return flightBookDetailsRepository.findAll();
    }
	
	@PostMapping("/bookFlight")
    public FlightBookDetails saveBookingDetails(@RequestBody FlightBookDetails flightBookDetails) {
        LOGGER.info("saveBookingDetails -> New Flight Booking Saved");
        return flightBookDetailsRepository.save(flightBookDetails);
    }

	@GetMapping("/bookFlight/{id}")
    public FlightBookDetails getBookingFlightDetailsById(@PathVariable(value = "id") String bookId) {
    	FlightBookDetails flightBookDetails = flightBookDetailsRepository.findById(bookId)
    			.orElseThrow(() -> new FlightBookException("Booking not found", bookId));
    	LOGGER.info("getBookingFlightDetailsById -> Fetch the Booking Flight Details by bookId as : {}", bookId);
        return flightBookDetails;
    }
	
	@DeleteMapping("/deleteBookFlight/{id}")
    public void deleteBookingFlightDetails(@PathVariable(value = "id") String bookId) {
		FlightBookDetails flightBookDetails = flightBookDetailsRepository.findById(bookId)
				.orElseThrow(() -> new FlightBookException("Booking not found", bookId));
	    flightBookDetailsRepository.delete(flightBookDetails);
    	LOGGER.info("deleteBookingFlightDetails -> Delete the flight booking of bookId : {}", bookId);
    }
	
}
