/**
 * ApplicationPacket - GetLangPacket
 */
package ATMProtocol.ApplicationPackets;

import java.nio.ByteBuffer;

import ATMProtocol.Opcodes;
import ATMProtocol.ApplicationPackets.Headers.OperationalHeader;

public class GetLangPacket extends ApplicationPacket {
	private int langId;
	
	public int getLangId()
	{
		return this.langId;
	}
	
	public GetLangPacket(int langId)
	{
		super(OperationalHeader.create(Opcodes.GETLANG));
		this.langId = langId;
	}
	
	public GetLangPacket(byte[] pkt) throws Exception
	{
		super(OperationalHeader.decode(pkt[0], Opcodes.GETLANG));
		byte[] data = new byte[4];
		System.arraycopy(pkt, 1, data, 0, data.length);
		this.langId = ByteBuffer.wrap(data).getInt();
	}

	@Override
	public byte[] make() {
		byte[] data = ByteBuffer.allocate(4).putInt(langId).array();
		return super.internalMake(data);
	}
}