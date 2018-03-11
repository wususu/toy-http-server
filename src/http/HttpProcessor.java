package http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;

import org.apache.commons.net.io.SocketInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import http.util.HttpParseUtil;

public class HttpProcessor {

	private ServletProcessor servletProcessor = new ServletProcessor();
	
	private HttpRequest request;
	private HttpResponse response;
	
	public void process(Socket socket){
		SocketInputStream socketInputStream = null;
		OutputStream outputStream = null;
		try{
			socketInputStream = new SocketInputStream(socket, socket.getInputStream());
			outputStream = socket.getOutputStream();
			
			request = new HttpRequest(socketInputStream);
			response = new HttpResponse(outputStream, request);
			
			response.setHeader("Server", "Xhash Server");
			
			parseRequest(socketInputStream, outputStream);
			
			if ( ServletUriManager.exists(request.getUri())){
				servletProcessor.process(request, response);
			}else {
				response.sendStaticResource();
			}
			socket.close();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private void parseRequest(SocketInputStream input, OutputStream output) throws IOException{
		ReadableByteChannel inputChannel = Channels.newChannel(input);
		StringBuffer sf = new StringBuffer(2048);
		ByteBuffer buffer = ByteBuffer.allocate(2048);
		byte[] bytes = new byte[2048];
		int length;
			buffer.clear();
			length =  inputChannel.read(buffer);
			buffer.flip();
			buffer.get(bytes, 0, length);
			for(int i=0; i<length; i++){
				sf.append((char)bytes[i]);
			}
		String rawContent = sf.toString();
		String line = HttpParseUtil.readLine(rawContent);
		String method = HttpParseUtil.parseMethod(line);
		String uri = HttpParseUtil.parseUri(line);
		String protocol = HttpParseUtil.parseProtocol(line);
		String queryString = HttpParseUtil.parseQueryString(uri);
		String cookieStr = HttpParseUtil.parseCookieString(rawContent);
		String sessionId = HttpParseUtil.parseSid(cookieStr);
		Map<String, String> headers = HttpParseUtil.parseHeader(rawContent);
		List<Cookie> cookies = HttpParseUtil.parseCookie(cookieStr);
		uri = uri.replace(queryString, "").replace("?", "").trim();
		request.setMethod(method);
		request.setUri(uri);
		request.setCookieString(cookieStr);
		request.setHasdSessionId(sessionId);
		request.setProtocol(protocol);
		request.setQueryString(queryString);
		request.setRawContent(rawContent);
		request.setHeaders(headers);
		request.setCookies(cookies);
		request.adjustHeaderItems(headers);
	}
}
