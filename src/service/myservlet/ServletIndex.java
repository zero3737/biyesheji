package service.myservlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.mysql.*;

public class ServletIndex extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request,response);
		
	}
	//发送请求的数据
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		PrintWriter out = ResponseSet.setHeader(response);
		String referer = request.getHeader("referer");
		SqlExecute sql = new SqlExecute();
		
		sql.setDBTable("zhiwuxinzhi");
		if(referer.indexOf("second.html") != -1){
			String jijie =new String(request.getParameter("name").getBytes("ISO8859-1"),"UTF-8").substring(0, 2);
			out.write(sql.getTabletoJSONArray("jijie", jijie, "neirong", "biaoti", "image").toString());
		}else if(referer.indexOf("third.html") != -1){
			String biaoti =new String(request.getParameter("name").getBytes("ISO8859-1"),"UTF-8");
			out.write(sql.getTabletoJSONArray("biaoti", biaoti, "neirong", "biaoti", "image").toString());
		}else if(referer.indexOf("Transition") != -1 || referer.indexOf("searchResult") != -1){
			String biaoti =new String(request.getParameter("keyword").getBytes("ISO8859-1"),"UTF-8");
			out.write(sql.getTabletoJSONArray("biaoti", biaoti, "neirong", "biaoti", "image", "jijie").toString());
		}
		
	}
	
}
