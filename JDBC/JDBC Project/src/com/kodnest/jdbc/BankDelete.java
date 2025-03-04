package com.kodnest.jdbc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BankDelete 
{
	public static void main(String[] args) throws ClassNotFoundException, SQLException
	{
		System.out.println("JDBC");

		// Loading Information
		Class.forName("com.mysql.cj.jdbc.Driver");

		// Connection information
		final String url = "jdbc:mysql://localhost/kodnest";
		final String username = "root";
		final String password = "root";

		// Connection Establishment
		Connection connection = DriverManager.getConnection(url, username, password);

		System.out.println(connection + " connection success");

		// Executing SQL Delete Statement
		String deleteQuery = "delete from Bank where acnt_no=101";
		Statement stmt = connection.createStatement();
		int rowsAffected = stmt.executeUpdate(deleteQuery);

		System.out.println("Success: " + rowsAffected + " record(s) deleted");
	}
}
