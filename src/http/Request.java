package http;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;

/**
 * 
 * @author janke
 *
 */
public class Request {
 
	private InputStream input;
	private String rawContent;
	private String uri;
	
	public Request(InputStream input) {
		// TODO Auto-generated constructor stub
		this.input = input;
	}
	
	public String getUri(){
		return this.uri;
	}
	
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
}
