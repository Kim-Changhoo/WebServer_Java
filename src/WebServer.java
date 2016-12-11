/*///////////////////// Issue 사항 /////////////////////////
line 60-63 중 while 탈출에 
while( (lines=in.readLine()) != null){
	sb.append(lines);
	System.out.println(lines);
}//

////////////////////////////////////////////////////////*/
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.json.simple.*;

public class WebServer{

	public static void main(String[] args) throws IOException {
		Executor executor = Executors.newFixedThreadPool(1);
		ServerSocket ss = new ServerSocket(8080);
		System.out.println("Http Server started at 8080 port");
		try{
			while(true)
				executor.execute(new TinyHttpConnection(ss.accept()));
		}catch(IOException e){}
		finally{
			ss.close();
		}
	}
}

class TinyHttpConnection implements Runnable{
	Socket client ;
	String response = null ;
	String lines =null;
	StringBuilder sb = new StringBuilder();
	//String msg = "{Device:[{Led:[{room1:On,room2:Off,room3:On,room4:Off,livingroom:On}],Curtain:Draw,Doorlock:Locked}]}";
	
	public JSONObject JsonCreate() {

		// 최종 완성될 JSONObject 선언 (전체)
		JSONObject jsonobject = new JSONObject();
		// Device의 JSON 어레이
		JSONArray deviceList = new JSONArray();
		// Led의 JSON 어레이
		JSONArray ledList = new JSONArray();
		// Led의 정보가 들어갈 JSON Object
		JSONObject ledInfo = new JSONObject();
		// Device의 정보가 들어갈 JSON Object
		JSONObject deviceInfo = new JSONObject();
		// device정보 입력

		try {
			ledInfo.put("room1", "On");
			ledInfo.put("room2", "Off");
			ledInfo.put("room3", "On");
			ledInfo.put("room4", "Off");
			ledList.add(ledInfo);
			deviceInfo.put("Curtain", "Draw");
			deviceInfo.put("Doorlock", "Locked");
			deviceInfo.put("Led", ledList);
			deviceList.add(deviceInfo);
			jsonobject.put("Device", deviceList);
		} catch (Exception e) {
		}
		return jsonobject ;
	}
	
	TinyHttpConnection(Socket client) throws SocketException{
		this.client = client;
	}
	
	public void run(){
		try{
			System.out.printf("New Client Connect! Connected IP : %s, Port : %d\n", client.getInetAddress(), client.getPort());
			BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			JSONObject sendData = JsonCreate(); 
			String msg = sendData.toString();
			System.out.println("to Send : " +msg);
			/*lines = in.readLine();
			sb.append(lines);
			String request = sb.toString();*/
			
			String request = in.readLine();
			System.out.println("Request: " +request);
			
			OutputStream out = client.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(out);
			BufferedWriter bw = new BufferedWriter(osw);
			
			String protocol = "HTTP/1.1 200 OK \r\n"; 
			String server = "Server: myWebserber \r\n";
			String cnt_type = "Content-type: text/html \r\n";
			String end = "\r\n";
			
			bw.write(protocol);
			bw.write(server);
			bw.write(cnt_type);
			bw.write(end);
			bw.write(msg);
			bw.flush();
			bw.close();
			
		}catch(IOException e){
			System.out.println("I/O error " +e);
		}finally{
			try{
				System.out.println("write success");
				client.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	
}
	

/*
public class ThreadServer implements Runnable{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Runnable target = new ThreadServer();
		Thread serverThread = new Thread(target);
		serverThread.start();
	}
	
	@Override
	public void run(){
		ServerSocket serverSocket = null;
		try{
			serverSocket = new ServerSocket(1555);
			System.out.println("------ Server is listening on port 1555------");
			//DataOutputStream out;
			String msgFromClient="aa";
			
			while(true){
				Socket socket = serverSocket.accept();
				
				System.out.println("클라이언트로부터 요청 들어옴 " + socket.getInetAddress());
				
				InputStream in = socket.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
				
				StringBuffer sb = new StringBuffer();
				String inputLine ;
				while( (inputLine = br.readLine()) != null)
					sb.append(inputLine+"\n");
				msgFromClient = sb.toString();
				System.out.println(msgFromClient);
				
				out=new DataOutputStream(socket.getOutputStream());
				out.writeUTF(msgFromClient +" : " +new java.util.Date().toString());
				
				OutputStream out = socket.getOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(out);
				BufferedWriter bw = new BufferedWriter(osw);
				
				String body = "Hello World\r\n";
				String protocol = "HTTP/1.1 200 OK \r\n"; 
				String server = "Server: myWebserber \r\n";
				String cnt_type = "Content-type: text/html \r\n";
				String end = "\r\n";
				
				bw.write(protocol);
				bw.write(server);
				bw.write(cnt_type);
				bw.write(end);
				bw.flush();
				bw.write(body);
				bw.flush();
				
				in.close();
				out.close();
				socket.close();
			}
		}catch (IOException e){
			System.err.println();
		}finally{
			if(serverSocket != null)
				try{ serverSocket.close(); }
				catch(IOException e){}
		}
	}
}
*/