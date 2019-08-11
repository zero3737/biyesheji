package service.myservlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.mysql.SqlExecute;

public class Drop extends HttpServlet {

	private static final long serialVersionUID = 1L;
	//删除数据库表数据
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String id = new String(request.getParameter("id").getBytes("ISO8859-1"),"UTF-8");
		SqlExecute sql = new SqlExecute();
		
		sql.setDBTable("zhiwuxinzhi");
		sql.delete(id);
		
	}
	
}
