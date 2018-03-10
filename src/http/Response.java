package http;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.security.PublicKey;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;

public class Response implements ServletResponse{

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

	@Override
	public void flushBuffer() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getBufferSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		// TODO Auto-generated method stub
		PrintWriter writer = new PrintWriter(output, true); // auto flush
		return writer;
	}

	@Override
	public boolean isCommitted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resetBuffer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBufferSize(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCharacterEncoding(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setContentLength(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setContentType(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLocale(Locale arg0) {
		// TODO Auto-generated method stub
		
	}
}
