package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import main.VirtualMachine;

import java.util.HashMap;

public class AgentAiguilleur extends Agent {
    VirtualMachine machine;

    @Override
    protected void setup() {
        System.out.println("Je suis "+getLocalName());

        //Recupérer l'argument (La machine)
        Object[] args = getArguments();
        if (args != null) {
            machine = (VirtualMachine) args[0];
        }

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                //Recevoir le message de l'agent mobile : le type de source à chercher
                //Renvoyer la machine suivante

                ACLMessage msg = myAgent.blockingReceive();
                if (msg != null) {
                    System.out.println("Jani message : "+msg);
                    // Message received. Process it
                    String typeSource = msg.getContent();
                    //ACLMessage reply = msg.createReply();

                    //Contacter les agents compteurs des machines successeurs

                    String container =  contactCompteurs(typeSource,this);

                    //Renvoyer le container avec max pher à l'agent mobile
                    System.out.println(container);
                }
                else{
                    block();
                }

            }
        });

    }

    private String contactCompteurs(String typeSource,CyclicBehaviour behaviour){
    //Return the container name of the machine of max pheromone
        int idMachineMaxPher = 0;
        int maxPher = 0;

        //Envoie aux agents compteurs
        int i=0;
        for (VirtualMachine machineSucc : machine.getSucc()) {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

            msg.addReceiver(new AID("Agent_Compteur_"+(machineSucc.getId()+1), AID.ISLOCALNAME));
            msg.setLanguage("English");
            msg.setOntology("Search");
            msg.setContent("type "+typeSource);
            send(msg);

            System.out.println(getLocalName()+" ; Envoie du message à : "+"Agent_Compteur_"+(machineSucc.getId()+1));

            //Attendre une réponse

            ACLMessage msgRec = this.blockingReceive();

            if (msgRec != null) {
                // Message received. Process it
                int pher = Integer.valueOf(msgRec.getContent());
                System.out.println("Valeur du phéromone envoyé par le receveur "+(machineSucc.getId()+1)+" est : "+pher);

                //Select the max Pher
                if (i==0){
                    maxPher = pher;
                    idMachineMaxPher = machineSucc.getId()+1;
                } else {
                    if (pher > maxPher) {
                        maxPher = pher;
                        idMachineMaxPher = machineSucc.getId()+1;
                    }else if(pher == maxPher && Math.random()>0.5){ //Select randomly between two identical pheromone
                        maxPher = pher;
                        idMachineMaxPher = machineSucc.getId()+1;
                    }
                }

            }else{
                behaviour.block();
            }
            i++;
        }

        //return the name of the container whith max Pher

        return ("Container-"+idMachineMaxPher);
    }


}
