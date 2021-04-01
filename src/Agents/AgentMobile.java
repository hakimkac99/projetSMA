package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import main.VirtualMachine;

//Fourmi
public class AgentMobile extends Agent {

    int idMachineSource;
    int actualId;
    String typeSource;

    @Override
    protected void setup() {
        System.out.println(getLocalName()+" : Début d'exécution");
        //Recupérer l'argument (La machine)
        Object[] args = getArguments();
        if (args != null) {
            idMachineSource = (int) args[0]+1;
            typeSource = (String) args[1];
            actualId = idMachineSource;
        }
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {



                    String receiver = "Agent_Aiguilleur_" + actualId ;

                    System.out.println(getLocalName()+" : demande du meilleur chemin à l'"+receiver);

                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    msg.addReceiver(new AID(receiver, AID.ISLOCALNAME));
                    msg.setLanguage("English");
                    msg.setOntology("Search");
                    msg.setContent(typeSource);
                    send(msg);

                    //Recevoir le conteneur avec max pher
                    String containerName = null;
                    ACLMessage msgRec = blockingReceive();
                    if (msgRec != null) {
                        if (msgRec.getContent().equals("Source")){
                            //if data source machine Cantact AgentReceveur
                            System.out.println(getLocalName()+" : Je suis à la source");
                            contactReceveur(this);

                        }else{
                            //Else (receive a container name) then move

                            containerName = msgRec.getContent();
                            System.out.println(getLocalName()+" : "+containerName+" choisi");
                            //mobility
                            ContainerID destination = new ContainerID();
                            // Initialize the destination object
                            destination.setName(containerName);
                            // Change of the agent state to move
                            actualId = Integer.valueOf(containerName.substring(10));

                            myAgent.doMove(destination);
                        }
                    } else {
                        block();
                    }


                }


        });
    }

    private void contactReceveur(CyclicBehaviour behaviour){
        System.out.println(getLocalName()+" : Cantacter AgentReceveur_"+actualId);

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);

        msg.addReceiver(new AID("Agent_Receveur_"+actualId, AID.ISLOCALNAME));
        msg.setLanguage("English");
        msg.setOntology("Search");
        msg.setContent(typeSource);
        send(msg);

        //Attendre un message
        ACLMessage msgRec = blockingReceive();
        if (msgRec != null) {

            if(msgRec.getContent().equals("Found")){
                System.out.println(getLocalName()+" source de donnée trouvée");

            }else{
                System.out.println(getLocalName()+" source de donnée trouvée");

            }

        }else{
            behaviour.block();
        }

    }

}
