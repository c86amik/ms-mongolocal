/**
 * 
 */
package com.gokoders.login.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gokoders.login.client.FlightSearchClientProxy;
import com.gokoders.login.exception.FlightException;
import com.gokoders.login.model.FlightSearchDetails;
import com.gokoders.login.repository.FlightExceptionRepository;

/**
 * @author gokoders
 *
 */
@Controller
@RequestMapping(path = "/flight")
public class FlightDetailsController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FlightDetailsController.class);
	
	@Autowired
	private FlightSearchClientProxy flightSearchClientProxy;
	@Autowired
	private FlightExceptionRepository flightExceptionRepository;
	
	@RequestMapping(path = "/flights/{identifier}", method = RequestMethod.GET)
    public String getAllFlights(Model model, @PathVariable(value = "identifier") String identifier) {
		LOGGER.info("getAllFlights -> All Flights fetched");
		model.addAttribute("flights", flightSearchClientProxy.getAllFlights());
		if("admin".equalsIgnoreCase(identifier)) {
			return "flightAdminLists";
		} else {
			return "flightLists";
		}
    }
	
	@RequestMapping(path = "/addFlight", method = RequestMethod.GET)
    public String addFlight(Model model) {
        model.addAttribute("flight", new FlightSearchDetails());
        LOGGER.info("addFlight -> New Flight Details added");
        return "flightDetail";
    }
	
	@RequestMapping(path = "/saveFlight", method = RequestMethod.POST)
    public String saveFlight(FlightSearchDetails flightSearchDetails) {
		if(null != flightSearchDetails) {
			flightSearchClientProxy.saveFlightDetails(flightSearchDetails);
			LOGGER.info("saveFlight -> New Flight Details Added");
		}
		return "redirect:/flight/flights/admin";
	}
	
	@RequestMapping(path = "/flight/{id}/{fname}", method = RequestMethod.GET)
	public String getFlightByFlightIdAndFlightName(Model model, @PathVariable(value = "id") String flightId, 
    		@PathVariable(value = "fname") String flightName) {
		FlightSearchDetails flightSearchDetails = null;
    	FlightException flightException = null;
    	if(StringUtils.hasText(flightId)) {
    		flightSearchDetails = flightSearchClientProxy.getFlightDetailsById(flightId);
    		if(null == flightSearchDetails) {
    			flightException = new FlightException(flightId, flightName, 
						"flight not found", flightName, "getFlightByFlightIdAndFlightName()", "FlightDetailsController");
    			flightExceptionRepository.save(flightException);
    		}
    	}
    	model.addAttribute("flight", flightSearchDetails);
    	LOGGER.info("getFlightByFlightIdAndFlightName -> Fetch the flight with flightId as : {}", flightId);
        return "flightDetail";
    }
	
	@RequestMapping(path = "/deleteFlight/{id}/{fname}", method = RequestMethod.GET)
    public String deleteFlightByFlightIdAndFlightName(@PathVariable(value = "id") String flightId, 
    		@PathVariable(value = "fname") String flightName) {
		FlightSearchDetails flightSearchDetails = null;
		FlightException flightException = null;
		if(StringUtils.hasText(flightId)) {
			flightSearchDetails = flightSearchClientProxy.getFlightDetailsById(flightId);
	    	if(null == flightSearchDetails) {
    			flightException = new FlightException(flightId, flightName, 
						"Flight not found", flightName, "deleteFlightByFlightIdAndFlightName()", "LoginController");
    			flightExceptionRepository.save(flightException);
    		}
	    	flightSearchClientProxy.deleteFlightDetails(flightId);
		}
    	LOGGER.info("deleteFlightByFlightIdAndFlightName -> Delete the flight of flightId : {}", flightId);
        return "redirect:/flight/flights/admin";
    }
	
	@RequestMapping(path = "/searchFlight/{identifier}", method = RequestMethod.GET)
    public String searchFlight(Model model, @PathVariable(value = "identifier") String identifier) {
        model.addAttribute("flight", new FlightSearchDetails());
        LOGGER.info("searchFlight -> Search Flight Page");
        if("admin".equalsIgnoreCase(identifier)) {
			return "flightAdminSearch";
		} else {
			return "flightSearch";
		}
    }
	
	@RequestMapping(path = "/searchFlight/{identifier}", method = RequestMethod.POST)
	public String searchFlight(Model model, FlightSearchDetails flightSearchDetails,
			@PathVariable(value = "identifier") String identifier) {
		List<FlightSearchDetails> searchDetails = null;
		//FlightException flightException = null;
		if(null != flightSearchDetails) {
			if(StringUtils.hasText(flightSearchDetails.getSourceAirportName()) && 
					StringUtils.hasText(flightSearchDetails.getDestinationAirportName())) {
				searchDetails = flightSearchClientProxy.getFlightsBySourceAndDestinationAirportNames(flightSearchDetails);
			}
		}
		if(null != searchDetails) {
			model.addAttribute("flights", searchDetails);
		}
    	LOGGER.info("searchFlight -> Fetch the flight lists with source airport as {} and destination airport as {}", 
    			flightSearchDetails.getSourceAirportCode(), flightSearchDetails.getDestinationAirportName());
		if("admin".equalsIgnoreCase(identifier)) {
			return "flightAdminLists";
		} else {
			return "flightLists";
		}
    }

}
