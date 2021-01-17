//icsd14151 Vasiliki Papadimou
//icsd16085 Maria Lalakou
package client;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import messages.ReservationInfo;
import messages.ReservationInfoList;

public class ReservePanel extends JPanel{
    AgentClient ac; // Αντικείμενο ac τύπου AgentClient
    ReservePanel(AgentClient ac){
        this.ac = ac;
        //μήνυμα δέυτερης καρτέλας όσο περιμένουμε να πατησει ο χρήστης το κουμπί της αναζήτησης
        this.add(new JLabel("Αναμονή αναζήτησης...."));
        
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        
}
    
    void showFlights(ReservationInfoList ril,String anaxorisiloc, String proorosimosloc){
        this.removeAll();
        //εμφάνιση πτήσεων αναχώρησης στο παράθυρο
        this.add(new JLabel("Πτήσεις από "+anaxorisiloc+" προς "+proorosimosloc));
        ButtonGroup departgroup = new ButtonGroup();
        for (ReservationInfo ri:ril.getList()){
            if (ri.isDepart()){
                JRadioButton rb = new JRadioButton(ri.toString());
                rb.setActionCommand(String.valueOf(ri.getId()));
                departgroup.add(rb);
                this.add(rb);
            }
        }
        
         //εμφάνιση πτήσεων επιστροφής στο παράθυρο
        this.add(new JLabel("Πτήσεις από "+proorosimosloc+" προς "+anaxorisiloc));
        ButtonGroup returngroup = new ButtonGroup();
        for (ReservationInfo ri:ril.getList()){
            if (!ri.isDepart()){
                JRadioButton rb = new JRadioButton(ri.toString());
                rb.setActionCommand(String.valueOf(ri.getId()));
                returngroup.add(rb);
                this.add(rb);
                
            }
        }
        
        String []seats ={"1","2","3","4","5"}; // Αριθμός θέσεων σε μία κράτηση
        //υλοποίηση combobox για επιλογή αριθμού ατόμων για την πτήση
        JPanel seatspanel = new JPanel();
        seatspanel.setOpaque(true);
        JLabel seatslabel = new JLabel("Θέσεις:"); 
        seatslabel.setForeground(Color.darkGray);
        seatspanel.add(seatslabel); 
        JComboBox seatsbox = new JComboBox(seats);
        seatspanel.add(seatsbox);
        this.add(seatspanel);
        
        //save κράτησης
        JButton reservebutton = new JButton(new ImageIcon("icons/insert.png"));
        reservebutton.addActionListener(new ActionListener(){

            @Override
            //με την μέθοδο actionPerformed λέμε στο κουμπί τι διαιδκασίες πρέπει να κανει στο πρόγραμμα
            public void actionPerformed(ActionEvent e) {
                try{
                    String departselected = ((JToggleButton.ToggleButtonModel)departgroup.getSelection()).getActionCommand();
                    String returnselected = ((JToggleButton.ToggleButtonModel)returngroup.getSelection()).getActionCommand();
                
                    int epivates = Integer.valueOf((String)seatsbox.getSelectedItem());
                    float price = ac.look_op.reserveFlights(Integer.valueOf(departselected), Integer.valueOf(returnselected),epivates);
                    ac.resultspanel.showResults(price);
                    ac.tabbedPane.setSelectedIndex(2);
                }catch(java.lang.NullPointerException ex){
                    JOptionPane.showMessageDialog(ac,  "Πρέπει να επιλεγούν πτήσεις και αναχώρησης και επιστροφής","Μη επιλογή πτήσης",JOptionPane.INFORMATION_MESSAGE );
                
                } catch (RemoteException ex) {
                    Logger.getLogger(ReservePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        });
        this.add(reservebutton);
        
        this.revalidate();
    }
                
    
}
