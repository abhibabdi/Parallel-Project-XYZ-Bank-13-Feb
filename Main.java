package com.ibm.training;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

//import javax.swing.text.DefaultEditorKit.CopyAction;

public class Main {
	int a;
	int oldbal = 0;
	int validpin=0;
	public static void main(String[] args)
	
	{
		//Load the driver
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
			Connection dbCon = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank?useTimezone=true&serverTimezone=UTC","root","");
			
			//new Main().getAll(dbCon);
			
			//new Main().depositMoney(dbCon);
			
			//new Main().fundTransfer(dbCon);
			
//	new Main().withdrawMoney(dbCon);
			
			new Main().printTransactions(dbCon);
			
			
		}
		catch (ClassNotFoundException e) {
			System.out.println("Exception while loading driver" + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Exception while connecting to db : " + e.getMessage());
		}
	}
	
	void getAll(Connection dbCon ) {
	
		try {
			Statement stmt = dbCon.createStatement();
			
			String fetchQry = "select * from other_details";
			
			ResultSet rs = stmt.executeQuery(fetchQry);
			
			while(rs.next())
			{
				System.out.print("User ID : "+rs.getInt("User_Id")+"  ");
				System.out.print("User Age : "+rs.getString("User_Age")+"  ");
				System.out.println("User PhoneNo : "+rs.getString("User_PhoneNo"));
				
			}
			
			dbCon.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception while connecting to db : " + e.getMessage());
		}
		
		
		
	}
	
	/*void insertInto(Connection dbCon) {
		try {
			Statement stmt = dbCon.createStatement();
			
			String insertQry = "insert into values(13,'Mohit','Bidar')"
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}*/
	
