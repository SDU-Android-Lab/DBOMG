package org.db4a.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RemoteDBHelper {

	public static String DriverName = "net.sourceforge.jtds.jdbc.Driver";
	private String ip;
	private int port;
	private String dbName;
	private String user;
	private String pwd;
	private Connection connection = null;

	public RemoteDBHelper(String ip, String dbName, String user, String pwd) {
		this(ip, 1433, dbName, user, pwd);
	}

	public RemoteDBHelper(String ip, int port, String dbName, String user,
			String pwd) {
		this.ip = ip;
		this.port = port;
		this.dbName = dbName;
		this.user = user;
		this.pwd = pwd;
	}

	public Connection getConnection() throws ClassNotFoundException, SQLException  {
		if (connection == null||connection.isClosed()) {
				Class.forName("net.sourceforge.jtds.jdbc.Driver");
				connection = DriverManager.getConnection(
						"jdbc:jtds:sqlserver://"+ip+":"+port+"/"+dbName, user,
						pwd);

		}
		return connection;

	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}
