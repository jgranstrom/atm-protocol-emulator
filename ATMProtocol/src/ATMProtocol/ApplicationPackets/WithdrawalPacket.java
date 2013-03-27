/**
 * ApplicationPacket - WithdrawalPacket
 */
package ATMProtocol.ApplicationPackets;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import ATMProtocol.Opcodes;
import ATMProtocol.ApplicationPackets.Headers.OperationalHeader;

public class WithdrawalPacket extends ApplicationPacket {
	private int amount;
	private String code;
	
	public int getAmount()
	{
		return this.amount;
	}
	
	public String getCode()
	{
		return this.code;
	}
	
	public WithdrawalPacket(int amount, String code)
	{		
		super(OperationalHeader.create(Opcodes.WITHDRAWAL));
		this.amount = amount;
		this.code = code;
	}
	
	public WithdrawalPacket(byte[] pkt) throws Exception
	{
		super(OperationalHeader.decode(pkt[0], Opcodes.WITHDRAWAL));
		
		this.amount = ByteBuffer.wrap(pkt, 1, 4).getInt();
		
		byte[] codeData = new byte[2];
		System.arraycopy(pkt, 5, codeData, 0, 2);
		this.code = new String(codeData, "ASCII");
	}

	@Override
	public byte[] make() {
		byte[] data = new byte[6];
	    System.arraycopy(ByteBuffer.allocate(4).putInt(amount).array(), 0, data, 0, 4);
	    try {
			byte[] codeData = code.getBytes("ASCII");
			System.arraycopy(codeData, 0, data, 4, 2);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();			
		}
		return super.internalMake(data);
	}
}
