/**
 * MultiPacket init packet
 */
package ATMProtocol.TransportPackets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class MultiPacketInit extends TransportPacket {
	private byte[] data;
	private int totalLen;
	
	public int getTotalLen()
	{
		return this.totalLen;
	}
	
	/**
	 * Create Init packet from application layer data part and total length
	 */
	public MultiPacketInit(byte[] partData, int totalLen)
	{
		this.totalLen = totalLen;
		
		// MultiPacket header (5 bytes long)
		byte[] header = new byte[5];
		header[0] = (byte)(0x80 | (0x1F & partData.length));		
		System.arraycopy(ByteBuffer.allocate(4).putInt(totalLen).array(), 0, header, 1, 4);
		
		// Place data into rest of init packet
		data = new byte[partData.length + header.length];
		System.arraycopy(header, 0, data, 0, header.length);
		System.arraycopy(partData, 0, data, header.length, partData.length);
	}
	
	/**
	 * Unpack init packet from transportation layer data
	 */
	public MultiPacketInit(byte[] pkt) throws Exception
	{		
		 if((pkt[0] & 0xE0) != (0x80))
			 throw new Exception ("Invalid MultiPacket header.");
		 
		 this.data = pkt;
		 
		 byte[] totLenBytes = new byte[4];
		 System.arraycopy(pkt, 1, totLenBytes, 0, totLenBytes.length);
		 this.totalLen = ByteBuffer.wrap(totLenBytes).getInt();
	}
	
	/**
	 * Read init packet from provided stream
	 */
	public static MultiPacketInit read(byte header, InputStream stream) throws Exception
	{
		// Init packets should always be 10 bytes
		byte[] initPkt = new byte[10];
		initPkt[0] = header;
		int read = stream.read(initPkt, 1, 9);
		if(read != 9)
			throw new Exception("Incomplete MultiPacket init.");
		
		return new MultiPacketInit(initPkt);
	}
	
	/**
	 * Send init packet to provided stream
	 */
	@Override
	public void send(OutputStream stream) throws IOException {
		stream.write(this.data);
	}

	/**
	 * Get application layer data for init packet
	 */
	@Override
	public byte[] getApplicationData() {
		byte[] appData = new byte[5];
		System.arraycopy(data, 5, appData, 0, appData.length);
		return appData;
	} 
}
