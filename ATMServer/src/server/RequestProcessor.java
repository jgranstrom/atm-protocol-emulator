/**
 * Process requests sent to the server
 */
package server;

import ATMProtocol.ReturnCodes;
import ATMProtocol.ApplicationPackets.*;
import ATMProtocol.TransportPackets.TransportationHelpers;

public class RequestProcessor {
	private TransportationHelpers transport;
	private ATMServer server;
	private User user;
	
	/**
	 * Create a new instance of the request processor
	 */
	public RequestProcessor(TransportationHelpers transport, ATMServer server)
	{
		this.transport = transport;
		this.server = server;
	}
	
	/**
	 * Process received server request 
	 */
	public Object processRequest(ApplicationPacket req) throws Exception
	{
		// Could be refactored to generateResponse()-methods in each packet class
		// to give a more object-oriented approach..
		
		if(req instanceof GetLangPacket)
			return processLangRequest((GetLangPacket)req);
		else if(req instanceof GetMotdPacket)
			return processMotdRequest((GetMotdPacket)req);
		else if(req instanceof GetBalancePacket)
			return processBalanceRequest((GetBalancePacket)req);
		else if(req instanceof WithdrawalPacket)
			return processWithdrawalRequest((WithdrawalPacket)req);
		else if(req instanceof DepositPacket)
			return processDepositRequest((DepositPacket)req);
		else if(req instanceof DeauthPacket)
			return req;
		else
			throw new Exception("Unhandled ApplicationPacket.");
	}
	
	/**
	 * Process GetLang request
	 */
	public Object processLangRequest(GetLangPacket pkt) throws Exception
	{
		Log.println("Language request received for Id: " + pkt.getLangId());
		transport.sendPacket(new LangProviderPacket(Languages.getLanguage(pkt.getLangId())), true);
		Log.println("Language data sent");
		
		return null;
	}
	
	/**
	 * Process Auth request
	 * @return Request-specific objects or null if not used
	 */
	public User processAuthRequest(AuthPacket pkt) throws Exception
	{
		// Determine if login successfull or not and return result
		User usercmp = server.getUserBase().getUser(pkt.getUsername());
		if(usercmp == null || !usercmp.passhash.equals(pkt.getPassword()) || usercmp.getLoggedIn())
		{
			Log.println("Client authentication failed.");
			transport.sendPacket(new ReturnPacket(ReturnCodes.AUTH_FAIL), false);
			return null;
		}
		else
		{
			Log.println("Client authentication succeeded.");
			usercmp.setLoggedIn(true);
			transport.sendPacket(new ReturnPacket(ReturnCodes.AUTH_SUCC), false);
			return usercmp;
		}
	}
	
	/**
	 * Process GetMotd request
	 */
	public Object processMotdRequest(GetMotdPacket pkt) throws Exception
	{
		Log.println("MOTD Requested");
		transport.sendPacket(new MotdProviderPacket(server.getMotd()));
		Log.println("MOTD Sent.");
		
		return null;
	}
	
	/**
	 * Process GetBalance request
	 */
	public Object processBalanceRequest(GetBalancePacket pkt) throws Exception
	{
		Log.println("Balance Requested");
		transport.sendPacket(new BalanceProviderPacket(user.credit));
		
		return null;
	}
	
	/**
	 * Process Withdrawal request
	 */
	public Object processWithdrawalRequest(WithdrawalPacket pkt) throws Exception
	{
		Log.println("Withdrawal Requested for: " + pkt.getAmount() + " with code: " + pkt.getCode());
		if(user.singeUseCodes.containsKey(pkt.getCode()) && !user.singeUseCodes.get(pkt.getCode())) // Make sure code is valid and unused
		{
			if(pkt.getAmount() > 0) // Can only withdraw positive amounts
			{
				if(user.credit >= pkt.getAmount()) // Make sure user has enough credit
				{
					user.singeUseCodes.put(pkt.getCode(), true); // Set code as used
					user.credit -= pkt.getAmount(); 
					transport.sendPacket(new ReturnPacket(ReturnCodes.WITHDRAWAL_SUCC));
					return null;
				}
			}
		}
		
		transport.sendPacket(new ReturnPacket(ReturnCodes.WITHDRAWAL_FAIL)); 
		return null;
	}
	
	/**
	 * Process Deposit request
	 */
	public Object processDepositRequest(DepositPacket pkt) throws Exception
	{
		if(pkt.getAmount() > 0) // Can only deposit positive amounts
		{
			Log.println("Desposit Requested for: " + pkt.getAmount());
			user.credit += pkt.getAmount();
		}
		return null;
	}
	
	/**
	 * Set the current user for this request processor
	 */
	public void setUser(User user)
	{
		this.user = user;
	}
}
