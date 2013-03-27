/**
 * ApplicationPacket - GetBalancePacket
 */
package ATMProtocol.ApplicationPackets;

import ATMProtocol.Opcodes;
import ATMProtocol.ApplicationPackets.Headers.OperationalHeader;

public class GetBalancePacket extends ApplicationPacket {	
	public GetBalancePacket()
	{
		super(OperationalHeader.create(Opcodes.GETBALANCE));
	}
	
	public GetBalancePacket(byte[] pkt) throws Exception
	{
		super(OperationalHeader.decode(pkt[0], Opcodes.GETBALANCE));
	}

	@Override
	public byte[] make() {
		return super.internalMake(new byte[0]);
	}
}