/**
 * Provides a data object for a collection of users
 */
package server;

import java.io.Serializable;
import java.util.HashMap;

public class UserBase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public HashMap<String, User> users = new HashMap<String, User>();
	
	/**
	 * Create a new instance of the UserBase class
	 */
	public UserBase()
	{
		users.put("1234", new User("1234", "123", 1000));
		users.put("2345", new User("2345", "456", 1500));
		users.put("3456", new User("3456", "789", 100));
	}
	
	/**
	 * Get user by ID
	 */
	public synchronized User getUser(String Id)
	{
		return users.get(Id);
	}
	
	/**
	 * Add a user to this UserBase. Will overwrite existing user if same username
	 */
	public synchronized void addUser(String username, String password, int credit)
	{
		users.put(username, new User(username, password, credit));
	}
	
	/**
	 * Remove a user from this UserBase
	 */
	public synchronized boolean removeUser(String username)
	{
		if(users.containsKey(username))
		{
			users.remove(username);
			return true;
		}
		return false;
	}
}
