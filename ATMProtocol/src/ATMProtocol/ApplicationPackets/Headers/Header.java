/**
 * Base class for ApplicationPacket headers
 */
package ATMProtocol.ApplicationPackets.Headers;

public interface Header {
	/**
	 * Make header into valid byte
	 */
	byte make();

}
