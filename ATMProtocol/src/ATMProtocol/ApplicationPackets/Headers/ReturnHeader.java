/**
 * Header for ReturnPackets
 */
package ATMProtocol.ApplicationPackets.Headers;

public class ReturnHeader implements Header {
	private byte returnCode;
	
	/**** Accessors ****/
	public byte getReturnCode()
	{
		return this.returnCode;
	}
	
	/*
	 * Constructor
	 */
	public ReturnHeader(byte returnCode)
	{
		this.returnCode = returnCode;
	}
	
	/**** Public methods ****/

	public static Header create(byte returnCode) {
		return new ReturnHeader(returnCode);
	}
	
	public static Header decode(byte in) throws Exception
	{
		if((in & 0xC0) != 0x40)
			throw new Exception("Invalid header start.");
		
		return new ReturnHeader((byte) (0x3F & in));
	}
	
	@Override
	public byte make() {
		return (byte) ((0x40) | (0x3F & returnCode));
	}
}
