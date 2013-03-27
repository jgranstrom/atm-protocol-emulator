/**
 * ApplicationPacket - BalanceProviderPacket
 */
package ATMProtocol.ApplicationPackets;

import java.nio.ByteBuffer;

import ATMProtocol.Opcodes;
import ATMProtocol.ApplicationPackets.Headers.OperationalHeader;

public class BalanceProviderPacket extends ApplicationPacket {
	private int balance;
	
	public int getBalance()
	{
		return this.balance;
	}
	
	public BalanceProviderPacket(int balance)
	{		
		super(OperationalHeader.create(Opcodes.BALANCEPROVIDE));
		this.balance = balance;
	}
	
	public BalanceProviderPacket(byte[] pkt) throws Exception
	{
		super(OperationalHeader.decode(pkt[0], Opcodes.BALANCEPROVIDE));
		
		byte[] data = new byte[pkt.length - 1];
		System.arraycopy(pkt, 1, data, 0, data.length);
			
		this.balance = ByteBuffer.wrap(data).getInt();
	}

	@Override
	public byte[] make() {
	    byte[] data = ByteBuffer.allocate(4).putInt(balance).array();		
		return super.internalMake(data);
	}
}
