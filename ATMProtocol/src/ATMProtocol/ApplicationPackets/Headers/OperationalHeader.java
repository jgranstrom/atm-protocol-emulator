/**
 * Header for Operational Packets
 */
package ATMProtocol.ApplicationPackets.Headers;

public class OperationalHeader implements Header {
	private byte opcode;
	
	/**** Accessors ****/
	public byte getOpcode()
	{
		return this.opcode;
	}
	
	/**
	 * Constructor
	 */
	private OperationalHeader(byte opcode)
	{
		this.opcode = opcode;
	}
	
	/**** Public methods ****/
	
	public static OperationalHeader create(byte opcode)
	{
		return new OperationalHeader(opcode);
	}
	
	public static OperationalHeader decode(byte in, byte opcode) throws Exception
	{
		if((in & 0xC0) != 0)
			throw new Exception("Invalid header start.");
		if((in & 0x3F) != (opcode & 0x3F))
			throw new Exception("Missmatchning opcode.");
		
		return new OperationalHeader(opcode);
	}

	@Override
	public byte make() {
		return (byte) (0x3F & opcode);
	}
}
