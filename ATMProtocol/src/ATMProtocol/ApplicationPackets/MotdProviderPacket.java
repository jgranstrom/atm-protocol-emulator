/**
 * ApplicationPacket - MotdProviderPacket
 */
package ATMProtocol.ApplicationPackets;

import java.io.UnsupportedEncodingException;
import ATMProtocol.Opcodes;
import ATMProtocol.ApplicationPackets.Headers.OperationalHeader;

public class MotdProviderPacket extends ApplicationPacket {
	private String motd;
	
	public String getMotd()
	{
		return this.motd;
	}
	
	public MotdProviderPacket(String motd)
	{		
		super(OperationalHeader.create(Opcodes.MOTDPROVIDE));
		this.motd = motd;
	}
	
	public MotdProviderPacket(byte[] pkt) throws Exception
	{
		super(OperationalHeader.decode(pkt[0], Opcodes.MOTDPROVIDE));
		
		byte[] data = new byte[pkt.length - 1];
		System.arraycopy(pkt, 1, data, 0, data.length);

	    this.motd = new String(data, "UTF8");
	}

	@Override
	public byte[] make() {
		try {
			return super.internalMake(this.motd.getBytes("UTF8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
