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
public class HttpConnector implements Runnable{
	
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
				new HttpProcessor().process(socket);

			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		await();
	}
	
	public void start(){
		Thread thread = new Thread(this);
		thread.start();
	}
}
