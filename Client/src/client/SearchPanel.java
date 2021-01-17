//icsd14151 Vasiliki Papadimou
//icsd16085 Maria Lalakou
package client;

import java.awt.*;
import java.awt.event.*;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import messages.ReservationInfo;
import messages.ReservationInfoList;

public class SearchPanel extends JPanel{
    AgentClient ac; // Αντικείμενο ac τύπου AgentClient
    SearchPanel(AgentClient ac){
        this.ac = ac;
    
        
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        
        
        /*ΔΗΜΙΟΥΡΓΊΑ ΧΩΡΟΥ ΗΜΕΡΟΜΗΝΊΑΣ ΑΦΙΞΗΣ*/
        String []locations ={"Αθήνα","Καστοριά","Κοζάνη","Θεσσαλονίκη"}; //αεροδρόμια 
        
       
        JPanel departpanel = new JPanel();
        departpanel.setOpaque(true);
        JLabel departlabel = new JLabel("Ημερομηνία Αναχώρησης (yyyy-mm-dd):"); 
        departlabel.setForeground(Color.darkGray);
        departpanel.add(departlabel); 
        JTextField departurefield = new JTextField(); 
        departurefield.setPreferredSize(new Dimension(100,20));
        departpanel.add(departurefield); 
        this.add(departpanel);
        
        JPanel departlocpanel = new JPanel();
        departlocpanel.setOpaque(true);
        JLabel departloclabel = new JLabel("Αεροδρόμιο Αναχώρησης:"); 
        departloclabel.setForeground(Color.darkGray);
        departlocpanel.add(departloclabel); 
        JComboBox departbox = new JComboBox(locations);// dropdown list
        departlocpanel.add(departbox);
        this.add(departlocpanel);
        
        JPanel returnpanel = new JPanel();
        returnpanel.setOpaque(true);
        JLabel returnlabel = new JLabel("Ημερομηνία Επιστροφής (yyyy-mm-dd):"); 
        returnlabel.setForeground(Color.darkGray);
        returnpanel.add(returnlabel); 
        JTextField returnfield = new JTextField(); 
        returnfield.setPreferredSize(new Dimension(100,20));
        returnpanel.add(returnfield); 
        this.add(returnpanel);
        
        JPanel returnlocpanel = new JPanel();
        returnlocpanel.setOpaque(true);
        JLabel returnloclabel = new JLabel("Αεροδρόμιο Προορισμού:"); 
        returnloclabel.setForeground(Color.darkGray);
        returnlocpanel.add(returnloclabel); 
        JComboBox returnbox = new JComboBox(locations);// dropdown list
        returnlocpanel.add(returnbox);
        this.add(returnlocpanel);
        
        String []seats ={"1","2","3","4","5"}; // Αριθμός θέσεων σε μία κράτηση
        
        JPanel seatspanel = new JPanel();
        seatspanel.setOpaque(true);
        JLabel seatslabel = new JLabel("Θέσεις:"); 
        seatslabel.setForeground(Color.darkGray);
        seatspanel.add(seatslabel); 
        JComboBox seatsbox = new JComboBox(seats);// dropdown list
        seatspanel.add(seatsbox);
        this.add(seatspanel);
        
        
        /* ΓΡΑΦΙΚΟ ΓΙΑ ΑΠΟΣΤΟΛΗ ΚΡΑΤΗΣΗΣ */
        
        JButton searchbutton = new JButton(new ImageIcon("icons/search.png"));
        // ΤΙ ΘΑ ΚΑΝΕΙ ΑΥΤΟ ΤΟ ΚΟΥΜΠΙ
        searchbutton.addActionListener(new ActionListener(){ 
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    LocalDate anaxorisiDate,epistrofiDate;
                    anaxorisiDate = LocalDate.parse(departurefield.getText());
                    epistrofiDate = LocalDate.parse(returnfield.getText());
                    String anaxorisiloc,proorosimosloc;
                    anaxorisiloc = (String) departbox.getSelectedItem();
                    proorosimosloc = (String) returnbox.getSelectedItem();
                    if (anaxorisiloc.equals(proorosimosloc)){
                        JOptionPane.showMessageDialog(ac,  "Πρέπει να επιλεγούν διαφορετικά αεροδρόμια","Λάθος αεροδρόμια",JOptionPane.INFORMATION_MESSAGE );
                
                    }
                    else{
                        int epivates = Integer.valueOf((String)seatsbox.getSelectedItem());
                        ReservationInfoList ril = ac.look_op.searchFlights(anaxorisiDate,epistrofiDate,anaxorisiloc,proorosimosloc,epivates);
                        
                        ac.tabbedPane.setSelectedIndex(1);
                        ac.reservepanel.showFlights(ril,anaxorisiloc,proorosimosloc);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(SearchPanel.class.getName()).log(Level.SEVERE, null, ex);
                }catch (DateTimeParseException ex){
                    JOptionPane.showMessageDialog(ac,  "Η ημερομηνία πρέπει να έχει την μορφή yyyy-mm-dd","Λάθος ημερομηνία",JOptionPane.INFORMATION_MESSAGE );
                }
            }});
        this.add(searchbutton);
    }
}
