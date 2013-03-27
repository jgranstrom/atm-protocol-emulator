/**
 * SinglePacket limited/unlimited
 * Use when packet sizes are small enough to be sent on a
 * single packet or when packet size is not limited.
 */
package ATMProtocol.TransportPackets;

import java.io.IOException;

import java.io.OutputStream;
import java.nio.ByteBuffer;

public class SinglePacket extends TransportPacket {
	byte[] data;
	
	/**
	 * Create a SinglePacket from application layer data
	 */
	public SinglePacket(byte[] data)
	{
		byte[] allData = new byte[data.length + 5];
		allData[0] = (byte)0xE0; // Set header to SinglePacket
		System.arraycopy(ByteBuffer.allocate(4).putInt(data.length).array(), 0, allData, 1, 4); // Include packet length in header
		System.arraycopy(data, 0, allData, 5, data.length);
		this.data = allData;
	}
	
	/**
	 * Sent SinglePacket on provided stream
	 */
	@Override
	public void send(OutputStream stream) throws IOException {
		stream.write(data);
	}

	/**
	 * Get application layer data for SinglePacket
	 */
	@Override
	public byte[] getApplicationData() {
		byte[] appData = new byte[data.length - 5]; // Return everything except header
		System.arraycopy(data, 5, appData, 0, appData.length);
		return appData;
	}
}
