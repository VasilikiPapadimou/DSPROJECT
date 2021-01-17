//icsd14151 Vasiliki Papadimou
//icsd16085 Maria Lalakou
package messages;

import java.io.Serializable;
import java.time.LocalDate;

public class Reservation implements Serializable{
    LocalDate anaxorisiDate;
    LocalDate epistrofiDate;
    String anaxorisiloc;
    String proorosimosloc;
    int epivates;
    
    // Κατασκευαστής για αποστολή στοιχείων από τον πελάτη
    public Reservation(LocalDate anaxorisiDate,LocalDate epistrofiDate,String anaxorisiloc,
            String proorosimosloc,int epivates){
        this.anaxorisiDate = anaxorisiDate;
        this.epistrofiDate = epistrofiDate;
        this.anaxorisiloc = anaxorisiloc;
        this.proorosimosloc = proorosimosloc;
        this.epivates = epivates;
    }

    public LocalDate getAnaxorisiDate() {
        return anaxorisiDate;
    }

    public LocalDate getEpistrofiDate() {
        return epistrofiDate;
    }

    public String getAnaxorisiloc() {
        return anaxorisiloc;
    }

    public String getProorosimosloc() {
        return proorosimosloc;
    }

    public int getEpivates() {
        return epivates;
    }
}
