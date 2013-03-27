/**
 * Provide helper methods for sending and receiving packets
 * on transportation layer.
 */
package ATMProtocol.TransportPackets;

import java.net.Socket;

import ATMProtocol.ApplicationPackets.ApplicationPacket;

public class TransportationHelpers {
	private Socket socket = null;
	
	/**
	 * Create a new instance of the TransportationHelper
	 */
	public TransportationHelpers(Socket socket)
	{
		this.socket = socket;
	}
	
	/**
	 * Send size-limited ApplicationPacket on transportation layer
	 */
	public void sendPacket(ApplicationPacket pkt) throws Exception
	{
		sendPacket(pkt, false);
	}
	
	/**
	 * Send ApplicationPacket on transportation layer
	 */
	public void sendPacket(ApplicationPacket pkt, boolean unlimited) throws Exception
	{
		if(unlimited)
			TransportPacket.getUnlimTransportPacket(pkt).send(socket.getOutputStream());
		else
			TransportPacket.getTransportPacket(pkt).send(socket.getOutputStream());
	}
	
	/**
	 * Receive ApplicationPacket from transportation layer
	 */
	public ApplicationPacket receivePacket() throws Exception
	{
		return ApplicationPacket.unpack(TransportPacket.receive(socket.getInputStream()));
	}
}
