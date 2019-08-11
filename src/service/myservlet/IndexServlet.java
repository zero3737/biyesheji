package service.myservlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.mysql.SqlExecute;

public class IndexServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		doPost(request, response);
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = ResponseSet.setHeader(response);
		SqlExecute sql = new SqlExecute();
		
		sql.setDBTable("zhiwuxinzhi");
		out.write(sql.getTabletoJSONArray(null, null, "neirong", "biaoti", "jijie", "image").toString());
		
	}

}
