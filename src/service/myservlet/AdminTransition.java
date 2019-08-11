package service.myservlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AdminTransition extends HttpServlet {

	private static final long serialVersionUID = 1L;
	//π‹¿Ì‘±µ«¬º
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String username =new String(request.getParameter("username").getBytes("ISO8859-1"),"UTF-8");
		String passwd =new String(request.getParameter("passwd").getBytes("ISO8859-1"),"UTF-8");
		Long Value = (long) (Math.random() * 1000000 + 1);
		Cookie cookie = new Cookie("keyID", Value.toString());
		
		cookie.setMaxAge(60/*√Î*/ * 30);
		if(username.equals("LiangJL") && passwd.equals("abc.123321")){
			response.addCookie(cookie);
			HttpSession session = request.getSession();
			session.setAttribute("keyID", Value.toString());
			Cookie SessionID = new Cookie("JSESSIONID", session.getId());
			SessionID.setMaxAge(60 * 30);
			response.addCookie(SessionID);
			response.sendRedirect("/biyesheji/graduateWork/admin/login_main.html");	
		}else
			response.sendRedirect("/biyesheji/graduateWork/admin/login.html");	
	}

}
