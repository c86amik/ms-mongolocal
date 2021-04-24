/**
 * 
 */
package com.gokoders.login.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gokoders.login.client.FlightBookClientProxy;
import com.gokoders.login.client.FlightSearchClientProxy;
import com.gokoders.login.client.RegistrationUserClientProxy;
import com.gokoders.login.exception.FlightException;
import com.gokoders.login.model.FlightBookDetails;
import com.gokoders.login.model.FlightSearchDetails;
import com.gokoders.login.model.NotificationMail;
import com.gokoders.login.model.UserRegistration;
import com.gokoders.login.repository.FlightExceptionRepository;
import com.gokoders.login.service.EmailService;

import freemarker.template.TemplateException;

/**
 * @author gokoders
 *
 */
@Controller
@RequestMapping(path = "/book")
public class FlightBookDetailsController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FlightBookDetailsController.class);
	
	@Autowired
	private RegistrationUserClientProxy registrationUserClientProxy;
	@Autowired
	private FlightSearchClientProxy flightSearchClientProxy;
	@Autowired
	private FlightBookClientProxy flightBookClientProxy;
	@Autowired
	private FlightExceptionRepository flightExceptionRepository;
	@Autowired
	private EmailService emailService;
	
	@RequestMapping(path = "/bookFlightLists", method = RequestMethod.GET)
    public String getAllBookFlightsLists(Model model) {
		LOGGER.info("getAllBookFlightsLists -> All Bookings of Flights are fetched");
		model.addAttribute("bookFlights", flightBookClientProxy.getAllBookings());
		return "bookFlightLists";
    }
	
	@RequestMapping(path = "/details/{id}/{fname}", method = RequestMethod.GET)
	public String getFlightDetailsByFlightIdAndFlightName(Model model, @PathVariable(value = "id") String flightId, 
    		@PathVariable(value = "fname") String flightName) {
		FlightBookDetails flightBookDetails = new FlightBookDetails();
		FlightSearchDetails flightSearchDetails = null;
    	FlightException flightException = null;
    	if(StringUtils.hasText(flightId)) {
    		flightSearchDetails = flightSearchClientProxy.getFlightDetailsById(flightId);
    		if(null != flightSearchDetails) {
    			flightBookDetails = convertToFlightBookDetails(flightBookDetails, flightSearchDetails);
    			//flightBookClientProxy.saveBookingDetails(flightBookDetails);
    		} else {
    			flightException = new FlightException(flightId, flightName, 
						"flight not found", flightName, "getFlightDetailsByFlightIdAndFlightName()", "FlightBookDetailsController");
    			flightExceptionRepository.save(flightException);
    			return "redirect:/error/" + flightName;
    		}
    	}
    	model.addAttribute("bookFlight", flightBookDetails);
    	LOGGER.info("getFlightByFlightIdAndFlightName -> Fetch the flight with flightId as : {}", flightId);
        return "flightInfo";
    }
	
	@RequestMapping(path = "/bookFlight", method = RequestMethod.POST)
	public String bookFlight(Model model, FlightBookDetails flightBookDetails) {
		FlightException flightException = null;
		FlightBookDetails savedFlightBookDetails = new FlightBookDetails();
		if(null != flightBookDetails) {
			if(StringUtils.hasText(flightBookDetails.getUserName())) {
				UserRegistration userRegistration = registrationUserClientProxy.getRegisterUserByUserName(flightBookDetails.getUserName());
				if(null != userRegistration) {
					flightBookDetails = convertToFlightBookDetails(flightBookDetails, userRegistration);
					savedFlightBookDetails = flightBookClientProxy.saveBookingDetails(flightBookDetails);
				}
			} else {
				flightException = new FlightException(flightBookDetails.getId(), flightBookDetails.getFlightName(), 
						"flight booking not found", flightBookDetails.getFlightName(), "bookFlight()", "FlightBookDetailsController");
    			flightExceptionRepository.save(flightException);
    			return "redirect:/error/" + flightBookDetails.getFlightName();
			}
		}
		model.addAttribute("bookConfirm", savedFlightBookDetails);
		LOGGER.info("bookFlight -> Book the Flight with necessary information");
		return "bookingConfirmation";
	}
	
	@RequestMapping(path = "/bookTicket", method = RequestMethod.POST)
	public String bookTicket(Model model, FlightBookDetails flightBookDetails) {
		FlightException flightException = null;
		if(null != flightBookDetails) {
			if(StringUtils.hasText(flightBookDetails.getId())) {
				flightBookDetails = flightBookClientProxy.getBookingFlightDetailsById(flightBookDetails.getId());
				NotificationMail mail = createNotificationMail(flightBookDetails);
				try {
					emailService.sendEmail(mail, "book-ticket-template.ftl");
				} catch (MessagingException | IOException | TemplateException e) {
					LOGGER.error("Exception thrown while sending mail : " + e.getMessage());
				}
			} else {
				flightException = new FlightException(flightBookDetails.getId(), flightBookDetails.getFlightName(), 
						"flight booking not found", flightBookDetails.getFlightName(), "bookTicket()", "FlightBookDetailsController");
				flightExceptionRepository.save(flightException);
				return "redirect:/error/" + flightBookDetails.getFlightName();
			}
		}
		model.addAttribute("bookTicket", flightBookDetails);
		LOGGER.info("bookTicket -> Book the Ticket for the Flight with necessary information");
		return "confirmTicket";
	}
	
	@RequestMapping(path = "/viewBookFlight/{id}/{fname}", method = RequestMethod.GET)
	public String viewBookingDetails(Model model, @PathVariable(value = "id") String bookId, 
    		@PathVariable(value = "fname") String flightName) {
		FlightBookDetails flightBookDetails = null;
    	FlightException flightException = null;
    	if(StringUtils.hasText(bookId)) {
    		flightBookDetails = flightBookClientProxy.getBookingFlightDetailsById(bookId);
    		if(null == flightBookDetails) {
    			flightException = new FlightException(bookId, flightName, 
						"booking not found", flightName, "viewBookingDetails()", "FlightBookDetailsController");
    			flightExceptionRepository.save(flightException);
    			return "redirect:/error/" + flightName;
    		}
    	}
    	model.addAttribute("bookConfirm", flightBookDetails);
    	LOGGER.info("viewBookingDetails -> Fetch the booking information with bookId as : {}", bookId);
        return "viewBookTicket";
    }
	
	@RequestMapping(path = "/deleteBookFlight/{id}/{fname}", method = RequestMethod.GET)
	public String deleteBookingDetails(Model model, @PathVariable(value = "id") String bookId, 
    		@PathVariable(value = "fname") String flightName) {
		FlightBookDetails flightBookDetails = null;
    	FlightException flightException = null;
    	if(StringUtils.hasText(bookId)) {
    		flightBookDetails = flightBookClientProxy.getBookingFlightDetailsById(bookId);
    		if(null == flightBookDetails) {
    			flightException = new FlightException(bookId, flightName, 
						"booking not found", flightName, "deleteBookingDetails()", "FlightBookDetailsController");
    			flightExceptionRepository.save(flightException);
    			return "redirect:/error/" + flightName;
    		}
    		flightBookClientProxy.deleteBookingFlightDetails(bookId);
    	}
    	LOGGER.info("deleteBookingDetails -> Delete the booking information with bookId as : {}", bookId);
        return "redirect:/book/bookFlightLists";
    }
	
	/**
	 * This method returns FlightBookDetails object by getting the properties from 
	 * FlightSearchDetails Object
	 * @param flightBookDetails
	 * @param flightSearchDetails
	 * @return FlightBookDetails
	 */
	private FlightBookDetails convertToFlightBookDetails(FlightBookDetails flightBookDetails, 
			FlightSearchDetails flightSearchDetails) {
		if(null != flightSearchDetails) {
			flightBookDetails.setFlightId(flightSearchDetails.getId());
			flightBookDetails.setFlightName(flightSearchDetails.getFlightName());
			flightBookDetails.setSourceAirportName(flightSearchDetails.getSourceAirportName());
			flightBookDetails.setDestinationAirportName(flightSearchDetails.getDestinationAirportName());
			flightBookDetails.setBookTicketPrice(flightSearchDetails.getBookTicketPrice());
			flightBookDetails.setDuration(flightSearchDetails.getDuration());
		}
		return flightBookDetails;
	}
	
	/**
	 * This method returns FlightBookDetails object by getting the properties from 
	 * UserRegistration Object
	 * @param flightBookDetails
	 * @param userRegistration
	 * @return FlightBookDetails
	 */
	private FlightBookDetails convertToFlightBookDetails(FlightBookDetails flightBookDetails, 
			UserRegistration userRegistration) {
		if(null != userRegistration) {
			flightBookDetails.setUserId(userRegistration.getId());
			flightBookDetails.setUserName(userRegistration.getUserName());
			flightBookDetails.setFirstName(userRegistration.getFirstName());
			flightBookDetails.setMiddleName(userRegistration.getMiddleName());
			flightBookDetails.setLastName(userRegistration.getLastName());
			flightBookDetails.setMobileNo(userRegistration.getMobileNo());
			flightBookDetails.setEmail(userRegistration.getEmail());
			flightBookDetails.setPassport(userRegistration.getPassport());
		}
		return flightBookDetails;
	}
	
	private NotificationMail createNotificationMail(FlightBookDetails flightBookDetails) {
		NotificationMail notificationMail = new NotificationMail();
		notificationMail.setFrom("amikevergreen.whitepeaksoft@gmail.com");
		notificationMail.setTo(flightBookDetails.getEmail());
		notificationMail.setSubject("Ticket Confirmed for " + flightBookDetails.getFirstName() + " " + flightBookDetails.getMiddleName() + " " + flightBookDetails.getLastName());
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("fullName", flightBookDetails.getFirstName() + " " + flightBookDetails.getMiddleName() + " " + flightBookDetails.getLastName());
		model.put("sourceAirportName", flightBookDetails.getSourceAirportName());
		model.put("destinationAirportName", flightBookDetails.getDestinationAirportName());
		model.put("flightName", flightBookDetails.getFlightName());
		model.put("bookTicketPrice", flightBookDetails.getBookTicketPrice());
		model.put("duration", flightBookDetails.getDuration());
		model.put("passport", flightBookDetails.getPassport());
		model.put("mobileNo", flightBookDetails.getMobileNo());
		model.put("email", flightBookDetails.getEmail());
		model.put("signature", "From WhitePeakSoft Team");
		notificationMail.setModel(model);
		
		return notificationMail;
	}

}
