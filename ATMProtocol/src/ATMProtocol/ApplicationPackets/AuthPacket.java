/**
 * ApplicationPacket - AuthPacket
 */
package ATMProtocol.ApplicationPackets;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import ATMProtocol.Opcodes;
import ATMProtocol.ApplicationPackets.Headers.OperationalHeader;


public class AuthPacket extends ApplicationPacket {
	private String username;
	private String password;
	
	/**** Accessors ****/
	public String getUsername()
	{
		return this.username;
	}
	
	public String getPassword()
	{
		return this.password;
	}
	
	/*
	 * Constructor for creating new packet
	 */
	public AuthPacket(String username, String password)
	{
		super(OperationalHeader.create(Opcodes.AUTH));
		
		this.username = username;
		this.password = password;
	}
	
	/*
	 * Create AuthPacket from incoming data.
	 * Will throw Exception on invalid data.
	 */
	public AuthPacket(byte[] pkt) throws Exception
	{	
		super(OperationalHeader.decode(pkt[0], Opcodes.AUTH));
		try
		{
			byte[] usrLen = new byte[4];
			byte[] pwdLen = new byte[4];
			System.arraycopy(pkt, 1, usrLen, 0, usrLen.length);
			
			byte[] usr = new byte[ByteBuffer.wrap(usrLen).getInt()];
			System.arraycopy(pkt, 1 + usrLen.length, usr, 0, usr.length);
			
			System.arraycopy(pkt, 1 + usrLen.length + usr.length, pwdLen, 0, pwdLen.length);
			
			byte[] pwd = new byte[ByteBuffer.wrap(pwdLen).getInt()];
			System.arraycopy(pkt, 1 + usrLen.length + usr.length + pwdLen.length, pwd, 0, pwd.length);
			
			this.username = new String(usr, "UTF8");
			this.password = new String(pwd, "UTF8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new Exception ("Invalid packet data.");
		}
	}

	@Override
	public byte[] make() {		
		try {
			byte[] usr = username.getBytes("UTF8");
			byte[] pwd = password.getBytes("UTF8");
			byte[] usrLen = ByteBuffer.allocate(4).putInt(usr.length).array();
			byte[] pwdLen = ByteBuffer.allocate(4).putInt(pwd.length).array();
			
			byte[] data = new byte[usr.length + pwd.length + 8];
			
			System.arraycopy(usrLen, 0, data, 0, usrLen.length);
			System.arraycopy(usr, 0, data, usrLen.length, usr.length);
			System.arraycopy(pwdLen, 0, data, usrLen.length + usr.length, pwdLen.length);
			System.arraycopy(pwd, 0, data, usrLen.length + usr.length + pwdLen.length, pwd.length);
			
			return super.internalMake(data);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
