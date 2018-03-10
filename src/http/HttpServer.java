package http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author janke
 *
 */
public class HttpServer {
	
	public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";
	private static final String SHUTDOWN_COMMAND = "/SHUTDOWN";
	
	private boolean isShutdown = false;
	
	private ServletProcessor servletProcessor = new ServletProcessor();
	
	public void await(){
		ServerSocket serverSocket = null;
		int port = 8888;
		try{
			serverSocket = new ServerSocket(port);
			InetAddress.getByName("127.0.0.1");
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("XHash 服务器启动成功");
		while(!isShutdown){
			Socket socket = null;
			InputStream input = null;
			OutputStream output = null;
			try{
				socket = serverSocket.accept();
				input = socket.getInputStream();
				output = socket.getOutputStream();

				Request request = new Request(input);
				request.parse();
				
				Response response = new Response(output, request);
				
				if ( ServletUriManager.exists(request.getUri())){
					servletProcessor.process(request, response);
				}else {
					response.sendStaticResource();
				}

				socket.close();
				
				
				if ((isShutdown = SHUTDOWN_COMMAND.equals(request.getUri())) == true) {
					System.out.println("XHash 服务器关闭");
				}
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		HttpServer server = new HttpServer();
		server.await();
	}
}
