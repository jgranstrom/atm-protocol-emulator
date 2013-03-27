/**
 * MultiPacket part packet
 */
package ATMProtocol.TransportPackets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MultiPacketPart extends TransportPacket {
	private byte[] data;
	private byte partLen;
	
	public byte getPartLen()
	{
		return this.partLen;
	}
	
	/**
	 * Create part packet from application layer data
	 */
	public MultiPacketPart(byte[] partData)
	{
		this.partLen = (byte)partData.length;
		
		// MultiPacket part header (packet length is included in header)
		byte header = (byte)(0xC0 | (partLen & 0x1F));
		
		this.data = new byte[partLen + 1];
		data[0] = header;
		System.arraycopy(partData, 0, data, 1, partLen);
	}
	
	/**
	 * Read part packet from provided stream
	 */
	public static MultiPacketPart read(InputStream stream) throws Exception
	{
		byte[] header = new byte[1];
		stream.read(header);
		
		if((0xE0 & header[0]) != 0xC0) // Make sure it's a multipacket part
			throw new Exception("Invalid header in MultiPacket part.");
		
		byte cPartLen = (byte)(0x1F & header[0]); // Get packet length from header
		
		byte[] data = new byte[cPartLen];
		stream.read(data); // Read rest of data
		
		return new MultiPacketPart(data);
	}
	
	/**
	 * Send part packet on provided stream
	 */
	@Override
	public void send(OutputStream stream) throws IOException {
		stream.write(data);
	}

	/**
	 * Get application layer data for part packet
	 */
	@Override
	public byte[] getApplicationData() {
		byte[] appData = new byte[partLen];
		
		System.arraycopy(data, 1, appData, 0, partLen);
		return appData;
	} 
}
