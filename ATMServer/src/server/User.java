/**
 * Representation of client users
 */
package server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ATMCommon.helpers.Security;

public class User implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String username;
	public String passhash;
	public int credit;
	public HashMap<String, Boolean> singeUseCodes = new HashMap<String, Boolean>();
	
	private boolean isLoggedIn = false;
	
	/**
	 * Create a new instance of a user
	 */
	public User(String username, String password, int credit)
	{
		this.username = username;
		this.passhash = Security.getHash(password);
		this.credit = credit;
		
		// Init single-use codes with all uneven numbers 1-99 as not used
		for(int i = 1; i < 100; i+=2)
		{
			String code;
			if(i < 10)
				code = "0" + i;
			else
				code = "" + i;
			singeUseCodes.put(code, false);
		}
	}
	
	/**
	 * Set the users login state
	 */
	public synchronized void setLoggedIn(boolean loggedIn)
	{
		this.isLoggedIn = loggedIn;
	}
	
	/**
	 * Get the users login state
	 */
	public synchronized boolean getLoggedIn()
	{
		return this.isLoggedIn;
	}

	/**
	 * Get a representative String of the user
	 */
	@Override
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		b.append("Username:").append(this.username).append("\n");
		b.append("Passhash:").append(this.passhash).append("\n");
		b.append("Credit:").append(this.credit).append("\n");
		b.append("Used codes:").append("\n");
		
		Iterator<Map.Entry<String, Boolean>> it = singeUseCodes.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, Boolean> pairs = (Map.Entry<String, Boolean>)it.next();
	        if(pairs.getValue())
	        	b.append("\t").append(pairs.getKey()).append("\n");
	    }
	    
		return b.toString();
	}
}
