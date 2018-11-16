package tool;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * Here is the configuration of the system,including the IP address and port,
 * also the design of the GUI;
 * 
 * @author Liu Dairui
 *
 */
public class Configuration {

	// IP address and port
	public static String ip_address = "127.0.0.1";
	public static int port = 8000;

	// The location and size of the windows
	public static int x = 100;
	public static int y = 100;
	public static int width = 400;
	public static int height = 400;

	// The size of the button
	public static int btwidth = 50;
	public static int btheight = 20;

	// The server part
	public static Rectangle scrollPane = new Rectangle(20, 20, 360, 320);

	// The client part
	public static Rectangle idText = new Rectangle(80, 15, 100, 18);
	public static Rectangle idInput = new Rectangle(130, 13, 100, 24);
	public static Rectangle btConnect = new Rectangle(260, 15, 80, 20);
	public static Rectangle msgRecord = new Rectangle(20, 50, 360, 150);
	public static Rectangle msgSend = new Rectangle(20, 250, 360, 100);
	public static Point start = new Point(10, 10);
	public static Point btList = new Point(20, 215);
	public static Rectangle idChecked = new Rectangle(btList.x + 160, btList.y + 1, 100, 18);
	public static Point btKick = new Point(btList.x + 260, btList.y);
	public static Point btStats = new Point(btList.x + 310, btList.y);
	public static Point btSend = new Point(280, 350);
}
