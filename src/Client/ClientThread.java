package Client;

import java.io.IOException;
import java.net.Socket;

import tool.*;

/**
 * Manager client thread, and response for the interact with server
 * 
 * @author Liu Dairui
 *
 */
public class ClientThread extends Thread {
	private String username;
	/**
	 * communication socket
	 */
	private Socket socket;

	public ClientThread() {
		username = ClientGUI.username;
	}

	/**
	 * basic input stream
	 */
	// private DataInputStream dis;

	/**
	 * basic output stream
	 */
	// private DataOutputStream dos;

	/**
	 * true is the user has logged in
	 */
	private boolean isLogged;

	/**
	 * connect to the server and login
	 */
	private void login() throws IOException {
		// connect to the server and get the socket Input and Output stream
		socket = new Socket(Configuration.ip_address, Configuration.port);
		// dis = new DataInputStream(socket.getInputStream());
		// dos = new DataOutputStream(socket.getOutputStream());
		// get user name from the GUI and send the message
		String msgLogin = "LOGIN-" + ClientGUI.username;
		Tool.sendMsg(socket, msgLogin);
		// dos.writeUTF(msgLogin);
		// dos.flush();
		// read the message returned from the server
		String response = Tool.recvMsg(socket);
		// if return fail
		if (response.equals("FAIL")) {
			ClientGUI.addMsg("Connect to server failed");
			// connect to server failed and stop the thread
			socket.close();
			return;
		}
		// connect to server successful
		else {
			ClientGUI.addMsg("Connect to server successfull");
			isLogged = true;
			ClientGUI.setButton("STOP", true);
		}
	}

	// disconnect to the server
	public void logout() {
		try {
			Tool.sendMsg(socket, "STOP-" + username);
			// String msgLongout="LOGOUT";
			// dos.writeUTF(msgLogout);
			// dos.flush();
			isLogged = false;

			// update the UI
			ClientGUI.addMsg("Disconnect to the server");
			ClientGUI.setButton("Connect", false);
			// delete the user
			socket.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}

	// send message
	public void broadcast() {
		String msg = "BROADCAST-" + username + "-" + ClientGUI.getTextAreaText();
		String dstUser = "All";

		// add to the message record area
		ClientGUI.addMsg(" I said To " + dstUser + "：" + ClientGUI.getTextAreaText());
		Tool.sendMsg(socket, msg);

	}

	// list command
	public void list() {
		String msg = "LIST-" + username;
		Tool.sendMsg(socket, msg);
	}

	// kick command
	public void kick(String id) {
		String msg = "KICK-" + username + "-" + id;
		Tool.sendMsg(socket, msg);
	}

	// stats command
	public void stats(String id) {
		String msg = "STATS-" + username + "-" + id;
		Tool.sendMsg(socket, msg);
	}

	@Override
	public void run() {
		// connect to the server
		try {
			login();

		} catch (IOException e) {
			ClientGUI.addMsg("Failed to connect to the server");
			// e.printStackTrace();
			return;
		}
		while (isLogged) {
			try {
				String msg = Tool.recvMsg(socket);
				String[] parts = msg.split("-");
				switch (parts[0]) {
				// deal with the message sent from the server
				case "LIST":
					String[] users = new String[parts.length - 1];
					String msgSent = "The list of users: ";
					System.arraycopy(parts, 1, users, 0, parts.length - 1);
					for (int i = 0; i < users.length; i++) {
						msgSent += (users[i] + " ");
					}
					ClientGUI.addMsg(msgSent);
					break;
				// deal with the message sent from server about the new user
				case "LOGIN":
					ClientGUI.addMsg(parts[1] + " connected");
					break;
				case "STOP":
					ClientGUI.addMsg(parts[1] + " disconnected");
					break;
				case "BROADCAST":
					String[] temp = msg.split("-", 3);
					System.out.println(msg);
					ClientGUI.addMsg(parts[1] + " said to all：" + temp[2]);
					break;
				case "STATS":
					String msgStats = "The stats of " + parts[1] + " is {";
					String commands[] = new String[parts.length - 2];
					System.arraycopy(parts, 2, commands, 0, parts.length - 2);

					for (int i = 0; i < commands.length; i++) {
						commands[i] = commands[i].replace("*", "-");
						msgStats += (commands[i] + "  ");
					}
					msgStats += "}";
					ClientGUI.addMsg(msgStats);
					break;
				case "KICK":
					logout();
					break;
				case "FAIL":
					ClientGUI.addMsg("Command Failed: " + parts[1]);
					break;
				default:
					break;
				}
			} catch (IOException e) {
				isLogged = false;
				// logout();
			}
		}
	}

}
