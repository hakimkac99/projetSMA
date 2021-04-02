package Agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import main.VirtualMachine;

public class AgentReceveur extends Agent {
    VirtualMachine machine;

    @Override
    protected void setup() {
        //Recupérer l'argument (La machine)
        Object[] args = getArguments();
        if (args != null) {
            machine = (VirtualMachine) args[0];
        }

        System.out.println(getLocalName()+" : Début d'exécution");

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                //Attendre un message
                ACLMessage msgRec = myAgent.blockingReceive();
                if (msgRec != null) {
                    // Message of agentMobile received. Process it
                    String replyMessage = "";
                    String typeSearch = msgRec.getContent(); //Type à chercher par l'agent mobile
                    //System.out.println(getLocalName()+" ; "+typeSearch+" ; "+machine.getDataSource());
                    if(typeSearch.equals(machine.getDataSource())){

                        System.out.println(getLocalName()+" : source de donnée trouvée");
                        replyMessage = "Found";

                    }else{
                        System.out.println(getLocalName()+" : source de donnée non trouvée");
                        replyMessage = "NotFound";
                    }

                    ACLMessage reply = msgRec.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent(replyMessage);
                    send(reply);

                }else{
                    block();
                }

            }
        });

    }
}
