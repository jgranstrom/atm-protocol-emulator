/**
 * TransportationPacket base class with generic methods 
 */
package ATMProtocol.TransportPackets;

import java.io.IOException;
import java.io.InputStream;

import java.io.OutputStream;
import java.nio.ByteBuffer;

import ATMProtocol.ApplicationPackets.ApplicationPacket;

public abstract class TransportPacket {
	/**
	 * Get TransportationPacket according to included ApplicationPacket
	 */
	public static TransportPacket getTransportPacket(ApplicationPacket pkt) throws Exception
	{
		byte[] data = pkt.make(); // Make ApplicationPacket
		if(data.length <= 5) // SinglePacket <= 10 bytes, with 5 bytes of administrative data included
			return new SinglePacket(data);
		else
			return new MultiPacket(data);
	}
	
	/**
	 * Get unlimited TransportationPacket (Will always give SinglePacket)
	 */
	public static TransportPacket getUnlimTransportPacket(ApplicationPacket pkt)
	{
		return new SinglePacket(pkt.make());
	}
	
	/**
	 * Receive TransportPacket from provided stream
	 */
	public static TransportPacket receive(InputStream stream) throws Exception
	{
		byte[] header = new byte[1];
		stream.read(header); // Read header from stream to distinguish packet types
		
		// Peak header start
		byte hstart = (byte) (header[0] & 0xE0); // Check only interesting bits
		switch(hstart)
		{
			case (byte)0x80: // Multipacket init
			{	
				MultiPacket multi = MultiPacket.read(header[0], stream);
				return multi; 
			}
			case (byte)0xC0: // Multipacket part without preceding init 
			{
				// MultiPacket parts are handled internally in MultiPackets.
				// Should never be received independently here.
				throw new Exception("Got unexpected MultiPacket part without init.");
			}
			case (byte)0xE0: // Singlepacket
			{
				// Get SinglePacket size
				byte[] sizebytes = new byte[4];
				stream.read(sizebytes);
				int size = ByteBuffer.wrap(sizebytes).getInt();
				
				// Read actual SinglePacket data
				byte[] data = new byte[size];
				stream.read(data);
				
				return new SinglePacket(data);
			}
		}
		
		return null;
	}
	
	/**
	 * Sent TransportPacket on provided stream
	 */
	public abstract void send(OutputStream stream) throws IOException;
	
	/**
	 * Get application layer data contained in TransportationPacket
	 */
	public abstract byte[] getApplicationData();
}
