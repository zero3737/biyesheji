package dao.mysql;

import java.io.FileInputStream;
import java.util.Properties;


public class ReadDBUser {

	protected static String user = null;
	protected static String passwd = null;
	protected static String driver = null;
	protected static String url = null;
	
	static{
		
		Properties p = new Properties();
		FileInputStream in;
		try {
			String path = ReadDBUser.class.getClassLoader().getResource("").getPath();
			in = new FileInputStream(path + "/db.properties");
			p.load(in);
			user = p.getProperty("username");
			passwd = p.getProperty("password");
			driver = p.getProperty("driverClassName");
			url = p.getProperty("url");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
