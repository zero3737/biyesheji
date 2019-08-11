package service.myservlet;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dao.mysql.SqlExecute;

public class ClickNumber extends HttpServlet {

	private static final long serialVersionUID = 1L;
	//增加点击量
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String biaoti =new String(request.getParameter("name").getBytes("ISO8859-1"),"UTF-8");
		SqlExecute sql = new SqlExecute();
		
		sql.setDBTable("zhiwuxinzhi");
		String[] parameter = {"id", "hot"};
		List list = sql.getTabletoList(parameter, "biaoti", biaoti);
		sql.update(list.get(0), list.get(1));
		
	}
	
}
