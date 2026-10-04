package BankingManagementSystem;

import java.sql.*;
import java.util.Scanner;
public class Accounts {
    private Connection connection;
    Scanner scanner;
    public Accounts(Connection con,Scanner sc){
        connection=con;
        scanner=sc;
    }
    public long open_account(String email){
        if(account_exists(email)==false){
            String open_account_query="insert into accounts(account_number,full_name,email,balance,security_pin)Values(?,?,?,?,?)";
            scanner.nextLine();
            System.out.println("Enter full name: ");
            String full_name=scanner.nextLine();
            System.out.println("Enter initial amount: ");
            double balance=scanner.nextDouble();
            scanner.nextLine();
            System.out.println("Enter security pin: ");
            String security_pin=scanner.nextLine();
            try {
                long account_number=generateAccountNumber();
                PreparedStatement preparedStatement=connection.prepareStatement(open_account_query);
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,full_name);
                preparedStatement.setString(3,email);
                preparedStatement.setDouble(4,balance);
                preparedStatement.setString(5,security_pin);
                int rowsAffected=preparedStatement.executeUpdate();
                if(rowsAffected>0){
                    return account_number;
                }else{
                    throw new RuntimeException("Account creation failed");
                }
            }catch(SQLException e){
                e.printStackTrace();
            }

        }
        throw new RuntimeException("Account already Exist");
    }
    public long getAccount_number(String email){
        String query="Select account_number from accounts where email=?";
        try{
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet= preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getLong("account_number");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        throw new RuntimeException("AccountNUmber doesn't exist");
    }
    private long generateAccountNumber(){
        try{
            Statement statement=connection.createStatement();
            ResultSet resultSet= statement.executeQuery("Select * from accounts order by account_number desc limit 1");
            if(resultSet.next()) {
                long last_account_number = resultSet.getLong("account_number");
                return last_account_number + 1;
            }else{
                return 10000100;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return 10000100;
    }
    public boolean account_exists(String email){
        String query="select * from accounts where email=?" ;
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet= preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }
            else{
                return false;
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }


}
