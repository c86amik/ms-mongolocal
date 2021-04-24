/**
 * 
 */
package com.gokoders.login.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.gokoders.login.exception.FlightException;
import com.gokoders.login.repository.FlightExceptionRepository;

/**
 * @author gokoders
 *
 */
@Controller
public class LoginErrorController implements ErrorController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginErrorController.class);
	
	@Autowired
	private FlightExceptionRepository flightExceptionRepository;
	
	@RequestMapping(path = "/error/{uname}", method = RequestMethod.GET)
    public String handleError(Model model, @PathVariable(value = "uname") String userName) {
		LOGGER.error("Error has oocured and our Engineers are working on that");
		FlightException flightException = flightExceptionRepository.findByUserName(userName);
        LOGGER.error("handleError -> Error oocured for this {}", userName);
        model.addAttribute("error", flightException);
        return "error";
    }

	@Override
	public String getErrorPath() {
		return "/error";
	}
	
	

}
