//icsd14151 Vasiliki Papadimou
//icsd16085 Maria Lalakou
package mainserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.*;

public class MainServer {
    static Connection conn;
  
    public static void main(String[] args) {
        //σύνδεση στη βαση δεδομένων
        connectToDatabase("flights.db");
        //δημιουργία βασης δεδομένων
        createNewTable();
        //εισαγωγή id και ημερομηνιών πτήσεων στην βάση δεδομένων
        int id=0;
        String date = "2019-06-01";
        id = insertDailyFlights(id,date);
        date = "2019-06-02";
        id = insertDailyFlights(id,date);
        date = "2019-06-03";
        id = insertDailyFlights(id,date);
        
        selectAll();
        ObjectOutputStream oos;
        ObjectInputStream ois;
        Message m;
            
            
            try {
                ServerSocket server = new ServerSocket(5555);
                System.out.println("Accepting Connection..."); // Ανοιξε ο Server
                Socket sock = server.accept(); //και Περιμένει τον Client να κάνει αποδοχή το αίτημα επικοινωνίας
                System.out.println("Operation Server Connected!");// Συνδεθηκε ο Operation Server
                oos = new ObjectOutputStream(sock.getOutputStream());//χώρος για τα outputStreams από το socket
                ois = new ObjectInputStream(sock.getInputStream());//χώρος για τα inputStreams από το socket
                
                while(true){
                    m = (Message)ois.readObject();
                    
                    //μήνυμα που εμφανίζεται στην καρτέλα του main server και αφορά την 
                    //αναζήτηση στον client                  
                    if (m.getMessage().equals("SEARCH")){
                        ReservationInfoList ril = new ReservationInfoList();
                               
                        // το μήνυμα (πτήση) που στέλνει ο operation στον main server 
                        System.out.println("Operations Server:"+m.getMessage());
                        Reservation res = m.getReservation();
                        System.out.println("Operation Server search Flight from "+res.getAnaxorisiloc()+" to "+
                                res.getProorosimosloc());
                         
                        //εμφάνιση στοιχείων αναχώρησης στο παράθυρο του client για να δούμε την διαθεσιμότητα                         
                        String sql = "SELECT * "
                          + "FROM flights WHERE flightfrom = ? AND flightto= ? AND flightdate = ?";
                         try (PreparedStatement pstmt  = conn.prepareStatement(sql)){

                              pstmt.setString(1,res.getAnaxorisiloc());
                              pstmt.setString(2,res.getProorosimosloc());
                              pstmt.setString(3,res.getAnaxorisiDate().toString());
                               ResultSet rs  = pstmt.executeQuery();
                               
                               while (rs.next()) {
                                   if (rs.getInt("passengers")>=res.getEpivates()){
                                       ReservationInfo ri = new ReservationInfo(rs.getInt("id"),
                                               LocalTime.parse(rs.getString("flighttime")),rs.getInt("price"),true);
                                       ril.addReservationInfo(ri);
                                   }
                               }
                           } catch (SQLException e) {
                               e.printStackTrace();
                               System.out.println(e.getMessage());
                           }
                         
                            //εμφάνιση στοιχείων επιστροφής στο παράθυρο του client για να δούμε την διαθεσιμότητα
                           sql = "SELECT * "
                          + "FROM flights WHERE flightfrom = ? AND flightto= ? AND flightdate = ?";
                         
                           try (PreparedStatement pstmt  = conn.prepareStatement(sql)){

                              pstmt.setString(1,res.getProorosimosloc());
                              pstmt.setString(2,res.getAnaxorisiloc());
                              pstmt.setString(3,res.getEpistrofiDate().toString());
                               ResultSet rs  = pstmt.executeQuery();
                               /*ελέγχουμε εάν ο αριθμός διαθέσιμων επιβατών της πτήσης
                               είναι μικρότερος ή μεγαλύτερος από τον αριθμό που δίνει ο πελάτης
                               Αν ο αριθμός είναι μικρότερος τότε γίνετε η κράτηση, αλλίως δεν επιτρέπεται*/
                               
                               while (rs.next()) {
                                   if (rs.getInt("passengers")>=res.getEpivates()){
                                       ReservationInfo ri = new ReservationInfo(rs.getInt("id"),
                                               LocalTime.parse(rs.getString("flighttime")),rs.getInt("price"),false);
                                       ril.addReservationInfo(ri);
                                   }
                               }
                           } catch (SQLException e) {
                               e.printStackTrace();
                               System.out.println(e.getMessage());
                           }
                           // Αποστολή αντικειμένου κράτησης 
                            oos.writeObject(new Message("OK",ril));  
                            oos.flush(); //Καθαρισμός του ObjectOutputStream
                        
                    }
                    
                    //μήνυμα που εμφανίζεται στην καρτέλα του main server και αφορά την 
                    //αναζήτηση που έκανε ο client                    
                    if (m.getMessage().equals("RESERVE")){
                        ReservationRequest rr = m.getReservationRequest();
                        System.out.println(rr.getAnaxorisiid()+"-"+rr.getEpistrofiid()+ " passengers:"+rr.getEpivates());
                        int flights=0;
                        float price=0;
                        int anaxorisipassengers=0;
                        int epistrofipassengers=0;
                        // εμφανίζουμε όλες τις πτήσεις σύμφωνα με τα id
                        String sql = "SELECT * "
                          + "FROM flights WHERE id = ?";
                           try (PreparedStatement pstmt  = conn.prepareStatement(sql)){
                              pstmt.setInt(1,rr.getAnaxorisiid());
                             ResultSet rs  = pstmt.executeQuery();
                             
                             /*εφόσον ο πελάτης κάνει κράτηση ο αριθμός των διαθέσιμων θέσεων μειώνεται
                             υπολογίζεται και η συνολική τιμή των εισητηρίων*/
                             //ΑΝΑΧΩΡΗΣΗ  
                             while (rs.next()) {
                                   if (rs.getInt("passengers")>=rr.getEpivates()){
                                       flights++;
                                       anaxorisipassengers = rs.getInt("passengers") - rr.getEpivates();
                                       price+=rs.getFloat("price");
                                   }
                               }
                           } catch (SQLException e) {
                               e.printStackTrace();
                               System.out.println(e.getMessage());
                           }
                           
                           // εμφανίζουμε όλες τις πτήσεις σύμφωνα με τα id
                           sql = "SELECT * "
                          + "FROM flights WHERE id = ?";
                           try (PreparedStatement pstmt  = conn.prepareStatement(sql)){
                              pstmt.setInt(1,rr.getEpistrofiid());
                             ResultSet rs  = pstmt.executeQuery();
                              //ΕΠΙΣΤΡΟΦΗ
                               while (rs.next()) {
                                   if (rs.getInt("passengers")>=rr.getEpivates()){
                                       flights++;
                                       epistrofipassengers = rs.getInt("passengers") - rr.getEpivates();
                                       price+=rs.getFloat("price");
                                   }
                               }
                           } catch (SQLException e) {
                               e.printStackTrace();
                               System.out.println(e.getMessage());
                           }
                           if (flights==2){
                               
       /******************************************************************************************/
                               //διαδικασία ενημέρωσης της βάσης δεδομένων αφού έχει δεχθεί τα στοιχεία
                               //της αναχώρησης
                            sql = "UPDATE flights SET passengers = ? "
                                    + "WHERE id = ?";
                            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                                pstmt.setInt(1,anaxorisipassengers);
                                pstmt.setInt(2, rr.getAnaxorisiid());
                                pstmt.executeUpdate();
                            } catch (SQLException e) {
                                System.out.println(e.getMessage());
                            }
                            
                               //διαδικασία ενημέρωσης της βάσης δεδομένων αφού έχει δεχθεί τα στοιχεία
                               //της επιστροφής
                            
                             sql = "UPDATE flights SET passengers = ? "
                                    + "WHERE id = ?";
                            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                                pstmt.setInt(1,epistrofipassengers);
                                pstmt.setInt(2, rr.getEpistrofiid());
                                pstmt.executeUpdate();
                            } catch (SQLException e) {
                                System.out.println(e.getMessage());
                            }   
                            //μήνυμα επιβεβαίωσης της κράτησης
                            oos.writeObject(new Message("OK",new ReservationAck(rr.getEpivates()*price)));  
                            oos.flush();
                           }
                           else{
                               //μήνυμα για μη επιβεβαίωση της κράτησης
                               oos.writeObject(new Message("NOT OK"));
                                oos.flush();
                           }
                        }
                }
                
            } catch (IOException ex) {
                Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MainServer.class.getName()).log(Level.SEVERE, null, ex);
            }
                
                
            }
    //διαθέσημες πτήσεις (για 4 αεροδρόμια)
    private static int insertDailyFlights(int id,String date){
        insert(id++,date,"12:00","Αθήνα","Καστοριά",100,50);
        insert(id++,date,"17:00","Καστοριά","Αθήνα",100,50);
        insert(id++,date,"12:00","Αθήνα","Κοζάνη",100,50);
        insert(id++,date,"17:30","Κοζάνη","Αθήνα",100,50);
        insert(id++,date,"11:30","Αθήνα","Θεσσαλονίκη",100,50);
        insert(id++,date,"13:30","Αθήνα","Θεσσαλονίκη",100,50);
        insert(id++,date,"11:30","Θεσσαλονίκη","Αθήνα",100,50);
        insert(id++,date,"13:30","Θεσσαλονίκη","Αθήνα",100,50);
        return id;
    }
    
    public static void connectToDatabase(String dbname) {
 
            try {
            // παράμετροι της βάσης δεδομένων
            String url = "jdbc:sqlite:"+dbname;
            // δημιουργία σύνδεσης με βδ
            conn = DriverManager.getConnection(url);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
    }
    
    public static void createNewTable() {
        // drop table κάθε φορά που ο client ανοίγει το πρόγραμμα ...
       String sql = "Drop table flights";
 
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            //εκτέλεση του upadate (...για να μην προστίθενται και οι παλίες πτήσεις που εγιναν αναζήτηση)
            pstmt.executeUpdate();
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
      
        sql = "CREATE TABLE flights (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	flightdate text NOT NULL,\n"
                + "	flighttime text NOT NULL,\n"
                + "	flightfrom text NOT NULL,\n"
                + "	flightto text NOT NULL,\n"
                + "	price float NOT NULL,\n"
                + "	passengers integer \n"
                + ");";
        
        try (Statement stmt = conn.createStatement()) {
      //εκτέλεση δημιουργίας table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    //εισαγωγή των πτήσεων που δηλώσαμε παραπάνω
    public static void insert(int id,String flightdate,String flighttime,String flightfrom,
                    String flightto,float price,int passengers) {
        String sql = "INSERT INTO flights(id,flightdate,flighttime,flightfrom,flightto,price,passengers) VALUES(?,?,?,?,?,?,?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1,id);
            pstmt.setString(2, flightdate);
            pstmt.setString(3, flighttime);
            pstmt.setString(4, flightfrom);
            pstmt.setString(5, flightto);
            pstmt.setFloat(6, price);
            pstmt.setInt(7,passengers);    
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    

    public static void selectAll(){
        System.out.println("Welcome to our database server:");
        System.out.println("Available flights:");
        String sql = "SELECT id, flightdate, flighttime,flightfrom,flightto FROM flights";
        
        try (
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
           //εμφάνιση πτήσεων στην καρτέλα του main server
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" + 
                                   rs.getString("flightdate") + "\t" +
                                   rs.getString("flighttime")+"\t"+rs.getString("flightfrom")+
                                           "\t"+rs.getString("flightto"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}