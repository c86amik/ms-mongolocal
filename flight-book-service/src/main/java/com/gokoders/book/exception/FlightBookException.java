/**
 * 
 */
package com.gokoders.book.exception;

/**
 * @author gokoders
 *
 */
public class FlightBookException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6516355976276869122L;

	private String exceptionMessage;
	private String exceptionCause;
	
	public FlightBookException() {
	}
	
	public FlightBookException(String exceptionMessage, String exceptionCause) {
		super();
		this.exceptionMessage = exceptionMessage;
		this.exceptionCause = exceptionCause;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FlightBookException [exceptionMessage=");
		builder.append(exceptionMessage);
		builder.append(", exceptionCause=");
		builder.append(exceptionCause);
		builder.append("]");
		return builder.toString();
	}
}
