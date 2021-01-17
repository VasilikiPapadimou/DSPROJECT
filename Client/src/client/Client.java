//icsd14151 Vasiliki Papadimou
//icsd16085 Maria Lalakou
package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import messages.RMIOperations;

public class Client {

    public static void main(String[] args) {
        /****************** RMI Connection..*********************************/
        try {
            String name = "//localhost/OperationsServer"; //.. στον Operation server
            RMIOperations look_op =(RMIOperations)Naming.lookup(name);
            
            AgentClient agentclient = new AgentClient(look_op);
            
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
