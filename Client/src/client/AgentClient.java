//icsd14151 Vasiliki Papadimou
//icsd16085 Maria Lalakou
package client;

import javax.swing.*;
import messages.RMIOperations;

public class AgentClient extends JFrame{
    SearchPanel searchpanel;
    ReservePanel reservepanel;
    ResultsPanel resultspanel;
    JTabbedPane tabbedPane;
    RMIOperations look_op;
    AgentClient(RMIOperations look_op){
        this.look_op = look_op;
        /*------------------------------------------------------------------------------------------------------------*/
        this.setTitle("ΣΥΣΤΥΜΑ ΚΡΑΤΗΣΕΩΝ");  //τίτλος παραθύρου      
        this.setVisible(true);//εμφάνιση του παραθύρου
        this.setSize(450,350);//μέγεθος παραθύρου
        
        MainMenu mb = new MainMenu(this); // Χώρος Main Menu (υλοποιηση στην MainMenu)
        this.setJMenuBar(mb);  
        //υλοποιήση των Tab
        tabbedPane = new JTabbedPane();
        this.add(tabbedPane); 
        
        searchpanel = new SearchPanel(this); 
        tabbedPane.add(searchpanel,"Αναζήτηση Πτήσης");
        
        reservepanel = new ReservePanel(this);
        tabbedPane.add(reservepanel,"Πληροφορίες Πτήσης");
        
        resultspanel = new ResultsPanel(this);
        tabbedPane.add(resultspanel,"Πληροφορίες Κράτησης");
        
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
