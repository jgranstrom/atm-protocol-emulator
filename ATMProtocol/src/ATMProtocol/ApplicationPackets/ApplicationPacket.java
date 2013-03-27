/**
 * ApplicationPacket base class with generic methods
 */
package ATMProtocol.ApplicationPackets;

import ATMProtocol.Opcodes;
import ATMProtocol.ApplicationPackets.Headers.Header;
import ATMProtocol.TransportPackets.TransportPacket;

public abstract class ApplicationPacket {
	private Header header;

	/**** Accessors ****/
	public Header getHeader()
	{
		return this.header;
	}
	
	/**
	 * Constructor
	 */
	public ApplicationPacket(Header header)
	{
		this.header = header;
	}
	
	/**
	 * Unpack received operational packet into proper ApplicationPacket determined by header
	 */
	private static ApplicationPacket unpackOperationalPacket(byte header, byte[] data) throws Exception
	{
		byte opcode = (byte)(0x3F & header);
		switch(opcode)
		{
			case Opcodes.AUTH: return new AuthPacket(data);
			case Opcodes.GETLANG: return new GetLangPacket(data); 
			case Opcodes.LANGPROVIDE: return new LangProviderPacket(data); 
			case Opcodes.GETMOTD: return new GetMotdPacket(data);
			case Opcodes.MOTDPROVIDE: return new MotdProviderPacket(data);
			case Opcodes.DEAUTH: return new DeauthPacket(data);
			case Opcodes.GETBALANCE: return new GetBalancePacket(data);
			case Opcodes.BALANCEPROVIDE: return new BalanceProviderPacket(data);
			case Opcodes.WITHDRAWAL: return new WithdrawalPacket(data);
			case Opcodes.DEPOSIT: return new DepositPacket(data);
			default: throw new Exception("Unknown opcode for ApplicationPacket.");
		}
	}
	
	private static ApplicationPacket unpackReturnPacket(byte header, byte[] data) throws Exception
	{
		return new ReturnPacket(data); 
	}
	
	/**** Public methods ****/
	
	public abstract byte[] make();
	
	/**
	 * Call from subclass to merge header and databytes to complete packet
	 */
	protected byte[] internalMake(byte[] data)
	{
		byte[] complete = new byte[data.length + 1];
		complete[0] = header.make();
		System.arraycopy(data, 0, complete, 1, data.length);
		
		return complete;
	}
	
	/**
	 * Unpack TransportPacket into proper ApplicationPacket type
	 */
	public static ApplicationPacket unpack(TransportPacket pkt) throws Exception
	{
		byte[] appData = pkt.getApplicationData();
		byte header = appData[0];
		byte prehead = (byte)(header & 0xC0);
		
		if(prehead == 0)
			return unpackOperationalPacket(header, appData);
		else if(prehead == 0x40)
			return unpackReturnPacket(header, appData);
		else
			throw new Exception("Invalid ApplicationPacket header.");
	}
}
