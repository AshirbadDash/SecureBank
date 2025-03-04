package com.kodnest.jdbc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BankUpdate 
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

		//Connection Establishment
		Connection connection = DriverManager.getConnection(url, username, password);

		System.out.println(connection + "connection succes");

		//executing a SQL Query
		String str1 = "update bank set balance=balance+500 where acnt_no=101";
		String str2 = "update bank set balance=balance+500 where acnt_no=102";
		String str3 = "update bank set balance=balance+500 where acnt_no=103";
		String str4 = "update bank set balance=balance+500 where acnt_no=104";
		String str5 = "update bank set balance=balance+500 where acnt_no=105";

		//Creating the Statement
		Statement st = connection.createStatement();
		st.addBatch(str1);
		st.addBatch(str2);
		st.addBatch(str3);
		st.addBatch(str4);
		st.addBatch(str5);
		st.executeBatch();

		System.out.println("Succes - "); 

	}
}