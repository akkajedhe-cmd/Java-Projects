import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Scanner;
import java.sql.Statement;
import java.sql.ResultSet;


public class HotelReservationSystem{
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "sqllearnerih2.0";

    public static void main(String[] args) throws ClassNotFoundException,SQLException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{
            Connection connection =DriverManager.getConnection(url,username,password);
            while(true){
                System.out.println("Hotel Management System");
                Scanner sc=new Scanner(System.in);
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservations");
                System.out.println("5. Delete Reservations");
                System.out.println("0. Exit");
                System.out.println("Choose an option: ");
                int choice = sc.nextInt();
                switch(choice){
                    case 1:
                        reserveRoom(connection,sc);
                        break;
                    case 2:
                        viewReservations(connection);
                        break;
                    case 3:
                        getRoomNumber(connection,sc);
                        break;
                    case 4:
                        updateReservation(connection,sc);
                        break;
                    case 0:
                        exit();
                        sc.close();
                        return;
                    case 5:
                        deleteReservation(connection,sc);
                        break;
                    default:
                        System.out.println("Invalid choice try again.");
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }catch(InterruptedException e){
            throw new RuntimeException(e);
        }

    }
    public static void reserveRoom(Connection connection,Scanner sc){
        try{
            System.out.print("Enter guest name: ");
            String guestName = sc.next();
            sc.nextLine();
            System.out.println("Enter room number: ");
            int roomNumber =  sc.nextInt();
            System.out.println("Enter contact number: ");
            String contactNumber = sc.next();
            String sql="Insert into reservations (guest_Name , room_number , contact_number)"+"Values('"+guestName +"',"+roomNumber+",'"+contactNumber+"')";
            try(Statement statement= connection.createStatement()){
                int affectedRows = statement.executeUpdate(sql);
                if(affectedRows>0){
                    System.out.println("Reservation successful!");
                }
                else{
                    System.out.println("Reservation failed.");
                }
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    public static void viewReservations(Connection connection)throws SQLException{
        String sql="Select reservation_id,guest_name,room_number,contact_number,reservation_date from reservations";
        try(Statement statement=connection.createStatement();
        ResultSet resultSet= statement.executeQuery(sql)){
            System.out.println("Current Reservations:");
            System.out.println("+------------------+----------+---------------+---------------------+--------------------+");
            System.out.println("| Reservation Id   | Guest    | Room Number   | Contact Number      | Reservation Date   |");
            System.out.println("+------------------+----------+---------------+---------------------+--------------------+");
            while(resultSet.next()){
                int reservationId = resultSet.getInt("reservation_id");
                String guestName = resultSet.getString("guest_name");
                int roomNumber = resultSet.getInt("room_number");
                String contactNumber = resultSet.getString("contact_number");
                String reservationDate = resultSet.getTimestamp("reservation_date").toString();
                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s  |\n",
                        reservationId,guestName,roomNumber,contactNumber,reservationDate);
            }
            System.out.println("--------------+----------+-----------+-------------+--------------+");
        }

    }
    private static void getRoomNumber(Connection connection,Scanner sc){
        try{
            System.out.print("enter reservation id: ");
            int reservationId=sc.nextInt();
            System.out.print("enter guest name: ");
            String guestName=sc.next();
            sc.nextLine();
            String sql="Select room_number from reservations "+
                    "Where reservation_id= "+reservationId+"And guest_name= '"+guestName+"'";
            try(Statement statement=connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql)){
                if(resultSet.next()){
                    int roomNumber=resultSet.getInt("room_number");
                    System.out.println("Room number for Reservation Id "+reservationId + "and Guest "+guestName+" is: "+roomNumber);
                }
                else{
                    System.out.println("Reservation not found for the given Id and Name" );
                }
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    private static void updateReservation(Connection connection ,Scanner sc){
        try{
            System.out.println("enter reservation id you want to update: ");
            int reservationId=sc.nextInt();
            sc.nextLine();//Consumes nextLine character
            if(!reservationExists(connection,reservationId)){
                System.out.println("Particular Reservation Id doesn't exist..");
                return;
            }
            System.out.println("Enter new guest Name: ");
            String newGuestName=sc.nextLine();
            System.out.println("Enter new room number: ");
            int newRoomNumber=sc.nextInt();
            System.out.println("Enter new contact number: ");
            String newContactNumber = sc.next();
            String sql = "Update reservations Set guest_name = '"+newGuestName+"',"+"room_number="+newRoomNumber+", "+"contact_number= "+newContactNumber+ "' "+
                    "Where reservation_id= "+reservationId;
            try(Statement statement = connection.createStatement()) {
                int affectedRows = statement.executeUpdate(sql);
                if(affectedRows>0){
                    System.out.println("Reservation Updated Succefully !...");

                }
                else{
                    System.out.println("Reservation Update failed.");
                }

            }

        }catch(SQLException e){
            e.printStackTrace();
        }

    }
    private static void deleteReservation(Connection con,Scanner sc){
        try{
            System.out.println("Enter the reservaation id you want to delete: ");
            int reservationId=sc.nextInt();
            if(!reservationExists(con,reservationId)){
                System.out.println("Reservation doesn't exist...");
                return;
            }
            String sql="Delete from reservations where reservation_id= "+reservationId;
            try(Statement statement= con.createStatement()){
                int affectedRows=statement.executeUpdate(sql);
                if(affectedRows>0){
                    System.out.println("Deletion completed Successfully");
                }
                else{
                    System.out.println("Reservation deletion failed..");
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    private static boolean reservationExists(Connection con, int reservationId){
        try{
            String sql="Select reservation_id from reservations where  reservation_id= "+reservationId;
            try(Statement statement = con.createStatement();
            ResultSet resultSet= statement.executeQuery(sql)){
                return resultSet.next();

            }
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public static void exit() throws InterruptedException{
        System.out.println("Exiting System");
        int i=5;
        while(i!=0){
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("Thankyou for visiting our Hotel....");
    }
}
