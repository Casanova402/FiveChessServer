package joker.fiveChessServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ChatServer implements Runnable{
	private Socket in;
	private Socket out;
	private InputStreamReader reader;
	private OutputStreamWriter writer;
	
	public ChatServer(Socket in, Socket out){
		this.in = in;
		this.out = out;
	}

	@Override
	public void run() {
		try{
			reader = new InputStreamReader(new DataInputStream(in.getInputStream()), "UTF-8");
			writer = new OutputStreamWriter(new DataOutputStream(out.getOutputStream()), "UTF-8");
			while(true){
				char[] tmp = new char[1000];
				reader.read(tmp);
				writer.write(new String(tmp));
				writer.flush();
			}
		}catch(Exception e){
			try {
				in.close();
				out.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
