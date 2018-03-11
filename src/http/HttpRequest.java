package http;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.Principal;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.common.collect.Maps;

import http.util.HttpParseUtil;
import http.util.ParameterMap;

import static com.google.common.base.Preconditions.*;

/**
 * 
 * @author janke
 *
 */
public class HttpRequest implements HttpServletRequest{
 
	protected InputStream input;

	protected String rawContent;
	protected String uri;
	protected String queryString;
	protected String cookieString;
	protected String method;
	protected String protocol;
	protected String hasdSessionId;
	protected String contentType;
	private boolean prased = false;
	private ParameterMap result;
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	protected Map<String, String> headers = Maps.newHashMap();
	protected List<Cookie> cookies = new LinkedList<>();
	protected ParameterMap parameters = null;
	
	public static final String CONTENT_TYPE="Content-Type";
	public static final String CONTENT_LENGTH="Content-Length";
	public static final String ACCEPT_LANGUAGE="Accept-Language";
	public static final String ACCEPT_ENCODING="Accept-Encoding";

	
	public HttpRequest(InputStream input) {
		// TODO Auto-generated constructor stub
		this.input = input;
	}
	
	public void adjustHeaderItems(Map<String, String> headers){
		setContentType(checkNotNull(headers.get(CONTENT_TYPE)));
	}
	
	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
		
	}
	
	public String getUri(){
		return this.uri;
	}
	
	public String getRawContent() {
		return rawContent;
	}

	public void setRawContent(String rawContent) {
		this.rawContent = rawContent;
	}

	public String getCookieString() {
		return cookieString;
	}

	public void setCookieString(String cookieString) {
		this.cookieString = cookieString;
	}

	public String getHasdSessionId() {
		return hasdSessionId;
	}

	public void setHasdSessionId(String hasdSessionId) {
		this.hasdSessionId = hasdSessionId;
	}

	public ParameterMap getParameters() {
		return parameters;
	}

	public void setParameters(ParameterMap parameters) {
		this.parameters = parameters;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public void setCookies(List<Cookie> cookies) {
		this.cookies = cookies;
	}
	
	@Deprecated
	public void parse() throws IOException{
		ReadableByteChannel inputChannel = Channels.newChannel(this.input);
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
			
		this.rawContent = sf.toString();
		this.uri = this.parseUri(sf.toString());
	}
	
	@Deprecated
	private String parseUri(String request){
		int index1, index2;
		index1 = request.indexOf(' ');
		if (index1 != -1) {
			index2 = request.indexOf(' ', index1+ 1);
			if (index2 > index1) {
				return request.substring(index1+1, index2);
			}
		}
		return null;
	}

	@Override
	public Object getAttribute(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCharacterEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getContentLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return contentType;
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return (ServletInputStream) input;
	}

	@Override
	public String getLocalAddr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLocalName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLocalPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Locale getLocale() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getLocales() {
		// TODO Auto-generated method stub
		return null;
	}

	private void parseParameters(){
		// GET
		if (prased) {
			return ;
		}
		if (result == null) {
			result = new ParameterMap();
		}
		result.setLocked(false);
		String encoding = getCharacterEncoding();
		if (encoding == null) {
			encoding = "ISO-8859-1";
		}
		HttpParseUtil.parseParameters(this.queryString, result, encoding);
		// POST
		String cType = getContentType();
		if (cType == null) 
			cType = "";
		int semicolon = cType.indexOf(';');
		if (semicolon >= 0) {
			cType = cType.substring(0, semicolon);
		}
		cType = cType.trim();
		if ("POST".equals(method) 
				&& "application/x-www-form-urlencoded".equals(cType)) {
				int indexOfBlankLine =  rawContent.lastIndexOf("\n");
				String dataStr = rawContent.substring(indexOfBlankLine, rawContent.length()).trim();
				HttpParseUtil.parseParameters(dataStr, result, encoding);
		}
		result.setLocked(true);
		parameters = result;
		prased = true;
	}
	
	@Override
	public String getParameter(String name) {
		// TODO Auto-generated method stub
		parseParameters();
		return (String) parameters.get(name);
	}

	@Override
	public Map getParameterMap() {
		// TODO Auto-generated method stub
		parseParameters();
		return parameters;
	}

	@Override
	public Enumeration getParameterNames() {
		// TODO Auto-generated method stub
		parseParameters();
		return null;
	}

	@Override
	public String[] getParameterValues(String arg0) {
		// TODO Auto-generated method stub
		if (!prased){
			parseParameters();
		}
		return (String[]) parameters.values().toArray();
	}

	@Override
	public String getProtocol() {
		// TODO Auto-generated method stub
		return protocol;
	}

	@Override
	public BufferedReader getReader() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRealPath(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteAddr() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRemoteHost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRemotePort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getScheme() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getServerPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isSecure() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeAttribute(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAuthType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cookie[] getCookies() {
		// TODO Auto-generated method stub
		return (Cookie[]) cookies.toArray();
	}

	@Override
	public long getDateHeader(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getHeader(String name) {
		// TODO Auto-generated method stub
		return this.headers.get(name);
	}

	@Override
	public Enumeration getHeaders(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enumeration getHeaderNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getIntHeader(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getMethod() {
		// TODO Auto-generated method stub
		return method;
	}

	@Override
	public String getPathInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPathTranslated() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getContextPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getQueryString() {
		// TODO Auto-generated method stub
		return queryString;
	}

	@Override
	public String getRemoteUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUserInRole(String role) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Principal getUserPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestedSessionId() {
		// TODO Auto-generated method stub
		return hasdSessionId;
	}

	@Override
	public String getRequestURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuffer getRequestURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getServletPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpSession getSession(boolean create) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpSession getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		// TODO Auto-generated method stub
		return false;
	}
}
