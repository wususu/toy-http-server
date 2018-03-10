package http;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.security.PublicKey;

public class Response {

	private static final int BUFFER_SIZE = 2048;
	private OutputStream output;
	private Request request;
	private byte[] msgOkByte = MSG_200_RAW.getBytes();
	private int b = msgOkByte.length;
	
	public static String MSG_200_RAW = HttpHeader.MSG_200.get();
	public static String MSG_404_RAW = HttpHeader.MSG_404.get() +
			"<html><head>404 Page</head><body><h2>404 Resource can not found</h2></body></html>";
	public static String MSG_500_RAW = HttpHeader.MSG_500.get() + 
			"<html><head>500 Page</head><body><h2>500 Hash Server Error</h2></body></html>";
	
	public Response(OutputStream output, Request request) {
		// TODO Auto-generated constructor stub
		this.output = output;
		this.request = request;
	}
	
	public void sendStaticResource() throws IOException{
		byte[] bytes;
		WritableByteChannel wcl = null;
		FileInputStream fis = null;
		FileChannel fcl = null;
		try{
			File file = new File(HttpServer.WEB_ROOT, request.getUri());
			wcl = Channels.newChannel(output);
			if (file.exists()) {
				ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
				fis = new FileInputStream(file);
				fcl = fis.getChannel();
				while(true){
					buffer.clear();
					int r = fcl.read(buffer);
					if (r==-1) {
						break;
					}
					bytes = new byte[r];
					buffer.flip();
					buffer.get(bytes, 0, r);
					
					ByteBuffer sendBuffer = ByteBuffer.allocate(r + b);
					sendBuffer.put(msgOkByte);
					sendBuffer.put(bytes);
					sendBuffer.flip();
					wcl.write(sendBuffer);
				}
				
			}else {
				wcl.write(ByteBuffer.wrap(MSG_404_RAW.getBytes()));
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			wcl.write(ByteBuffer.wrap(MSG_500_RAW.getBytes()));
		}finally {
			if (wcl!=null) {
				wcl.close();
			}
			if (fis != null) {
				fis.close();
			}
			if (fcl != null) {
				fcl.close();
			}
		}
	}
}
