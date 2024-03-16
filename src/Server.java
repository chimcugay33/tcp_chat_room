import java.io.IOException;
import java.net.*;

public class Server {
	private ServerSocket serverSocket;
	
    public Server (ServerSocket serverSocket) {
    	this.serverSocket = serverSocket;
    }
    
    public void startServer() {
    	try {
    		while (!serverSocket.isClosed()) {
        		Socket socket = serverSocket.accept(); // ham nay tra ve socket
        		System.out.println("A new client has connected.");
        		ClientHandler clientHandler = new ClientHandler(socket);
        		Thread thread = new Thread(clientHandler);
        		thread.start();
        	}
    		
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public void closeSocketServer () {
    	try {
    		if (serverSocket!=null) {
    			serverSocket.close();
    		}
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void main (String[] args) {
    	try {
    		ServerSocket serverSocket = new ServerSocket(1500);
        	Server server = new Server(serverSocket);
        	server.startServer();
        	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
