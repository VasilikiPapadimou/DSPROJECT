//icsd14151 Vasiliki Papadimou
//icsd16085 Maria Lalakou
package client;

import java.awt.event.*;
import javax.swing.*;

public class MainMenu  extends JMenuBar{
    AgentClient ac; 
    MainMenu(AgentClient ac){
        this.ac = ac;
        JMenu menu = new JMenu("Επιλογές");//"Δημιουργία" drop down
        
                
        //Υλοποίηση Menu About
        JMenuItem aboutmi = new JMenuItem("Σχετικά");
        //Πραγματοποιείται μία ενέργεια και "ακούγεται" κάπου κάνοντας κάτι
        aboutmi.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(ac,"icsd14151 Vasiliki Papadimou\n icsd16085 Maria Lalakou","Σχετικά",JOptionPane.INFORMATION_MESSAGE );
            }
        });
        menu.add(aboutmi);

        //Υλοποίηση Menu Choice Exit
        JMenuItem exitmi = new JMenuItem("Έξοδος");
        exitmi.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
                System.exit(0);
            }
        });
        //προσθήκη του drop down menu
        menu.add(exitmi);
        this.add(menu);
    }
}
