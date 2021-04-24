/**
 * 
 */
package com.gokoders.registration.utility;

import java.security.SecureRandom;

/**
 * @author gokoders
 *
 */
public class PasswordGenerator {
	
	public static String generateRandomPassword(int len)
	{
		// ASCII range - alphanumeric (0-9, a-z, A-Z)
		final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			int randomIndex = random.nextInt(chars.length());
			sb.append(chars.charAt(randomIndex));
		}
		return sb.toString();
	}
}
