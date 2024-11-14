package a;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import model.Message;
import model.User;



public class Server1 {
	
	private static final int PORT = 12345;
	
	private static final List<SocketHandler> clients = Collections.synchronizedList(new ArrayList<>());
	
	public static void main (String args[]) {
		 
		try (ServerSocket serverSocket = new ServerSocket(PORT)){
			System.out.println("Server open on port" + PORT);
			while (true) {
				Socket socket = serverSocket.accept();
				SocketHandler loginHandler = new SocketHandler(socket);
				clients.add(loginHandler);
				new Thread(loginHandler).start();
				
			}
			
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
				
	}
	
	public static void broadcastMessage(Message msg) {
		synchronized (clients) {
			for(SocketHandler client: clients) {
				try {
					client.sendMessage(msg);
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	
		
	}
	public static void removeClient (SocketHandler client) {
		clients.remove(client);
	}
	public static List<User> getUsers() {
		List<User> users = new ArrayList<User>();
		for (SocketHandler sh: clients) {
			User u = sh.getCurrentUser();
			if(u!=null) {
				users.add(u);
			}
		}
		return users;
	}
	public static List<Integer> getUidList() {
		List<Integer> uidList = new ArrayList<Integer>();
		for (SocketHandler sh: clients) {
			Integer u = sh.getCurrentUid();
			if(u!=-1) {
				uidList.add(u);
			}
		}
		return uidList;
	}

	public static List<SocketHandler> getClients() {
		return clients;
	}
	
	
	
	
	
}