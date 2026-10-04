package BankingMnaganmentSystem;
import java.sql.*;
import java.util.Scanner;
import static java.lang.Class.forName;
public class Banking{
    private static final String url="jdbc:mysql://localhost:3306/banking_system";
    private static final String username="root";
    private static final String password="sqllearnerih2.0";
    public static void main(String[] args)throws ClassNotFoundException,SQLException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver loaded Successfully!!!");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{
            Connection con=DriverManager.getConnection(url,username,password);
            Scanner sc = new Scanner(System.in);
            BankingManagementSystem.User user=new BankingManagementSystem.User(con,sc);
            BankingManagementSystem.Accounts accounts=new BankingManagementSystem.Accounts(con,sc);
            BankingManagementSystem.AccountManager accountManager=new BankingManagementSystem.AccountManager(con,sc);
            String email;
            long account_number;
            while(true){
                System.out.println("Welcom to BANKING SYSTEM...");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("Enter your choice..");
                int choice1=sc.nextInt();
                switch(choice1){
                    case 1:
                        user.register();
                        System.out.println("\033[H\033[2J");
                        System.out.flush();
                        break;
                    case 2:
                        email = user.login();
                        if(email!=null) {
                            System.out.println();
                            System.out.println("User Logged in! ");
                        }
                            if(accounts.account_exists(email)==false){
                                System.out.println();
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2. Exit");
                                int choice=sc.nextInt();
                                if(choice==1){
                                    System.out.println("debug email= "+email);
                                    account_number=accounts.open_account(email);
                                    System.out.println("Account created successfully...");
                                    System.out.println("your account number id is: "+account_number);

                                }else{
                                    break;
                                }

                            }
                            account_number=accounts.getAccount_number(email);
                            int choice2=0;
                            while(choice2!=5){
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check balance");
                                System.out.println("5. Log Out ");
                                System.out.println("Enter your choice");
                                choice2=sc.nextInt();
                                switch(choice2){
                                    case 1:
                                        accountManager.debitMoney(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter Valid choice! ");
                                        break;
                                }
                            }
                        }

                }
            }catch(SQLException e) {
            e.printStackTrace();
            }

        }

    }

