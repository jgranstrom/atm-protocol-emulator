/**
 * ApplicationPacket - DeauthPacket
 */
package ATMProtocol.ApplicationPackets;

import ATMProtocol.Opcodes;
import ATMProtocol.ApplicationPackets.Headers.OperationalHeader;

public class DeauthPacket extends ApplicationPacket {	
	public DeauthPacket()
	{
		super(OperationalHeader.create(Opcodes.DEAUTH));
	}
	
	public DeauthPacket(byte[] pkt) throws Exception
	{
		super(OperationalHeader.decode(pkt[0], Opcodes.DEAUTH));
	}

	@Override
	public byte[] make() {
		return super.internalMake(new byte[0]);
	}
}