package server;

import java.net.*;
import java.io.*;

public class ATMServer extends Thread {
	private static int port = 51123;
	private static String stateFile = "state.dat";
	private static ATMServer server;
	
	ServerSocket serverSocket = null;
	Boolean listening = true;
	
	UserBase users;
	private String motd = "Message of the day.";
	
	public ATMServer()
	{       
		
	}
	
	/**
	 * Run server main loop
	 */
	public void run()
	{
		loadState();
		Languages.init();
		
        try {
            serverSocket = new ServerSocket(port); 
        } catch (IOException e) {
            System.err.println("Could not listen on port: " + port);
            System.exit(1);
        }
	
        try
        {
        	mainLoop();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	return;
        }
	}
	
	/**
	 * Load saved state
	 */
	private void loadState()
	{
		File f = new File(stateFile);
		if(f.exists())
		{			
			FileInputStream fi = null;
			try {
				fi = new FileInputStream(stateFile);
				ObjectInputStream objStream = new ObjectInputStream(fi);
				ServerState state = (ServerState) objStream.readObject();
				
				if(state == null || state.userbase == null) // Fallback
					this.users = new UserBase();
				else
				{
					this.users = state.userbase;
					this.motd = state.motd;
				}
				objStream.close();
				return;
			} catch (Exception e) {
				if(fi != null)
					try {
						fi.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				e.printStackTrace();
			}
		}
		else
			this.users = new UserBase();
	}
	
	/**
	 * Main loop
	 */
	private void mainLoop() throws IOException
	{
		System.out.println("Bank started listening on port: " + port);
		
        while (true)
        {
        	synchronized(listening) // Thread safe access to listening bool
        	{
        		if(!listening)
        			break;
        	}
            new ClientConnection(this, serverSocket.accept()).start();
        }

        System.out.println("Server stopping.");
        serverSocket.close();
	}
	
	/**
	 * Get the current user base
	 */
	public UserBase getUserBase()
	{
		return this.users;
	}
	
	/**
	 * End listening
	 */
	public void stopListening()
	{
		synchronized(listening)
		{
			listening = false;
		}
	}
	
	/**
	 * Get current motd
	 */
	public synchronized String getMotd()
	{
		return this.motd;
	}
	
	/**
	 * Set current motd
	 */
	public synchronized void setMotd(String s)
	{
		this.motd = s;
	}
	
	/**
	 * Save current state to file for later use
	 */
	public void saveState()
	{		
		FileOutputStream fo = null;
		try {
			fo = new FileOutputStream(stateFile);
			ObjectOutputStream objStream = new ObjectOutputStream(fo);
			
			ServerState state = new ServerState();
			state.userbase = users;
			state.motd = motd;			
			objStream.writeObject(state);
		} catch (Exception e) {
			e.printStackTrace();
		} finally
		{
			if(fo != null)
				try {
					fo.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	/**
	 * Main entry point
	 */
	public static void main(String[] args) throws IOException {
		// Start server main loop on background thread
        server = new ATMServer();
        server.setDaemon(true);
        server.start();
        
        InputStreamReader converter = new InputStreamReader(System.in);
	    BufferedReader in = new BufferedReader(converter);
        // Server command loop
        while(true)
        {
		    String s = in.readLine();
		    
		    // Handle some basic commands
		    if(s.equals(":q"))
		    	break;
		    else if(s.startsWith(":motd="))
		    {
		    	if(s.length() > 6)
		    	{
		    		if(s.length() > 86)
		    			System.out.println("Motd too long. Max 80 chars.");
		    		else
		    			server.setMotd(s.substring(6));
		    	}
		    	else
		    		server.setMotd("");
		    }
		    else if(s.equals(":lu"))
		    {
		    	for(User u : server.getUserBase().users.values())
		    	{
		    		System.out.println(u);
		    	}
		    }
		    else if(s.startsWith(":au"))
		    {
		    	String[] inp = s.split(" ");
	    		if(inp.length == 4)
	    		{
	    			server.getUserBase().addUser(inp[1], inp[2], Integer.parseInt(inp[3]));
	    			System.out.println("User added.");
	    		}		    	
	    		else
	    			System.out.println("Invalid command arguments.");
		    }
		    else if(s.startsWith(":ru"))
		    {
		    	String[] inp = s.split(" ");
	    		if(inp.length == 2)
	    		{
	    			if(server.getUserBase().removeUser(inp[1]))
	    				System.out.println("User removed.");
	    			else
	    				System.out.println("No such user.");
	    		}		    	
	    		else
	    			System.out.println("Invalid command arguments.");
		    }
        }
        
        server.saveState();
    }
}
