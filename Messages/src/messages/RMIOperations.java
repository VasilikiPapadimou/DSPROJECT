//icsd14151 Vasiliki Papadimou
//icsd16085 Maria Lalakou
package messages;

import java.rmi.*;
import java.time.LocalDate;

    //υλοποίηση κατασκευαστή για remote method invocation και παπαραιτητων μεθόδων 
//για αναζήτηση και κράτηση πτήσεων
public interface RMIOperations extends Remote{
    public ReservationInfoList searchFlights(LocalDate anaxorisiDate,LocalDate epistrofiDate,String anaxorisiloc,
            String proorosimosloc,int epivates) throws RemoteException;
    
    public float reserveFlights(int anaxorisiid,int epistrofiid,int epivates) throws RemoteException;
}
