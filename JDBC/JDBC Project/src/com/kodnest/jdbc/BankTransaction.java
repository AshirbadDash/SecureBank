package com.kodnest.jdbc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BankTransaction 
{
	public static void main(String[] args) throws ClassNotFoundException, SQLException 
	{
		System.out.println("JDBC");

		//Loading Information
		Class.forName("com.mysql.cj.jdbc.Driver");

		//connection information
		final String url = "jdbc:mysql://localhost/kodnest";
		final String username="root";
		final String password="root";

		//executing a SQL Query
		String str1 = "update bank set balance=balance-500 where acnt_no=101";
		String str2 = "updat bank set balance=balance+500 where acnt_no=102";
		Connection conn=null;
		try {
		//Connection Establishment
		 conn = DriverManager.getConnection(url, username, password);
		conn.setAutoCommit(false);

		//Creating the Statement
		Statement st = conn.createStatement();
		st.addBatch(str1);
		st.addBatch(str2);
		st.executeBatch();
		//save the table
		conn.commit();
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("Exception occured - rolling back the changes");
		}

		System.out.println("Succes->>>>>>>>>>>"); 

	}
}