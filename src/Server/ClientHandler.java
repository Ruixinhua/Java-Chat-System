package Server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import tool.Tool;

/**
 * This class is used to interact with client for server.When a client connect
 * to the server, an object of this thread will be created.
 * 
 * @author Liu Dairui
 *
 */
public class ClientHandler implements Runnable {
	private Socket socket;
	private boolean isConnected;
	private String username;
	private ArrayList<String> commands = new ArrayList<String>();
	/**
	 * client map，key -> String：client name； value -> ClientHandler： client thread
	 */
	// the user name can not be the same, use hash map to store the user name and
	// socket
	private static HashMap<String, ClientHandler> clients = new HashMap<String, ClientHandler>();

	public ClientHandler(Socket socket) {
		this.socket = socket;
		isConnected = true;
	}

	@Override
	public void run() {
		while (ServerGUI.getIsRunning() && isConnected) {
			try {
				// read the message that the client sent
				String msg = Tool.recvMsg(socket);
				String[] parts = msg.split("-");
				switch (parts[0]) {
				// deal with message
				case "LOGIN":
					try {
						String loginUsername = parts[1];
						// If the user name has been login, then return fail message, else return
						// success
						if (clients.containsKey(loginUsername)) {
							fail("The user exist");
						} else {
							// add the information of this thread into the clientHandlerMap
							clients.put(loginUsername, this);
							// send success to the new user
							Tool.sendMsg(socket, "SUCCESS");
							// broadcast the information of the new user to the other users.
							String msgLogin = "LOGIN-" + loginUsername;
							broadcastMsg(loginUsername, msgLogin);

							// store the user name
							this.username = loginUsername;
							ServerGUI.addMsg(this.username + " connected");
						}
					} catch (Exception e) {
						fail("Connect failed");
					}
					break;
				case "STOP":
					stopClient();
					break;
				case "LIST":
					// deal with the list command
					try {
						commands.add("{LIST}");
						// read the list of user from hash map
						StringBuffer msgUserList = new StringBuffer();
						msgUserList.append("LIST-");
						for (String username : clients.keySet()) {
							msgUserList.append(username + "-");
						}
						// send the user list to the new user
						Tool.sendMsg(socket, msgUserList.toString());
						ServerGUI.addMsg(parts[1] + " ask for list");
					} catch (Exception e) {
						fail("Fail to execute list command");
					}
					break;
				case "BROADCAST":
					// deal with broadcast command
					try {
						commands.add("{BROADCAST * {" + parts[2] + "}}");
						String temp[] = msg.split("-",3);
						String msgTalkToAll = "BROADCAST-" + parts[1] + "-" + temp[2];
						broadcastMsg(username, msgTalkToAll);
						ServerGUI.addMsg(parts[1] + " broadcast: " + temp[2]);
					} catch (Exception e) {
						fail("Fail to execute broadcast command");
					}
					break;
				case "KICK":
					// deal with kick command
					try {
						if (parts[1].equals(parts[2])) {
							fail("Can not kick yourself");
						} else {
							ClientHandler clientHandler = clients.get(parts[2]);
							if (clientHandler != null) {
								commands.add("{KICK * " + parts[2] + "}");
								clientHandler.isConnected = false;
								String msgKick = "KICK-" + username + "-" + parts[2];
								Tool.sendMsg(clientHandler.socket, msgKick);
							} else {
								fail("The user doesn't exist");
							}
						}
					} catch (Exception e) {
						fail("Fail to execute kick command");
					}
					break;
				case "STATS":
					// deal with stats command
					try {
						commands.add("{STATS * " + parts[2] + "}");
						ClientHandler clientStats = clients.get(parts[2]);
						if (clientStats != null) {
							String msgStats = "STATS-" + parts[2] + "-";
							for (String command : clientStats.commands) {
								msgStats += (command + "-");
							}
							Tool.sendMsg(socket, msgStats);
							ServerGUI.addMsg(parts[1] + " ask stats of " + parts[2]);
						} else {
							fail("The user doesn't exist");
						}
					} catch (Exception e) {
						fail("Fail to execute stats command");
					}
				default:
					break;
				}
			} catch (IOException e) {
				stopClient();
				//e.printStackTrace();
			}
		}
	}
	
	private void stopClient() {
		// deal with the stop command
		try {
			clients.remove(username);
			String msgLogout = "STOP-" + username;
			// broadcast the message to the others users
			broadcastMsg(username, msgLogout);
			// close the connection between user and server
			isConnected = false;
			socket.close();
			ServerGUI.addMsg(username + " disconnected");
		} catch (Exception e) {
			fail("Fail to execute stop command");
		}
	}

	/**
	 * If any commands fail, the method will be called
	 * 
	 * @throws IOException
	 */
	private void fail(String msg){
		Tool.sendMsg(socket, "FAIL-" + msg);
		ServerGUI.addMsg("Command Fail: " + msg);
	}

	/**
	 * broadcast the message which received from a user to the other users except
	 * the user self
	 * 
	 * @param fromUsername the user who sent message
	 * @param msg          the message that need to broadcast
	 */
	private void broadcastMsg(String fromUsername, String msg) throws IOException {
		for (String toUserName : clients.keySet()) {
			if (!fromUsername.equals(toUserName)) {
				Tool.sendMsg(clients.get(toUserName).socket, msg);
			}
		}
	}
}
