package Server;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import tool.*;

/**
 * The GUI of the server side
 * @author Liu Dairui
 *
 */
public class ServerGUI {

	/**
	 * it will be true if the server is running
	 */
	private static boolean isRunning;

	private JFrame serverWindow;
	private static JTextArea textAreaRecord;// record text

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGUI window = new ServerGUI();
					window.serverWindow.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ServerGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		serverWindow = new JFrame();
		serverWindow.setResizable(false);
		serverWindow.setTitle("Server");
		serverWindow.setBounds(Configuration.x, Configuration.y, Configuration.width, Configuration.height);
		serverWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// It is a empty layout
		serverWindow.getContentPane().setLayout(null);

		// add scroll bar
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(Configuration.scrollPane.x, Configuration.scrollPane.y, Configuration.scrollPane.width,
				Configuration.scrollPane.height);
		serverWindow.getContentPane().add(scrollPane);

		textAreaRecord = new JTextArea();
		textAreaRecord.setEditable(false);
		scrollPane.setViewportView(textAreaRecord);

		Thread serverThread = new Thread(new ServerThread());
		serverThread.start();

	}

	/**
	 * Add message in the record area
	 * 
	 * @param msg the message that need to be added,it will add local time before
	 *            the message
	 */
	public static void addMsg(String msg) {
		Tool.addMsgRec(textAreaRecord, msg);
	}

	/**
	 * 
	 * @return if the server is running, return true;else, return false
	 */
	public static boolean getIsRunning() {
		return isRunning;
	}

	/**
	 * set the state of the server
	 * 
	 * @param running when server is running, set the isRunning to true, else set to
	 *                false
	 */
	public static void setIsRunning(boolean running) {
		isRunning = running;
	}
}