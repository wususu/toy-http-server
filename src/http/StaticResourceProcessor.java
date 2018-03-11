package http;

import java.io.IOException;

public class StaticResourceProcessor {
	
	public void process(HttpRequest request, HttpResponse response){
		try {
			response.sendStaticResource();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
