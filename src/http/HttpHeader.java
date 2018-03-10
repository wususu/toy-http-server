package http;

/**
 * 
 * @author janke
 *
 */
public enum HttpHeader {
	
	MSG_404("HTTP/1.1 404 File Not Found\r\n"+
			"Content-Type: text/html\r\n"+
			"Sever: XHasder\r\n" +
			"\r\n"),
	
	MSG_200("HTTP/1.1 200  Success\r\n"+
					"Content-Type: text/html\r\n"+
					"Sever: XHasder\r\n" +
					"\r\n"),
	
	MSG_500("HTTP/1.1 500 Server Error\r\n"+
			"Content-Type: text/html\r\n"+
			"Sever: XHasder\r\n" +
			"\r\n");
	
	public String header;
	
	private HttpHeader(String header){
		this.header = header;
	}
	
	public String get(){
		return this.header;
	}
}
