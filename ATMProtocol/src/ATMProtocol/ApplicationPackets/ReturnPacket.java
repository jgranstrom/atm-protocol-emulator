/**
 * ApplicationPacket - ReturnPacket
 */
package ATMProtocol.ApplicationPackets;

import ATMProtocol.ApplicationPackets.Headers.ReturnHeader;

public class ReturnPacket extends ApplicationPacket {
	/**** Accessors ****/
	public byte getReturnCode()
	{
		return ((ReturnHeader)this.getHeader()).getReturnCode();
	}
	
	/*
	 * Constructor for creating new packet
	 */
	public ReturnPacket(byte returnCode)
	{
		super(ReturnHeader.create(returnCode));
	}
	
	/*
	 * Create AuthPacket from incoming data.
	 * Will throw Exception on invalid data.
	 */
	public ReturnPacket(byte[] pkt) throws Exception
	{	
		super(ReturnHeader.decode(pkt[0]));
	}

	@Override
	public byte[] make() {		
		return super.internalMake(new byte[3]); // Provide the ability to include additional return information in the future
	}
}
