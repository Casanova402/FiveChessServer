package joker.fiveChessServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	public static final int BLACK = 1;
	public static final int WHITE = 2;
	
	public static int PORT = 55535;
	public static int CHAT_PORT = 55536;

	public static void main(String[] args) {
		new Server();
	}
	
	@SuppressWarnings("resource")
	public Server(){
		try {
			ServerSocket server = new ServerSocket(PORT);
			ServerSocket chatServer = new ServerSocket(CHAT_PORT);
			int sessionNo = 1;
			while(true){
				System.out.println("wait for players to join seesion "+sessionNo);
				//wait for player 1
				Socket player1 = server.accept();
				Socket chat1 = chatServer.accept();
				player1.setSoTimeout(60*5*1000);
				chat1.setSoTimeout(60*5*1000);
				System.out.println("player1 join the session");
				//notify that the player plays in black
				DataOutputStream toPlayer1 = new DataOutputStream(player1.getOutputStream());
//				new DataOutputStream(player1.getOutputStream()).writeInt(BLACK);
				toPlayer1.writeInt(BLACK);
				toPlayer1.flush();
				//wait for player 2
				Socket player2 = server.accept();
				Socket chat2 = chatServer.accept();
				player2.setSoTimeout(60*5*1000);
				chat2.setSoTimeout(60*5*1000);
				System.out.println("player2 join the session");
				//notify that the player plays in white
				DataOutputStream toPlayer2 = new DataOutputStream(player2.getOutputStream());
//				new DataOutputStream(player2.getOutputStream()).writeInt(WHITE);
				toPlayer2.writeInt(WHITE);
				toPlayer2.flush();
				
				System.out.println("Session "+sessionNo+" start.");
				sessionNo++;
				
				Thread session = new Thread(new HandleASession(player1, player2));
				Thread chatSession1 = new Thread(new ChatServer(chat1, chat2));
				Thread chatSession2 = new Thread(new ChatServer(chat2, chat1));
				
				session.start();
				chatSession1.start();
				chatSession2.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
