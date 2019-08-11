package service.myservlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

public class ResponseSet {
	
	public static PrintWriter setHeader(HttpServletResponse response) throws IOException{
		
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		return out;
		
	}
	
}
