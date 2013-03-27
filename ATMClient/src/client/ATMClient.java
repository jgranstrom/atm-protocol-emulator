/**
 * Implementation of the ATMClient
 */
package client;

import java.net.*;
import java.io.*;

import ATMCommon.data.Language;
import ATMCommon.helpers.Security;
import ATMProtocol.*;
import ATMProtocol.ApplicationPackets.*;
import ATMProtocol.TransportPackets.TransportationHelpers;


public class ATMClient {
	private static Socket socket = null;
	private static TransportationHelpers transport;
	private static int port = 51123;
	
	private static int currentLangCode = 1;
	private static Language currentLanguage;
	
	/**
	 * Main entry point
	 */
	public static void main(String[] args) throws Exception
	{		
		socket = initConnection("localhost");	
		transport = new TransportationHelpers(socket);
		
		// Get current language data from server
		currentLanguage = requestLanguage(currentLangCode);
		
		// Input
		InputStreamReader converter = new InputStreamReader(System.in);
	    BufferedReader in = new BufferedReader(converter);
		
	    while(true) // Main outer loop
	    {
			// Login user
			while(!login(in))
			{
				// Provide information on what was wrong here
		    	System.out.println(currentLanguage.get("loginfail"));
			}
			
			boolean logout;
			do
			{
				System.out.println(requestMotd()); // Print current motd
				System.out.println(currentLanguage.get("options")); // Print menu options
			
				logout = interact(in);
			}
			while(!logout);
			
			// Logout user
			deauth();
	    }
	}
	
	/**
	 * Interact with user and server
	 */
	private static boolean interact(BufferedReader in) throws Exception
	{
		String s = in.readLine();
		int opt;
		try
		{
			opt = Integer.parseInt(s);
		}
		catch(Exception e)
		{
			return false;
		}
		
		switch(opt)
		{
			case 0: return true;
			case 1: requestBalance(); break;
			case 2: requestWithdrawal(in); break;
			case 3: requestDeposit(in); break;
			case 4: selectLanguage(in);
		}
		
		return false;
	}
	
	/**
	 * Login user
	 */
	private static boolean login(BufferedReader in) throws Exception
	{
		System.out.println(currentLanguage.get("login"));
		
		System.out.println(currentLanguage.get("loginuser"));
		String inputUser = in.readLine();
		System.out.println(currentLanguage.get("loginpass"));
		String inputPass = Security.getHash(in.readLine());
	    
	    AuthPacket pkt = new AuthPacket(inputUser, inputPass);
	    transport.sendPacket(pkt);
	    ReturnPacket ret = (ReturnPacket)transport.receivePacket();
	    if(ret.getReturnCode() == ReturnCodes.AUTH_SUCC)
	    	return true;
	    else		
	    {
	    	return false;
	    }
	}
	
	/**
	 * Logout user
	 */
	private static void deauth() throws Exception
	{
		transport.sendPacket(new DeauthPacket());
	}
	
	/**
	 * Init connection to server
	 */
	private static Socket initConnection(String host)
	{
		try
		{
			Socket socket = new Socket(host, port);			
			return socket;
		}
		catch(UnknownHostException e)
		{
			System.err.println("Unknown host.");
			System.exit(1);
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		return null;
	}
	
	/**
	 * Request language from server by ID
	 */
	private static Language requestLanguage(int langCode) throws Exception
	{
		transport.sendPacket(new GetLangPacket(langCode));
		return ((LangProviderPacket)transport.receivePacket()).getLanguage();
	}
	
	/**
	 * Request current motd from server
	 */
	private static String requestMotd() throws Exception
	{
		transport.sendPacket(new GetMotdPacket());
		return ((MotdProviderPacket)transport.receivePacket()).getMotd();
	}
	
	/**
	 * Request current user's balance from server
	 */
	private static void requestBalance() throws Exception
	{
		transport.sendPacket(new GetBalancePacket());
		System.out.println(currentLanguage.get("balance") + ((BalanceProviderPacket)transport.receivePacket()).getBalance());
	}
	
	/**
	 * Request withdrawal
	 */
	private static void requestWithdrawal(BufferedReader in) throws Exception
	{
		int amount;
		String code;
		System.out.println(currentLanguage.get("withdrawalamount"));
		amount = Integer.parseInt(in.readLine());
		System.out.println(currentLanguage.get("withdrawalcode"));
		code = in.readLine();
		
		transport.sendPacket(new WithdrawalPacket(amount, code)); // Request withdrawal with amount and code from input
		ReturnPacket ret = (ReturnPacket)transport.receivePacket();
		if(ret.getReturnCode() == ReturnCodes.WITHDRAWAL_SUCC)
			System.out.println(currentLanguage.get("withdrawalsucc"));
		else
			System.out.println(currentLanguage.get("withdrawalfail"));
	}
	
	/**
	 * Request deposit
	 */
	private static void requestDeposit(BufferedReader in) throws Exception
	{
		int amount;
		System.out.println(currentLanguage.get("depositamount"));
		amount = Integer.parseInt(in.readLine());
		
		transport.sendPacket(new DepositPacket(amount));
		System.out.println(currentLanguage.get("depositcomplete"));
	}
	
	/**
	 * Provide language selection to user
	 */
	private static void selectLanguage(BufferedReader in)
	{
		System.out.println(currentLanguage.get("langselect"));
		try
		{
			int opt = Integer.parseInt(in.readLine());
			if(opt == 0)
				return;
			
			Language rec = requestLanguage(opt);
			if(rec == null)
				System.out.println(currentLanguage.get("nolang"));
			else
				currentLanguage = rec;
		}
		catch(Exception ex)
		{
			System.out.println(currentLanguage.get("invalidopt"));
		}
	}
}
