package joker.fiveChessServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

class HandleASession implements Runnable{

	private Socket player1;
	private Socket player2;
	
	
	private DataInputStream fromPlayer1;
	private DataOutputStream toPlayer1;
	private DataInputStream fromPlayer2;
	private DataOutputStream toPlayer2;
	
	
	public HandleASession(Socket player1,Socket player2){
		this.player1 = player1;
		this.player2 = player2;
	}
	@Override
	public void run() {
		try {
			fromPlayer1 = new DataInputStream(player1.getInputStream());
			fromPlayer2 = new DataInputStream(player2.getInputStream());
			toPlayer1 = new DataOutputStream(player1.getOutputStream());
			toPlayer2 = new DataOutputStream(player2.getOutputStream());
			
			//write signal to tell player1 start
			toPlayer1.writeInt(1);
			toPlayer1.flush();
			
			while(true){
				//receive from player1
				int x = fromPlayer1.readInt();
				if(x == -1){
					toPlayer2.writeInt(-1);
					toPlayer2.flush();
					fromPlayer1.close();
					fromPlayer2.close();
					toPlayer1.close();
					toPlayer2.close();
					player1.close();
					player2.close();
					break;
				}else{
					int y = fromPlayer1.readInt();
					sendMove(toPlayer2,x,y);
					x=fromPlayer2.readInt();
					if(x == -1){
						toPlayer1.writeInt(-1);
						toPlayer1.flush();
						fromPlayer1.close();
						fromPlayer2.close();
						toPlayer1.close();
						toPlayer2.close();
						player1.close();
						player2.close();
						break;
					}
					y=fromPlayer2.readInt();
					sendMove(toPlayer1,x,y);
				}
			}
			
		} catch (IOException e) {
			try {
				fromPlayer1.close();
				fromPlayer2.close();
				toPlayer1.close();
				toPlayer2.close();
				player1.close();
				player2.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private void sendMove(DataOutputStream out, int x, int y) throws IOException{
		out.writeInt(x);
		out.writeInt(y);
		out.flush();
	}
	
}