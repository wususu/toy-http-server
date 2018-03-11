package http;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.security.PublicKey;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Maps;

public class HttpResponse implements HttpServletResponse{

	protected byte[] buffer = new byte[BUFFER_SIZE];
	protected int bufferCount = 0;
	private static final int BUFFER_SIZE = 2048;
	
	protected boolean committed = false;
	protected int contentCount = 0;
	protected int contentLength = -1;
	protected String contentType = null;
	protected String encoding = null;
	
	private OutputStream output;
	private HttpRequest request;
	private byte[] msgOkByte = MSG_200_RAW.getBytes();
	private int b = msgOkByte.length;
	
	private Map<String, String> headers = Maps.newHashMap();
	
	public static String MSG_200_RAW = HttpHeader.MSG_200.get();
	public static String MSG_404_RAW = HttpHeader.MSG_404.get() +
			"<html><head>404 Page</head><body><h2>404 Resource can not found</h2></body></html>";
	public static String MSG_500_RAW = HttpHeader.MSG_500.get() + 
			"<html><head>500 Page</head><body><h2>500 Hash Server Error</h2></body></html>";
	
	public HttpResponse(OutputStream output, HttpRequest request) {
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
			System.out.println(request.getUri());
			File file = new File(HttpConnector.WEB_ROOT, request.getUri());
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
		if (encoding == null)
		      return "UTF-8";
		    else
		      return encoding;
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
		PrintWriter writer = new PrintWriter(output, true);
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

	@Override
	public void addCookie(Cookie cookie) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean containsHeader(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String encodeURL(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeRedirectURL(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeUrl(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String encodeRedirectUrl(String url) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendError(int sc, String msg) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendError(int sc) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendRedirect(String location) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDateHeader(String name, long date) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addDateHeader(String name, long date) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHeader(String name, String value) {
		// TODO Auto-generated method stub
		this.headers = Maps.newHashMap();
		addHeader(name, value);
	}

	@Override
	public void addHeader(String name, String value) {
		// TODO Auto-generated method stub
		if (this.headers == null) {
			this.headers = Maps.newHashMap();
		}
		this.headers.put(name, value);
	}

	@Override
	public void setIntHeader(String name, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addIntHeader(String name, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStatus(int sc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setStatus(int sc, String sm) {
		// TODO Auto-generated method stub
		
	}
}
