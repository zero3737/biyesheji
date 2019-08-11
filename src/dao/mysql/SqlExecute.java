package dao.mysql;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
//定义处理器接口
interface HandlerImpl<T> {
	
	T handle(ResultSet rs, SqlSupport...parameter) throws Exception;
	
}

public class SqlExecute{
	
	private static String dbTable = null;
	private static int showRow = -1;
	//获取表列数处理器
	private static class GetTableColHandler implements HandlerImpl<Integer>{

		public Integer handle(ResultSet rs, SqlSupport... parameter) throws Exception {
			
			Integer col = null;

			while(rs.next())
				col = rs.getInt("count(*)");
			
			return col;
			
		}
		
	}
	//获取表列数处理器
	private static class GetAllColNameHandler implements HandlerImpl<String[]>{

		public String[] handle(ResultSet rs, SqlSupport... parameter) throws Exception {
			
			int i = 0;
			String[] dataColName = new String[1024];
			
			while(rs.next())
				dataColName[i++] = rs.getString(1);
			
			return dataColName;
			
		}
		
	}
	//获取JSON数组数据处理器
	private static class GetTabletoJSONArrayHandler implements HandlerImpl<JSONArray>{

		public JSONArray handle(ResultSet rs, SqlSupport... parameter) throws Exception {
			
			JSONObject j = null;
			JSONArray jsonArray = new JSONArray();
			String[] string = parameter[0].getString();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			ResultSetMetaData rsm =rs.getMetaData();

			while(rs.next()){
				j = new JSONObject();
				for(int i = 0; i < string.length; i++)
					if(Types.TIMESTAMP == rsm.getColumnType(i + 1))
						j.put(string[i], sdf.format(rs.getTimestamp(string[i])).toString());
					else	
						j.put(string[i], rs.getObject(string[i]));
				jsonArray.put(j);
			}
			
			return jsonArray;
			
		}
		
	}
	//获取list集合处理器
	private static class GetTabletoListHandler implements HandlerImpl<List<Object>>{

		public List<Object> handle(ResultSet rs, SqlSupport... parameter) throws Exception {
			
			List<Object> list = new ArrayList<Object>();
			String[] string = parameter[0].getString();

			while(rs.next())
				for(int i = 0; i < string.length; i++)
						list.add(rs.getObject(string[i]));
			
			return list;
			
		}
		
	}
	//设置查询参数
	public void setDBTable(String table){
		
		dbTable = table;
		
	}
	public void setShowRow(int number){
		
		showRow = number;
		
	}
	//获取任意数据表列数
	private static Integer getTableCol(){
		
		String sqlStr = "select count(*) from information_schema.COLUMNS where table_name=" + "'" + dbTable + "'";
		
		return SqlSupport.DQLTemplet(sqlStr, new GetTableColHandler());
		
	}
	//获得任意数据表所有列名
	private static String[] getAllColName(){
		
		String sqlStr = "select COLUMN_NAME from information_schema.COLUMNS where table_name=" + "'" + dbTable + "'";
		
		return SqlSupport.DQLTemplet(sqlStr, new GetAllColNameHandler());
		
	}
	//拼接SQL语句前半句
	private static String sqlSentenceBefore(String sqlStr, String[] string){

		int col = SqlExecute.getTableCol();
		String[] dataColName = SqlExecute.getAllColName();
		sqlStr = "select";
		
		for(int i = 0; i < string.length; i++){
			for(int j = 0; j < col; j++){
				if(string[i].equals(dataColName[j])){
					sqlStr += " " + dataColName[j] + ",";
					break;
				}
			}
		}
		sqlStr = sqlStr.substring(0, sqlStr.length() - 1);
		
		return sqlStr;
		
	}
	//拼接SQL语句后半句
	private static String sqlSentenceAfter(String sqlStr){
		
		sqlStr += " from " + dbTable;
		
		return sqlStr;
		
	}
	private static String sqlSentenceAfter(String sqlStr, String col){
		
		if(showRow == -1)
			sqlStr += " from " + dbTable + " order by " + col + " desc";
		else
			sqlStr += " from " + dbTable + " order by " + col + " desc limit 0, " + showRow;
		
		return sqlStr;
		
	}
	private static String sqlSentenceAfter(String sqlStr, String col, String keyword){
		
		sqlStr += " from " + dbTable + " where " + col + " like '%" + keyword + "%'";
		
		return sqlStr;
		
	}
	//查询任意表某一列或多列，并转换成JSON数组
	public JSONArray getTabletoJSONArray(String col, String keyword, String...string){

		String sqlStr = null;
		SqlSupport sql = new SqlSupport();
		
		sqlStr = SqlExecute.sqlSentenceBefore(sqlStr, string);
		if(col == null && keyword == null)
			sqlStr = sqlSentenceAfter(sqlStr);
		else if(keyword == null)
			sqlStr = sqlSentenceAfter(sqlStr, col);
		else
			sqlStr = sqlSentenceAfter(sqlStr, col, keyword);
		sql.setString(string);
		
		return SqlSupport.DQLTemplet(sqlStr, new GetTabletoJSONArrayHandler(), sql);
		
	}
	public List getTabletoList(String[] string, String col, String keyword){

		String sqlStr = null;
		SqlSupport sql = new SqlSupport();
		
		sqlStr = SqlExecute.sqlSentenceBefore(sqlStr, string);
		sqlStr = sqlSentenceAfter(sqlStr, col, keyword);
		sql.setString(string);
		
		return SqlSupport.DQLTemplet(sqlStr, new GetTabletoListHandler(), sql);
		
	}
	//删除数据库表一条信息
	public void delete(String id){
		
		String sqlStr = "delete from " + dbTable + " where id = ?";
		SqlSupport.DMLTemplet(sqlStr, id);
		
	}
	//增加数据库表一条信息
	public void addOrUpdate(Map<Object, Object> map) throws SQLException{
		
		String[] str = {"biaoti", "neirong", "image", "jijie"};
		String sqlStr = "insert into " + dbTable + " (biaoti, neirong, image, jijie, hot) values (?,?,?,?,0)";
		
		for(int i = 0; i < str.length; i++)
			str[i] = (String) map.get(str[i]);
		SqlSupport.DMLTemplet(sqlStr, str);
		
	}
	//修改数据库表一条信息
	public void addOrUpdate(Map<Object, Object> map, String id) throws SQLException{
		
		String[] str = {"biaoti", "neirong", "image", "jijie", "id"};
		String sqlStr = "update " + dbTable + " set biaoti = ?, neirong = ?, image = ?, jijie = ? where id = ?";
		
		for(int i = 0; i < str.length; i++){
			if(str.length - 1 != i)
				str[i] = (String) map.get(str[i]);
			else
				str[i] = id;
		}
		SqlSupport.DMLTemplet(sqlStr, str);
		
	}
	//增加点击量
	public void update(Object id, Object hot){
		
		String sqlStr = "update " + dbTable + " set hot = ? where id = ?";
		SqlSupport.DMLTemplet(sqlStr, ((Long)hot + 1), id);
		
	}
	
}
