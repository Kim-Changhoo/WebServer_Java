import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ServerSocket listener = new ServerSocket(8080);
		StringBuffer lines = new StringBuffer();
		//StringBuilder lines = new StringBuilder();
		
		try{
			System.out.println("Http Server started at 8080 port");
			while(true){
				Socket socket = listener.accept();
				try{
					System.out.printf("New Client Connect! Connected IP : %s, Port : %d\n", socket.getInetAddress(), socket.getPort());
					//socket.setSoTimeout(1000);
	
					InputStream in = socket.getInputStream();
					BufferedReader br = new BufferedReader(new InputStreamReader(in,"euc-kr"));
					
					String inputLine ;
					while((inputLine = br.readLine()) != null)
						lines.append(inputLine+"\n".toString());
					System.out.println(lines.toString());
					
					// 클라이언트 전송 코드 //
					OutputStream out = socket.getOutputStream();
					OutputStreamWriter osw = new OutputStreamWriter(out);
					BufferedWriter bw = new BufferedWriter(osw);
					
					String body = "Hello World\r\n";
					String protocol = "HTTP/1.1 200 OK \r\n"; 
					String server = "Server: myWebserber \r\n";
					String cnt_type = "Content-type: text/html \r\n";
					String end = "\r\n";
					/*
					out.write(protocol.getBytes());
					out.write(server.getBytes());
					out.write(cnt_type.getBytes());
					out.write(end.getBytes());
					out.flush();
					out.write(body.getBytes());
					out.flush();*/
					//out.close();
					
					bw.write(protocol);
					bw.write(server);
					bw.write(cnt_type);
					bw.write(end);
					bw.flush();
					bw.write(body);
					bw.flush();
					
					//bw.write("Content-Type: text/html;charset=utf-8/\r\n");
					//bw.write(("Content-Length: " +body.length() +"\r\n"));
					//bw.write(body+"\n");
					//bw.flush();
					
					///////////////////
					bw.close();
					//out.close
					br.close();
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					socket.close();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			listener.close();
		}
	}
}


/*
byte[] body = "Hello World".getBytes();

dos.writeBytes("HTTP/1.1 200 OK \r\n");
dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
dos.writeBytes("Content-Length: " +body.length +"\r\n");
dos.writeBytes("\r\n");

dos.write(body, 0, body.length);
dos.writeBytes("\r\n");
//dos.writeBytes("keykeykey");
dos.flush();
br.close();*/