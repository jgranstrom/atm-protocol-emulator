/**
 * Contains all Opcodes as static bytes
 */
package ATMProtocol;

public class Opcodes {
	public static final byte AUTH = 32;
	public static final byte DEAUTH = 31;
	public static final byte GETLANG = 1;
	public static final byte LANGPROVIDE = 2;
	public static final byte GETMOTD = 3;
	public static final byte MOTDPROVIDE = 4;
	public static final byte GETBALANCE = 5;
	public static final byte BALANCEPROVIDE = 6;
	public static final byte WITHDRAWAL = 7;
	public static final byte DEPOSIT = 8;
}
