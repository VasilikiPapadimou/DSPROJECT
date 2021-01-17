//icsd14151 Vasiliki Papadimou
//icsd16085 Maria Lalakou
package messages;

import java.io.Serializable;
import java.util.ArrayList;

//δημιουργία λίστας για αποθήκευση των κρατήσεων
public class ReservationInfoList implements Serializable{
    ArrayList<ReservationInfo> list;
    public ReservationInfoList(){
        this.list = new ArrayList<>();
    }
    public void addReservationInfo(ReservationInfo ri){
        list.add(ri);
    }
    public ArrayList<ReservationInfo> getList(){
        
        return this.list;
    }
}
