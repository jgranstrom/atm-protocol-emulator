/**
 * ApplicationPacket - LangProviderPacket
 */
package ATMProtocol.ApplicationPackets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import ATMCommon.data.Language;
import ATMProtocol.Opcodes;
import ATMProtocol.ApplicationPackets.Headers.OperationalHeader;

public class LangProviderPacket extends ApplicationPacket {
	private Language language;
	
	public Language getLanguage()
	{
		return this.language;
	}
	
	public LangProviderPacket(Language language)
	{		
		super(OperationalHeader.create(Opcodes.LANGPROVIDE));
		this.language = language;
	}
	
	public LangProviderPacket(byte[] pkt) throws Exception
	{
		super(OperationalHeader.decode(pkt[0], Opcodes.LANGPROVIDE));
		
		if(pkt.length < 2)
			this.language = null;
		else			
		{		
			byte[] data = new byte[pkt.length - 1];
			System.arraycopy(pkt, 1, data, 0, data.length);
			
			ByteArrayInputStream fis = new ByteArrayInputStream(data);	     
		    ObjectInputStream ois = new ObjectInputStream(fis);
		    this.language = (Language)ois.readObject();
		    ois.close();
		}
	}

	@Override
	public byte[] make() {
		if(language == null)
			return super.internalMake(new byte[0]);
		// Serialize Language object
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {			
			ObjectOutputStream objStream = new ObjectOutputStream(stream);
			objStream.writeObject(language);
			objStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	    byte[] data = stream.toByteArray();
		
		return super.internalMake(data);
	}
}
