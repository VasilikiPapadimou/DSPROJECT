//icsd14151 Vasiliki Papadimou
//icsd16085 Maria Lalakou
package messages;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationInfo implements Serializable{
    int id;
    LocalTime anaxorisiTime;
    float price;
    boolean depart;
    // Κατασκευαστής για αποστολή στοιχείων από τον operation server στον client
    public ReservationInfo(int id,LocalTime anaxorisiTime,float price,boolean depart){
        this.id = id;
        this.anaxorisiTime = anaxorisiTime;
        this.price = price;
        this.depart = depart;
    }
    
    public String toString(){
        return "Flight no:"+id+" Time:"+anaxorisiTime+ " Price:"+price;
    }

    public boolean isDepart() {
        return depart;
    }
    public int getId(){
        return this.id;
    }
}
