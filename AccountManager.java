package BankingManagementSystem;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;
public class AccountManager {
    private Connection con;
    private Scanner sc;
    public AccountManager(Connection con,Scanner sc){
        this.con=con;
        this.sc=sc;
    }
    public void credit_money(long account_number)throws SQLException{
        sc.nextLine();
        System.out.println("Enter the amount you want to  credit: ");
        double amount=sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter security pin: ");
        String security_pin=sc.nextLine();
        try{
            con.setAutoCommit(false);
            if(account_number!=0){
                PreparedStatement preparedStatement=con.prepareStatement("Select * from accounts where account_number=? and security_pin=?");
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet= preparedStatement.executeQuery();
                if(resultSet.next()){
                    double current_balance=resultSet.getDouble("balance");
                    String credit_money_query="Update accounts set balance=balance+? where account_number=?";
                    PreparedStatement preparedStatement1=con.prepareStatement(credit_money_query);
                    preparedStatement1.setDouble(1,amount);
                    preparedStatement1.setLong(2,account_number);
                    int rowsAffected= preparedStatement1.executeUpdate();
                    if(rowsAffected>0){
                        System.out.println("Money credited Successfully!!");
                        con.commit();
                        con.setAutoCommit(true);
                        return;
                    }
                    else{
                        System.out.println("Transaction failed!!! ");
                        con.rollback();
                        con.setAutoCommit(true);
                    }
                }else{
                    System.out.println("Invalid account number!!!");
                }

            }


        }catch(SQLException e){
            e.printStackTrace();
        }
        con.setAutoCommit(true);

    }
    public void debitMoney(long account_number)throws SQLException {
        sc.nextLine();
        System.out.print("Enter the amount you want to debit: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security pin: ");
        String security_pin = sc.nextLine();
        String debit_account_query = "Select * from accounts where account_number=? and security_pin=?";

        try {
            con.setAutoCommit(false);
            if(account_number!=0){
            PreparedStatement preparedStatement = con.prepareStatement(debit_account_query);
            preparedStatement.setLong(1, account_number);
            preparedStatement.setString(2,security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double current_balance=resultSet.getDouble("balance");
                if(amount<=current_balance){
                    String debit_query="update accounts set balance=balance-? where account_number=?";
                    PreparedStatement preparedStatement1=con.prepareStatement(debit_query);
                    preparedStatement1.setDouble(1,amount);
                    preparedStatement1.setLong(2,account_number);
                    int rowsAffected=preparedStatement1.executeUpdate();
                    if(rowsAffected>0){
                        System.out.println("Rs "+amount+" debited successfully...");
                        con.commit();
                        con.setAutoCommit(true);
                        return;
                    }else{
                        System.out.println("transaction failed..");
                        con.rollback();
                        con.setAutoCommit(true);
                    }
                }else{
                    System.out.println("Insufficient balance!");
                }

            } else {
                System.out.println("Invalid pin!");
            }

            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        con.setAutoCommit(true);
    }
    public void getBalance(long account_number){
        sc.nextLine();
        System.out.println("Enter security pin: ");
        String security_pin=sc.nextLine();
        try{
            PreparedStatement preparedStatement=con.prepareStatement("Select balance from accounts where account_number=? and security_pin=?");
            preparedStatement.setLong(1,account_number);
            preparedStatement.setString(2,security_pin);
            ResultSet resultSet= preparedStatement.executeQuery();
            if(resultSet.next()){
                double balance=resultSet.getDouble("balance");
                System.out.println("Balance: "+balance);
            }else{
                System.out.println("Invalid pin..");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void transfer_money(long sender_account_number){
        sc.nextLine();
        System.out.println("Enter receiver's account number: ");
        long receiver_account_number=sc.nextLong();
        sc.nextLine();
        System.out.println("Enter amount  ");
        double amount=sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter Security pin: ");
        String security_pin=sc.nextLine();
        try{
            con.setAutoCommit(false);
            if(sender_account_number!=0 && receiver_account_number!=0) {
                PreparedStatement preparedStatement = con.prepareStatement("select*from accounts where account_number=? and security_pin=?");
                preparedStatement.setLong(1, sender_account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");
                    if (amount <= current_balance) {
                        String debit_query = "update accounts set balance=balance - ? where account_number=?";
                        String credit_query = "update accounts set balance=balance + ? where account_number=?";
                        PreparedStatement creditPreparedStatement = con.prepareStatement(credit_query);
                        PreparedStatement debitPreparedStatement = con.prepareStatement(debit_query);
                        creditPreparedStatement.setDouble(1, amount);
                        creditPreparedStatement.setLong(2, receiver_account_number);
                        debitPreparedStatement.setDouble(1, amount);
                        debitPreparedStatement.setLong(2, sender_account_number);
                        int rowsAffected1 = debitPreparedStatement.executeUpdate();
                        int rowsAffected2 = creditPreparedStatement.executeUpdate();
                        if (rowsAffected1 > 0 && rowsAffected2 > 0) {
                            System.out.println("Transaction Successfull");
                            System.out.println("Rs." + amount + " Transferred Successfully");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction failed!");
                            con.rollback();
                            con.setAutoCommit(true);
                        }

                    } else {
                        System.out.println("Insufficient balance");
                    }
                } else {
                    System.out.println("Invalid security pin!");
                }

            }
            con.commit();
            con.setAutoCommit(true);

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

}
