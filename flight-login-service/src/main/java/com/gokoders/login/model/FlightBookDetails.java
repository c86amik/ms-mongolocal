/**
 * 
 */
package com.gokoders.login.model;

import java.io.Serializable;

/**
 * @author gokoders
 *
 */
public class FlightBookDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8012425099919238354L;
	
	private String id;
	private String userId;
	private String flightId;
	private String userName;
	private String firstName;
	private String middleName;
	private String lastName;
	private String mobileNo;
	private String email;
	private String passport;
	
	private String flightName;
	private String sourceAirportName;
	private String destinationAirportName;
	private Double bookTicketPrice;
	private String duration;
	
	public FlightBookDetails() {
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFlightId() {
		return flightId;
	}
	public void setFlightId(String flightId) {
		this.flightId = flightId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassport() {
		return passport;
	}
	public void setPassport(String passport) {
		this.passport = passport;
	}
	public String getFlightName() {
		return flightName;
	}
	public void setFlightName(String flightName) {
		this.flightName = flightName;
	}
	public String getSourceAirportName() {
		return sourceAirportName;
	}
	public void setSourceAirportName(String sourceAirportName) {
		this.sourceAirportName = sourceAirportName;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FlightBookDetails [id=");
		builder.append(id);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", flightId=");
		builder.append(flightId);
		builder.append(", userName=");
		builder.append(userName);
		builder.append(", firstName=");
		builder.append(firstName);
		builder.append(", middleName=");
		builder.append(middleName);
		builder.append(", lastName=");
		builder.append(lastName);
		builder.append(", mobileNo=");
		builder.append(mobileNo);
		builder.append(", email=");
		builder.append(email);
		builder.append(", passport=");
		builder.append(passport);
		builder.append(", flightName=");
		builder.append(flightName);
		builder.append(", sourceAirportName=");
		builder.append(sourceAirportName);
		builder.append(", destinationAirportName=");
		builder.append(destinationAirportName);
		builder.append(", bookTicketPrice=");
		builder.append(bookTicketPrice);
		builder.append(", duration=");
		builder.append(duration);
		builder.append("]");
		return builder.toString();
	}
}
