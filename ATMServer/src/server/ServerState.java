/**
 * Data object for server state
 */
package server;

import java.io.Serializable;

public class ServerState implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public UserBase userbase;
	public String motd;
}
