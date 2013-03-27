/**
 * Basic logging to console mechanism
 */
package server;

public class Log {
	public static void println(String s)
	{
		System.out.println("[" + Thread.currentThread().getId() + "] " + s);
	}
}
