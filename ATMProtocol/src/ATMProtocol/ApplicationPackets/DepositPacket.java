/**
 * ApplicationPacket - DepositPacket
 */
package ATMProtocol.ApplicationPackets;

import java.nio.ByteBuffer;

import ATMProtocol.Opcodes;
import ATMProtocol.ApplicationPackets.Headers.OperationalHeader;

public class DepositPacket extends ApplicationPacket {
	private int amount;
	
	public int getAmount()
	{
		return this.amount;
	}

	public DepositPacket(int amount)
	{		
		super(OperationalHeader.create(Opcodes.DEPOSIT));
		this.amount = amount;
	}
	
	public DepositPacket(byte[] pkt) throws Exception
	{
		super(OperationalHeader.decode(pkt[0], Opcodes.DEPOSIT));
		
		this.amount = ByteBuffer.wrap(pkt, 1, 4).getInt();
	}

	@Override
	public byte[] make() {
		byte[] data = ByteBuffer.allocate(4).putInt(amount).array();
		return super.internalMake(data);
	}
}
