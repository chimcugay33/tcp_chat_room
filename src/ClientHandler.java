import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;


public class ClientHandler implements Runnable{

	public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
	private Socket socket;
	private BufferedReader bufferedReader;
	private BufferedWriter bufferedWriter;
	private String clientUsername;
	
	public ClientHandler(Socket socket) {
		this.socket = socket;
		try {
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // doc
			this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); // gui
			this.clientUsername = bufferedReader.readLine();
			clientHandlers.add(this);
			broadcastMessage("SERVER: " + clientUsername + " has joined the chat!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			closeEverything(socket, bufferedReader, bufferedWriter);
		}
	}	
	
	@Override
	public void run() {
		String messageFromClient;
		
		while (socket.isConnected()) {
			try {
				messageFromClient = bufferedReader.readLine();
				broadcastMessage(messageFromClient);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				closeEverything(socket, bufferedReader, bufferedWriter);
				break;
			}
		}
		
	}
	
	public void broadcastMessage (String message) {
		for (ClientHandler i : clientHandlers) {
			try {
				if (!i.clientUsername.equals(this.clientUsername)) {
					i.bufferedWriter.write("\n" + message + "\n" + i.clientUsername + ": ");
					i.bufferedWriter.newLine();
					i.bufferedWriter.flush();// bang viec an "Enter"
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				closeEverything(socket, bufferedReader, bufferedWriter);
				break;
			}
		}
	}

	public void removeClientHandler () {
		clientHandlers.remove(this);
		broadcastMessage("SERVER: " + clientUsername + " has left the chat.");
	}
	
	public void closeEverything (Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
		removeClientHandler();
		try {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (bufferedWriter != null) {
				bufferedWriter.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
