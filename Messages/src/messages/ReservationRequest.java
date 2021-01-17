//icsd14151 Vasiliki Papadimou
//icsd16085 Maria Lalakou
package messages;

import java.io.Serializable;

public class ReservationRequest implements Serializable{
    int anaxorisiid;
    int epistrofiid;
    int epivates;
    public ReservationRequest(int anaxorisiid,int epistrofiid,int epivates){
        this.anaxorisiid = anaxorisiid;
        this.epistrofiid = epistrofiid;
        this.epivates = epivates;
    }

    public int getAnaxorisiid() {
        return anaxorisiid;
    }

    public int getEpistrofiid() {
        return epistrofiid;
    }

    public int getEpivates() {
        return epivates;
    }
    
    
}
