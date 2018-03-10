package http;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class ServletProcessor {
	
	public void process(Request request, Response response) {
		String uri = request.getUri();

		Servlet servlet = ServletUriManager.getServletByUri(uri);
		try {
			servlet.service((ServletRequest)new RequestFacade(request), (ServletResponse)new ResponseFacade(response));
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
