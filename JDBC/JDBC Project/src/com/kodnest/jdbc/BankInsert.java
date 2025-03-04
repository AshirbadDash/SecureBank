package com.kodnest.jdbc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class BankInsert 
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
		String str1 = "insert into Bank values(101,'IOB',2000)";
		String str2 = "insert into Bank values(102,'HDFC',4000)";
		String str3 = "insert into Bank values(103,'PNB',6000)";
		String str4 = "insert into Bank values(104,'Canara',7000)";
		String str5 = "insert into Bank values(105,'SBI',8000)";

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


