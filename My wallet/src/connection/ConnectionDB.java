package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import inputOutput.Output;

public class ConnectionDB {
	private final static String dbType = "sqlserver";
	private final static String ip = "A-SRV-BDINST";
	private final static String port = "1433";
	private final static String dbName = "BD21A02";
	private final static String user = "BD21A02";
	private final static String password = "BD21A02";
	private final static String template = "jdbc:%s://%s:%s;databaseName=%s;";
	
	
	public static Connection getConection() {
		Connection connection = null;
		String url = String.format(template, dbType, ip, port, dbName);
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connection = DriverManager.getConnection(url, user, password);
		}catch (ClassNotFoundException e) {
			Output.mostrarMensaje(e.getMessage() + "Error no se pudo establecer la coneccion");
		}
		catch (SQLException e) {
			Output.mostrarMensaje(e.getMessage() + "Error no se pudo establecer la coneccion");
		}
		return connection;
	}
	
}
