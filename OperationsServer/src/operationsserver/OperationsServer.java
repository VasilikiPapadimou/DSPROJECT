//icsd14151 Vasiliki Papadimou
//icsd16085 Maria Lalakou
package operationsserver;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.*;

public class OperationsServer  extends UnicastRemoteObject implements RMIOperations{

    ObjectOutputStream oos;
    ObjectInputStream ois;
    
    public static void main(String[] args) {
        try  {
            OperationsServer server = new OperationsServer();
            Registry r = java.rmi.registry.LocateRegistry.createRegistry(1099);//1099 is the port number
            Naming.rebind("//localhost/OperationsServer", server);
            //μήνυμα που εμφανίζεται στον operation server όταν ειναι συνδεδεμένος και περιμένει να συνδεθεί ο client
            System.out.println("Server up and running....");
        } catch (RemoteException | MalformedURLException ex) {
            Logger.getLogger(OperationsServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    OperationsServer() throws RemoteException{
        super();
        /* 
      *********************** MΗNΥMATA CLIENT (OperationsServer) -> SERVER (MainServer)*********************** 
    */
        try {
            //Ανοιξε την θύρα 5555 και στειλε το μηνυμά μου στο localhost μέσω socket
            Socket sock = new Socket("localhost", 5555); 
            oos = new ObjectOutputStream(sock.getOutputStream()); //Δεσμευση χώρου για να παρει το oos στο socket
            ois = new ObjectInputStream(sock.getInputStream());
            
        }catch (IOException ex) {
            System.out.println("Το αντικείμενο από τον εξυπηρετητή δεν διαβάστηκε επιτυχώς");
        } 
    }
    
    @Override
    public ReservationInfoList searchFlights(LocalDate anaxorisiDate,LocalDate epistrofiDate,String anaxorisiloc,
            String proorismosloc,int epivates) throws RemoteException {
        //μήνυμα που εμφανίζεται στον operation server όταν πατήσουμε αναζήτηση
        System.out.println("RMI message - Client searched for Flights from "+anaxorisiloc+" to "+proorismosloc);
        try {
            Reservation res = new Reservation(anaxorisiDate,epistrofiDate,anaxorisiloc,proorismosloc, epivates);
            oos.writeObject(new Message("SEARCH",res));  //Μήνυμα Client προς Server
            oos.flush(); //Καθαρισμός του ObjectOutputStream
            
            Message m = (Message) ois.readObject(); //"Mεσολαβητής" Message για να μην συμπεσουν τα μηνύματα στο socket κατα το read
            System.out.println("Server:"+m.getMessage());
            for (ReservationInfo ri:m.getResinfolist().getList()){
                System.out.println(ri);
            }
            return m.getResinfolist();
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Problem in sending/receiving message to Main Server");
            Logger.getLogger(OperationsServer.class.getName()).log(Level.SEVERE, null, ex);
        }
            return null;
            
    }

    @Override
    public float reserveFlights(int anaxorisiid,int epistrofiid,int epivates) throws RemoteException {
        System.out.println("ReserveFlights");
          //μήνυμα που εμφανίζεται στον operation server όταν πατήσουμε κράτηση
            System.out.println("RMI message - Client requested reservation for Flights :"+anaxorisiid +
                    " and "+epistrofiid);
            try {
            ReservationRequest rr = new ReservationRequest(anaxorisiid,epistrofiid,epivates);
            oos.writeObject(new Message("RESERVE",rr));  //Μήνυμα Client προς Server
            oos.flush(); //Καθαρισμός του ObjectOutputStream
            Message m = (Message) ois.readObject();
            if (m.getMessage().equals("OK"))
                return m.getResack().getPrice();
            } catch (IOException | ClassNotFoundException ex) {}
            return -1;
    }
    
}
