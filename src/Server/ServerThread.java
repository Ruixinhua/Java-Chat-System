package Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import tool.Configuration;

/**
 * The server thread will start automatically
 * @author Liu Dairui
 *
 */
public class ServerThread implements Runnable {
	/**
	 * server socket
	 */
	private ServerSocket server;
	
	/**
	 * Start server
	 */
	private void startServer() {
		try {
			// Create the address of socket
			SocketAddress socketAddress = new InetSocketAddress(Configuration.ip_address, Configuration.port);
			// Create server socket,and bind socket address
			server = new ServerSocket();
			server.bind(socketAddress);
			// The server is running
			ServerGUI.setIsRunning(true); ;

			ServerGUI.addMsg("The server is running at " + Configuration.ip_address + ", and port is " + Configuration.port);
		} catch (IOException e) {
			ServerGUI.addMsg("Fail to start the server");
			//e.printStackTrace();
			ServerGUI.setIsRunning(false);
		}
	}

	/**
	 * The main part of thread
	 */
	@Override
	public void run() {
		startServer();
		// Create a monitor, and observer the request from client
		while (ServerGUI.getIsRunning()) {
			try {
				// Accept the request from the client 
				Socket socket = server.accept();
				// Create a thread that connect to client
				Thread thread = new Thread(new ClientHandler(socket));
				thread.start();
			} catch (IOException e) {

			}
		}
	}

}

