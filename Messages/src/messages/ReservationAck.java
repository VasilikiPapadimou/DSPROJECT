//icsd14151 Vasiliki Papadimou
//icsd16085 Maria Lalakou
package messages;

import java.io.Serializable;

public class ReservationAck implements Serializable{
    float price;
    public ReservationAck(float price){
        this.price = price;
    }

    public float getPrice() {
        return price;
    }
    
}