	void createAccount(Connection dbCon)
	{
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Please enter your name  : ");
		
		String User_Name = scan.nextLine();
		
		System.out.println("Please enter your  address : ");
		
		String User_Address = scan.nextLine();
		
		System.out.println("Enter your Phone no :");
		
		String phone = scan.nextLine();
		
		System.out.println("Create your four digit pin No : ");
		
		int pin = scan.nextInt();
		
		String insertQ = "insert into userdetails(userName, userAddress, userPhoneNo) values"
				         +"("
				+"'"+User_Name+"'"
				+",'"+User_Address+"'"
				+",'"+phone+"'"
				+ ")";
		Statement stmt;
		try {
			stmt = dbCon.createStatement();
		
			stmt.executeUpdate(insertQ);
			
			String updateQry = "update userdetails set accountNo=LPAD(FLOOR(RAND() * 99999999.99), 8, '0')";
			
			stmt.executeUpdate(updateQry);
			
			System.out.println("Account Details Are : ");
			
			String getAll = "select * from userdetails where userName="+"'"+User_Name+"'";
			
			ResultSet rs = stmt.executeQuery(getAll);
			
			while(rs.next())
			{
				System.out.println("User Name : "+rs.getString("userName"));
				
				System.out.println("Account No : "+rs.getInt("accountNo"));
				System.out.println("Address : "+rs.getString("userAddress"));
				System.out.println("Phone No : "+rs.getString("userPhoneNo"));
				
			}
			
			String query = "select accountNo from userdetails where userName ="+"'"+User_Name+"'";
			
			rs = stmt.executeQuery(query);
			
			while(rs.next())
			{
				a = rs.getInt("accountNo");
			}
			
			//System.out.println(a);
			
			String pindet ="insert into pindetails(accountNo, pinNo) values"
					+"("
					+"'"+a+"'"
					+", '"+pin+"'"
					+")";
			
			stmt.executeUpdate(pindet);
			
			
			dbCon.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	/*
	void lowBalance() {
		if(b.balance < 100)
		{
			System.out.println("You have Low Balance In Your Account!!!");
		}
		else
			System.out.println("You can Continue with Your Transactions....");
	}*/
	
	void depositMoney(Connection dbCon) {
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter Your AccountNo to Deposit Amount : ");
		
	   int ac = scan.nextInt();
	   
	   System.out.println("Enter the Amount You want to Deposit : ");
	   
	   int dep = scan.nextInt();
	   
	   
	   
	   String oldbalQry = "select balance from userdetails where accountNo="+"'"+ac+"'";
	   
	   Statement stmt;
	   try {
		   
	   
	   stmt = dbCon.createStatement();
	   
	  
	   ResultSet rs = stmt.executeQuery(oldbalQry);
	   
	   while(rs.next())
	   {
		   oldbal = rs.getInt("balance");
	   }
	   
	   int newbal =0;
	   
	    newbal = dep+oldbal;
	 
	   String depositQry = "update userdetails set balance ="+"'"+newbal+"'"+"where accountNo="+"'"+ac+"'";
	   
	   stmt.executeUpdate(depositQry);
	   
	   dbCon.close();
	   }
	   catch(SQLException e)
	   {
		  System.out.println("Something is wrong SQL Exception!!!!!!!!!!!!!!!!!!!!!!"+e.getMessage()); 
	   }
		
	   
	}
	
	void fundTransfer(Connection dbCon)
	{
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Enter The Account No Of your Account..");
		
		int myAccount = scan.nextInt();
		
		System.out.println("Enter Recepient Account No : ");
		
		int recep = scan.nextInt();
		
		System.out.println("Enter Four Digit Pin to Proceed The Fund Transfer : ");
		
		int myPin = scan.nextInt();
		
		String valid = "select pinNo from pindetails where accountNo="+"'"+myAccount+"'";
		
		Statement stmt;
		try
		{
		
		stmt = dbCon.createStatement();
		
		ResultSet rs = stmt.executeQuery(valid);
		
		while(rs.next())
		{
			validpin = rs.getInt("pinNo");
		}
		
		if(myPin == validpin)
		{
			System.out.println("You have Entered Pin Correctly...");
			
			System.out.println("Enter the Amount You Want to transfer : ");
			
			int tran = scan.nextInt();
			
			String avail = "select balance from userdetails where accountNo="+"'"+myAccount+"'";
			
			int thresold = 0;
			
            rs = stmt.executeQuery(avail);
            
            while(rs.next())
            {
            	thresold = rs.getInt("balance");
            }
            

			String avail1 = "select balance from userdetails where accountNo="+"'"+recep+"'";
			
			int thresold1 = 0;
			
            rs = stmt.executeQuery(avail1);
            
            while(rs.next())
            {
            	thresold1 = rs.getInt("balance");
            }
            
            if(thresold >= tran)
            {
            	int bal = thresold - tran;
            	
            	String updateBal = "update userdetails set balance="+"'"+bal+"'"+"where accountNo="+"'"+myAccount+"'";
            
            	stmt.executeUpdate(updateBal);
            	
                thresold1 = thresold1 + tran;
                
                
                String recepUpdate = "update userdetails set balance="+"'"+thresold1+"'"+"where accountNo="+"'"+recep+"'";
                
                stmt.executeUpdate(recepUpdate);
            	
            	String display = "select userName, accountNo, balance from userdetails where accountNo="+"'"+myAccount+"'";
            	
            	rs = stmt.executeQuery(display);
            	
            	while(rs.next())
            	{
            		System.out.println("User Name : "+rs.getString("userName"));
            		System.out.println("AccountNo : "+rs.getInt("accountNo"));
            		System.out.println("Updated Balance : "+rs.getInt("balance"));
            		
            	}
            }
			
			
		}
		
		
	}
		catch(SQLException e)
		{
			System.out.println("Some error related to SQL Exception...  "+e.getMessage());
		}

}
	
	void withdrawMoney(Connection dbCon) {
		Scanner scan = new Scanner(System.in);
		
        System.out.println("Enter The Account No Of your Account..");
		
		int myAccount = scan.nextInt();
		
		System.out.println("Enter Four Digit Pin to Proceed The Fund Transfer : ");
		
		int myPin = scan.nextInt();
		
        String valid = "select pinNo from pindetails where accountNo="+"'"+myAccount+"'";
		
		Statement stmt;
		
		try
		{
		
		stmt = dbCon.createStatement();
		
		ResultSet rs = stmt.executeQuery(valid);
		
		while(rs.next())
		{
			validpin = rs.getInt("pinNo");
		}
		
		if(myPin == validpin)
		{
			System.out.println("You have Entered Pin Correctly...");
			
			System.out.println("Enter the Amount You Want to Withdraw : ");
			
			int tran = scan.nextInt();
			
			String avail = "select balance from userdetails where accountNo="+"'"+myAccount+"'";
			
			int thresold = 0;
			
            rs = stmt.executeQuery(avail);
            
            while(rs.next())
            {
            	thresold = rs.getInt("balance");
            }
            

			
			
			
            if(thresold >= tran)
            {
            	int bal = thresold - tran;
            	
            	String updateBal = "update userdetails set balance="+"'"+bal+"'"+"where accountNo="+"'"+myAccount+"'";
            
            	stmt.executeUpdate(updateBal);
            	
            	String display = "select userName, balance from userdetails where accountNo="+"'"+myAccount+"'";
            	
            	rs = stmt.executeQuery(display);
            	
            	while(rs.next())
            	{
            		System.out.println("User Name : "+rs.getString("userName"));
            		//System.out.println("AccountNo : "+rs.getInt("accountNo"));
            		System.out.println("Amount After Withrawl is : "+rs.getInt("balance"));
            		//System.out.println("Updated Balance : "+rs.getInt("balance"));
            		
            	}
            }
			
			
		}
		
		
	}
		catch(SQLException e)
		{
			System.out.println("Some error related to SQL Exception...  "+e.getMessage());
		}
		
	}
	
	void printTransactions(Connection dbCon)
	{
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Enter the Account Number to Get Transaction Details : ");
		
		int myAccount = scan.nextInt();
		
		System.out.println("Enter the Four Digit Pin Code To Proceed...  ");
		
		int pin =scan.nextInt();
		
		Statement stmt;
		try {
		
		stmt = dbCon.createStatement();
		
		String validPin = " select pinNo from pindetails where accountNo="+"'"+myAccount+"'";
		
		ResultSet rs = stmt.executeQuery(validPin);
		
		int myPin = 0;
		
		while(rs.next())
		{
		 myPin = rs.getInt("pinNo");
		}
		if(myPin == pin)
		{
			System.out.println("You have Entered Your Pin Correctly...");
			
			String printTran ="select previousTran, amount from trandetails where accountNo="+"'"+myAccount+"'";
			
			rs = stmt.executeQuery(printTran);
			
			System.out.println("The Transaction Details Are : ");
			
			while(rs.next())
			{
				System.out.print("Transaction Type : "+rs.getString("previousTran")+"  ");
				System.out.println("Amount : "+rs.getInt("amount"));
			}
		}
		
	}
		catch(SQLException e)
		{
			System.out.println("Error Dude!!!!"+e.getMessage());
		}
	}
}
