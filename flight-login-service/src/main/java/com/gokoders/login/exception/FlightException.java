/**
 * 
 */
package com.gokoders.login.exception;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author gokoders
 *
 */
@Document(collection = "flightException")
public class FlightException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6516355976276869122L;

	@Id
	private String id;
	private String userId;
	private String userName;
	private String exceptionMessage;
	private String exceptionCause;
	private String methodName;
	private String className;
	
	public FlightException() {
	}
	
	public FlightException(String userId, String userName, String exceptionMessage, String exceptionCause,
			String methodName, String className) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.exceptionMessage = exceptionMessage;
		this.exceptionCause = exceptionCause;
		this.methodName = methodName;
		this.className = className;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getExceptionMessage() {
		return exceptionMessage;
	}
	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
	public String getExceptionCause() {
		return exceptionCause;
	}
	public void setExceptionCause(String exceptionCause) {
		this.exceptionCause = exceptionCause;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FlightException [id=");
		builder.append(id);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", userName=");
		builder.append(userName);
		builder.append(", exceptionMessage=");
		builder.append(exceptionMessage);
		builder.append(", exceptionCause=");
		builder.append(exceptionCause);
		builder.append(", methodName=");
		builder.append(methodName);
		builder.append(", className=");
		builder.append(className);
		builder.append("]");
		return builder.toString();
	}
}
