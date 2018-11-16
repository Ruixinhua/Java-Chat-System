package tool;

import java.io.DataOutputStream;
import java.awt.Point;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 * It is a tool to manager the system
 * @author Liu Dairui
 *
 */
public class Tool {
	/**
	 * send message through socket
	 * @param s the socket 
	 * @param msg the message that need to send
	 */
	public static void sendMsg(Socket s, String msg) {
		try {
			//character stream
			DataOutputStream dos = new DataOutputStream(s.getOutputStream());
			dos.writeUTF(msg);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * receive the stream, if TCP is closed, then return null
	 * @param s socket
	 * @return the message that received
	 * @throws IOException
	 */
	public static String recvMsg(Socket s) throws IOException {
		String msg = null;
		//read the data from input stream
		DataInputStream dis = new DataInputStream(s.getInputStream());
		msg =  dis.readUTF();
		return msg;

	}

	/**
	 * get current time
	 * @return the format of the date
	 */
	public static String getTimeStr() {
		SimpleDateFormat fm = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

		return fm.format(new Date());

	}

	/**
	 * add message to the text area and roll to the last line
	 * @param textArea the text area that need to add 
	 * @param msg the message
	 */
	public static void addMsgRec(JTextArea textArea, String msg) {
		textArea.append(Tool.getTimeStr() + ": \n" + msg + "\n");
		textArea.setCaretPosition(textArea.getText().length());
	}
	
	/**
	 * add button on the certain position
	 * @param frame the frame where the button is
	 * @param text the text on the button
	 * @param position the position of button, it is a Point object
	 * @param width the width of button
	 * @param height the height of button
	 * @return current button
	 */
	public static JButton addButton(JFrame frame, String text, Point position, int width, int height) {
		//connect button
		JButton button = new JButton(text);
		button.setBounds(position.x, position.y, width, height);
		button.setEnabled(false);
		frame.getContentPane().add(button);
		return button;
	}
}
