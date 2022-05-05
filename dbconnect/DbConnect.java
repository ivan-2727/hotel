package dbconnect;

import java.sql.*;
import java.util.*;

import room.*;
import compare.*;

public class DbConnect {

    static String url = "jdbc:sqlite:dbconnect/Hotel.db";

    private static List < Room > queryRooms(Connection conn) {
        List <Room> rooms = new ArrayList <Room> (); 
        
        Statement stmt = null; 
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Hotel");
            while(rs.next()) {
                int number = rs.getInt("RoomNumber");
                int booked = rs.getInt("Booked");
                rooms.add(new Room(number, booked)); 
            }
        } catch (SQLException e ) {
            System.out.println("Problem while querying rooms but database is connected");
            System.out.println(e.getMessage());
        } finally {
            if (stmt != null) { 
                try {conn.close();}
                catch (SQLException ex) {
                    System.out.println("Problem while closing query statement");
                    System.out.println(ex.getMessage());
                } 
            }
        }
        Collections.sort(rooms, new Compare()); 
        return rooms;
    }

    public static List <Room> executeQueryRooms() {
        List <Room> rooms = new ArrayList <Room> (); 
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url); 
            rooms = queryRooms(conn); 
        } catch (SQLException e) {
            System.out.println("Problem while establishing connection with DB");
            System.out.println(e.getMessage());
        } finally {
            if (conn != null) {
                try {conn.close();}
                catch (SQLException ex) {
                    System.out.println("Problem while closing connection with DB");
                    System.out.println(ex.getMessage());
                }
            }
        }
         
        return rooms;
    }

    public static void updateRooms(List <Room> rooms, Connection conn) {
        Statement stmt = null; 
        try {
            for (Room room : rooms) {
                //Hard to know what's the time complexity of SQL queries. Can be O(n^2) if it looks for WHERE each time.
                //then maybe it's better to delete everything then insert again, that would take O(n). 
                stmt = conn.createStatement();
                stmt.executeUpdate(String.format("UPDATE Hotel SET Booked=%d WHERE RoomNumber=%d ", room.booked, room.number));
            }     
        } catch (SQLException e ) {
            System.out.println("Problem while updating rooms but database is connected");
            System.out.println(e.getMessage());
        } finally {
            if (stmt != null) { 
                try {conn.close();}
                catch (SQLException ex) {
                    System.out.println("Problem while closing query statement");
                    System.out.println(ex.getMessage());
                } 
            }
        }
    }

    public static void executeUpdateRooms (List <Room> rooms) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url); 
            updateRooms(rooms, conn);
        } catch (SQLException e) {
            System.out.println("Problem while establishing connection with DB");
            System.out.println(e.getMessage());
        } finally {
            if (conn != null) {
                try {conn.close();}
                catch (SQLException ex) {
                    System.out.println("Problem while closing connection with DB");
                    System.out.println(ex.getMessage());
                }
            }
        }
    }
}