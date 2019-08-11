package com.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class FilterOne implements Filter {
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest)request;
		HttpSession session = req.getSession();
		Cookie[] cookies = req.getCookies();
		if(cookies != null){
			for(Cookie cookie : cookies){
				if(cookie.getName().equals("keyID") && cookie.getValue().equals(session.getAttribute("keyID"))){
					chain.doFilter(request, response);
					return;
				}
			}
			request.getRequestDispatcher("/graduateWork/admin/login.html").forward(request, response);
			return;
		}else{
			request.getRequestDispatcher("/graduateWork/admin/login.html").forward(request, response);
		}

	}
	
	public void init(FilterConfig fConfig) throws ServletException {
		
	}
	
	public void destroy() {

	}

}
