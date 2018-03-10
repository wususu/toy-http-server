package http.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import http.HasherService;
import http.Response;

@HasherService(value="/index")
public class IndexServlet implements Servlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3138077541735111816L;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Index";
	}
	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter writer = res.getWriter();
		writer.println(Response.MSG_200_RAW + "<html><head><title>Servlet Response</title></head><body><h2>你好,我是IndexServlet</h2></body></html>");
		writer.flush();
	}
	@Override
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
}
