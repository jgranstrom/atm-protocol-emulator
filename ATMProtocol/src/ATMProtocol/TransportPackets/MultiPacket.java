/**
 * Contains generic methods for Multipackets aswell as the collection
 * of Multipacket parts.
 */
package ATMProtocol.TransportPackets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

public class MultiPacket extends TransportPacket {
	private static int partDataMaxLen = 9;
	
	MultiPacketInit initPart;
	LinkedList<MultiPacketPart> parts = new LinkedList<MultiPacketPart>();
	int applicationDataLen;
	
	/**
	 * Create a new MultiPacket from data
	 */
	public MultiPacket(byte[] data) throws Exception
	{
		applicationDataLen = data.length;
		
		if(data.length <= 5) // Multipackets should not be used for packetsizes less than 5 since singlepackets should be used in that case
			throw new Exception("Invalid packet usage.");
		else
		{
			// Place 5 first databytes into init packet
			byte[] partData = new byte[5];
			System.arraycopy(data, 0, partData, 0, partData.length);
			initPart = new MultiPacketInit(partData, data.length);
			
			// Place the rest of the data into part packets with max size
			for(int i = partData.length; i < data.length; i += partDataMaxLen)
			{				
				int partLen = applicationDataLen - i;
				if(partLen > partDataMaxLen)
					partLen = partDataMaxLen;
				
				partData = new byte[partLen];
				System.arraycopy(data, i, partData, 0, partData.length);
				parts.add(new MultiPacketPart(partData)); // Add part to collection of multipacket parts
			}
		}
	}
	
	/**
	 * Create multipacket from init packet.
	 * Use for unpacking
	 */
	public MultiPacket(MultiPacketInit initpkt)
	{
		this.applicationDataLen = initpkt.getTotalLen();
		this.initPart = initpkt;
	}
	
	/**
	 * Append part to multipacket
	 * Use for unpacking
	 */
	public void AppendPart(MultiPacketPart partpkt)
	{
		this.parts.add(partpkt);
	}
	
	/**
	 * Read complete multipacket from provided stream
	 */
	public static MultiPacket read(byte header, InputStream stream) throws Exception
	{			
		MultiPacketInit init = MultiPacketInit.read(header, stream); // First read init packet		
		MultiPacket multi = new MultiPacket(init); // Create multipacket from init

		int total = init.getTotalLen(); // Get total length contained in init packet
		int read = 5;
		
		// Read remaining data as part packets
		while(read < total)
		{
			MultiPacketPart part = MultiPacketPart.read(stream);
			multi.AppendPart(part);
			
			read += part.getPartLen();
		}
		
		return multi;
	}
	
	/**
	 * Send complete multipacket in parts on provided stream
	 */
	@Override
	public void send(OutputStream stream) throws IOException {
		initPart.send(stream);
		for(MultiPacketPart part : parts)
		{
			part.send(stream);
		}
	}

	/**
	 * Get all application layer data contained in complete multipacket
	 */
	@Override
	public byte[] getApplicationData() {
		byte[] appData = new byte[applicationDataLen];
		
		// Get initpacket data
		byte[] partData = initPart.getApplicationData();
		System.arraycopy(partData, 0, appData, 0, partData.length);
		
		// Get remaining data in parts
		int offset = partData.length;
		for(MultiPacketPart part : parts)
		{
			partData = part.getApplicationData();
			System.arraycopy(partData, 0, appData, offset, partData.length);
			offset += partData.length;
		}
		
		return appData;
	}
}
