package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import main.VirtualMachine;

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

                ACLMessage msg = myAgent.receive();
                if (msg != null) {
                    // Message received. Process it
                    String typeSource = msg.getContent();
                    //ACLMessage reply = msg.createReply();

                    //Contacter les agents compteurs des machines successeurs
                    String container =  contactCompteurs(typeSource);

                    //Renvoyer le container avex max pher à l'agent mobile

                }

            }
        });

    }

    String contactCompteurs(String typeSource){
    //Return the container name of the machine of max pheromone

        //Envoie aux agents compteurs
        for (VirtualMachine machineSucc : machine.getSucc()) {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            System.out.println("Envoie du message à : "+"Agent_Compteur_"+(machineSucc.getId()+1));
            msg.addReceiver(new AID("Agent_Compteur_"+(machineSucc.getId()+1), AID.ISLOCALNAME));
            msg.setLanguage("English");
            msg.setOntology("Search");
            msg.setContent("type "+typeSource);
            send(msg);

            //Attendre une réponse
            ACLMessage msgRec = this.receive();
            if (msgRec != null) {
                // Message received. Process it
                int pher = Integer.valueOf(msgRec.getContent());
                System.out.println(pher);
            }
        }



        return null;
    }


}
