/**
 * Handle individual client connections
 * @author John
 *
 */
package server;

import java.net.*;
import ATMProtocol.TransportPackets.*;
import ATMProtocol.ApplicationPackets.*;

public class ClientConnection extends Thread{
	private ATMServer parentServer;
	private Socket socket;	
	private TransportationHelpers transport;
	private RequestProcessor processor;
	
	private User user;
	
	/**
	 * Create a new instance of a client connection
	 */
	public ClientConnection(ATMServer parentServer, Socket socket)
	{
		super("ClientConnectionThread");
		this.parentServer = parentServer;
		this.socket = socket;
		
		this.transport = new TransportationHelpers(socket);
		this.processor = new RequestProcessor(this.transport, this.parentServer);
	}
	
	/**
	 * ClientConnection main loop
	 */
	public void run()
	{
		try
		{
			Log.println("Client connected.");
			
			// Pre main loop processing to init connection
			try
			{
				// Client should request language so force it's handling here
				processor.processLangRequest((GetLangPacket)transport.receivePacket());
			}
			catch(SocketTimeoutException e)
			{
				Log.println("Client timed out pre authentication.");
				return;
			}
			catch(SocketException e)
			{
				Log.println("Client connection lost.");
				return;
			}
			
			// Main loop to handle multiple logins
			boolean connected = true;
			while(connected)
			{
				socket.setSoTimeout(0); // Disable timeout while waiting for user login
				
				try
				{
					// Client should authenticate itself
					boolean loginfailed;
					do
					{
						user = processor.processAuthRequest((AuthPacket)transport.receivePacket());
						loginfailed = user == null;
					}
					while(loginfailed);
				}
				catch(SocketException e)
				{
					Log.println("Client lost while waiting for user authentication, possibly terminated. Ending connection.");
					return;
				}
				
				processor.setUser(user);
				// Set a read timeout to avoid long client inactivity
				socket.setSoTimeout(600000); // 10 minutes timeout
				
				// Inner loop to handle multiple actions from logged in user
				try
				{
					boolean useractive = true;
					while(useractive)
					{
						if(socket.isClosed() || !socket.isConnected())
						{
							Log.println("Client connection lost, possibly timeout.");
							useractive=false;
							break;
						}
						
						ApplicationPacket reqPacket = transport.receivePacket();
						Object res = processor.processRequest(reqPacket);
						
						if(res instanceof DeauthPacket) // Detect logout request
						{
							Log.println("User deauthenticated.");
							useractive=false;
						}
					}
				}
				catch(SocketTimeoutException e)
				{
					Log.println("Client disconnected due to inactivity.");
					connected = false;
				}
				catch(SocketException e)
				{
					Log.println("Client connection lost.");
					connected = false;
				}
			
				user.setLoggedIn(false);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
