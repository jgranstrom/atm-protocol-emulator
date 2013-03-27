/**
 * ApplicationPacket - GetMotdPacket
 */
package ATMProtocol.ApplicationPackets;

import ATMProtocol.Opcodes;
import ATMProtocol.ApplicationPackets.Headers.OperationalHeader;

public class GetMotdPacket extends ApplicationPacket {	
	public GetMotdPacket()
	{
		super(OperationalHeader.create(Opcodes.GETMOTD));
	}
	
	public GetMotdPacket(byte[] pkt) throws Exception
	{
		super(OperationalHeader.decode(pkt[0], Opcodes.GETMOTD));
	}

	@Override
	public byte[] make() {
		return super.internalMake(new byte[0]);
	}
}