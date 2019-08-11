package service.myservlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.mysql.*;


public class HotSort extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	//根据点击量排序排序
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		PrintWriter out = ResponseSet.setHeader(response);
		String Number =new String(request.getParameter("number").getBytes("ISO8859-1"),"UTF-8");
		int number = Integer.parseInt(Number);
		SqlExecute sql = new SqlExecute();
		
		sql.setDBTable("zhiwuxinzhi");
		sql.setShowRow(number);
		out.write(sql.getTabletoJSONArray("hot", null, "biaoti", "hot", "jijie", "image").toString());
		
	}
}
