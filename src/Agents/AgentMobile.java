package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import main.VirtualMachine;

//Fourmi
public class AgentMobile extends Agent {

    int idMachineSource;
    int actualId;
    String typeSource;

    @Override
    protected void setup() {

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                System.out.println(getLocalName()+" : Début d'exécution");
                //Recupérer l'argument (La machine)
                Object[] args = getArguments();
                if (args != null) {
                    idMachineSource = (int) args[0];
                    typeSource = (String) args[1];
                    actualId = idMachineSource;

                    String receiver = "Agent_Aiguilleur_" + (actualId + 1);

                    System.out.println(getLocalName()+" : demande du meilleur chemin à l'"+receiver);

                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    msg.addReceiver(new AID(receiver, AID.ISLOCALNAME));
                    msg.setLanguage("English");
                    msg.setOntology("Search");
                    msg.setContent(typeSource);
                    send(msg);

                    //Recevoir le conteneur avec max pher
                    ACLMessage msgRec = blockingReceive();
                    if (msgRec != null) {
                        String container = msgRec.getContent();
                        System.out.println(getLocalName()+" : choisi "+container);
                    } else {
                        block();
                    }
                }

            }
        });
    }
}
