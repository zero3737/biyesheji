package dao.mysql;

import java.sql.*;
import org.apache.commons.dbcp.BasicDataSource;

public class SqlSupport extends ReadDBUser{
	
	private static BasicDataSource ds = null;
	private String[] string = null;
	//���ò���
	public String[] getString() {
		
		return string;
		
	}
	//��ȡ����
	public void setString(String[] string) {
		
		this.string = string;
		
	}
	//��������
	static{
		
		ds = new BasicDataSource();
		
		ds.setDriverClassName(SqlSupport.driver);
		ds.setUsername(SqlSupport.user);
		ds.setPassword(SqlSupport.passwd);
		ds.setUrl(SqlSupport.url);
		
	}
	//��������
	public static Connection getConnection(){

		try {
			return ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	//�ر�����
	public static void closeConnection(Connection con, Statement stmt, ResultSet rs){
		
		try{
			if(rs != null && !rs.isClosed())
				rs.close();
			if(stmt != null && !stmt.isClosed())
				stmt.close();
			if(con != null && !con.isClosed())
				con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	//DMLģ��
	public static void DMLTemplet(String sqlStr, Object...parameter){
		
		Connection con = null;
		PreparedStatement pstmt = null;
		
		try{
			con = SqlSupport.getConnection();
			pstmt = con.prepareStatement(sqlStr);
			for(int i = 0; i < parameter.length; i++)
				pstmt.setObject(i + 1, parameter[i]);
			pstmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SqlSupport.closeConnection(con, pstmt, null);
		}
		
	}
	//DQLģ��
	public static <T>T DQLTemplet(String sqlStr, HandlerImpl<T> h, Object...parameter){
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			con = SqlSupport.getConnection();
			pstmt = con.prepareStatement(sqlStr);
			for(int i = 0; i < parameter.length; i++)
				pstmt.setObject(i + 1, parameter[i]);
			rs = pstmt.executeQuery(sqlStr);
			return h.handle(rs);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SqlSupport.closeConnection(con, pstmt, rs);
		}
		
		return null;
		
	}
	public static <T>T DQLTemplet(String sqlStr, HandlerImpl<T> h, SqlSupport sql){
		
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			con = SqlSupport.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sqlStr);
			return h.handle(rs, sql);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SqlSupport.closeConnection(con, stmt, rs);
		}
		
		return null;
		
	}
	
}
