/**
 * 
 */
package com.gokoders.login.model;

import java.io.Serializable;

/**
 * @author gokoders
 *
 */
public class FlightSearchDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6588776998883847584L;
	
	private String id;
	private String flightName;
	private String flightType;
	private String sourceAirportCode;
	private String sourceAirportName;
	private String destinationAirportCode;
	private String destinationAirportName;
	private Double bookTicketPrice;
	private String duration;
	private Long stops;
	
	private Boolean isCancelled;
	
	public FlightSearchDetails() {
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFlightName() {
		return flightName;
	}
	public void setFlightName(String flightName) {
		this.flightName = flightName;
	}
	public String getFlightType() {
		return flightType;
	}
	public void setFlightType(String flightType) {
		this.flightType = flightType;
	}
	public String getSourceAirportCode() {
		return sourceAirportCode;
	}
	public void setSourceAirportCode(String sourceAirportCode) {
		this.sourceAirportCode = sourceAirportCode;
	}
	public String getSourceAirportName() {
		return sourceAirportName;
	}
	public void setSourceAirportName(String sourceAirportName) {
		this.sourceAirportName = sourceAirportName;
	}
	public String getDestinationAirportCode() {
		return destinationAirportCode;
	}
	public void setDestinationAirportCode(String destinationAirportCode) {
		this.destinationAirportCode = destinationAirportCode;
	}
	public String getDestinationAirportName() {
		return destinationAirportName;
	}
	public void setDestinationAirportName(String destinationAirportName) {
		this.destinationAirportName = destinationAirportName;
	}
	public Double getBookTicketPrice() {
		return bookTicketPrice;
	}
	public void setBookTicketPrice(Double bookTicketPrice) {
		this.bookTicketPrice = bookTicketPrice;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public Long getStops() {
		return stops;
	}
	public void setStops(Long stops) {
		this.stops = stops;
	}
	public Boolean isCancelled() {
		return isCancelled;
	}
	public void setCancelled(Boolean isCancelled) {
		this.isCancelled = isCancelled;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FlightSearchDetails [id=");
		builder.append(id);
		builder.append(", flightName=");
		builder.append(flightName);
		builder.append(", flightType=");
		builder.append(flightType);
		builder.append(", sourceAirportCode=");
		builder.append(sourceAirportCode);
		builder.append(", sourceAirportName=");
		builder.append(sourceAirportName);
		builder.append(", destinationAirportCode=");
		builder.append(destinationAirportCode);
		builder.append(", destinationAiportName=");
		builder.append(destinationAirportName);
		builder.append(", bookTicketPrice=");
		builder.append(bookTicketPrice);
		builder.append(", duration=");
		builder.append(duration);
		builder.append(", stops=");
		builder.append(stops);
		builder.append(", isCancelled=");
		builder.append(isCancelled);
		builder.append("]");
		return builder.toString();
	}
}
