/**
 * Help class for security methods
 */
package ATMCommon.helpers;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Security {
	private static String salt = "abmkeo#)039i";
	
	/**
	 * Get salted iterative hash for provided String
	 */
	public static String getHash(String s)
	{
		String hash = s + salt;
		MessageDigest cript = null;
		try {
			cript = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.exit(1);			
		}
		
		// Iterative hashing
		for(int i = 0; i < 100; i++)
		{
	        cript.reset();
	        try {
				cript.update(hash.getBytes("utf8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				System.exit(1);
			}
	        hash = new BigInteger(1, cript.digest()).toString(16);
		}
		
		return hash;
	}
}
