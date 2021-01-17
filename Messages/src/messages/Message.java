//icsd14151 Vasiliki Papadimou
//icsd16085 Maria Lalakou
package messages;


import java.io.Serializable;
//Στην κλάση αυτή υλοποιούμε κώδικα για την σύνδεση των Client/Server
//Διανέμουμε τα μηνυματα έτσι ώστε να καταφέρουν να φτάσουν χωρίς να χαθούν
//Είναι μία κλάση "μεσολαβητής" 
public class Message implements Serializable{
    private String str;
    Reservation res;
    ReservationInfoList resinfolist;
    ReservationRequest resrequest;
    ReservationAck resack;
    public Message(String str){
        this.str = str;
        res = null;
    }
    public Message(String str,Reservation res){
        this.str = str;
        this.res = res;
    }
    public Message(String str,ReservationInfoList resinfolist){
        this.str = str;
        this.resinfolist = resinfolist;
    }
    public Message(String str,ReservationRequest resrequest){
        this.str = str;
        this.resrequest = resrequest;
    }
    public Message(String str,ReservationAck resack){
        this.str = str;
        this.resack = resack;
    }
    public Reservation getReservation(){
        return this.res;
    }
    public String getMessage(){
        return this.str;
    }

    public ReservationInfoList getResinfolist() {
        return resinfolist;
    }
    public ReservationRequest getReservationRequest(){
        return this.resrequest;
    }

    public ReservationAck getResack() {
        return resack;
    }
    
}
