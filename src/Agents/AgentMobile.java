package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import main.VirtualMachine;

//Fourmi
public class AgentMobile extends Agent {

    int idMachineSource;
    int actualId;
    String typeSource;

    @Override
    protected void setup() {

        System.out.println("Je suis "+getLocalName());
        //Recupérer l'argument (La machine)
        Object[] args = getArguments();
        if (args != null) {
            idMachineSource = (int) args[0];
            typeSource = (String) args[1];
            actualId = idMachineSource;


            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new AID("Agent_Aiguilleur_"+(actualId+1), AID.ISLOCALNAME));
            msg.setLanguage("English");
            msg.setOntology("Search");
            msg.setContent(typeSource);
            send(msg);
        }


    }
}
