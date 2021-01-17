//icsd14151 Vasiliki Papadimou
//icsd16085 Maria Lalakou
package client;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class ResultsPanel extends JPanel{
    AgentClient ac;
    ResultsPanel(AgentClient ac){
        this.ac = ac;
        //μήνυμα τριτης καρτέλας όσο περιμένουμε να πατησει ο χρήστης το κουμπί της αποθήκευσης κράτησης       
        this.add(new JLabel("Αναμονή κράτησης...."));
    }
    
    void showResults(float price){
        this.removeAll();
        //εμφάνιση μηνύματος για μη επιτυχή κράτηση
        if (price==-1){
         this.add(new JLabel("Κράτηση μη επιτυχής, δεν βρέθηκαν θέσεις"));
        }
        //εμφάνιση μηνύματος για επιτυχή κράτηση και συνολική τιμή
        else{
            this.add(new JLabel("Κράτηση επιτυχής, συνολική τιμή:"+price));
            
        }
        this.revalidate();
    }
    
}
