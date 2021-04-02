package Agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ContainerID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import main.VirtualMachine;

import java.util.ArrayList;

//Fourmi
public class AgentMobile extends Agent {

    int idMachineSource;
    int actualId;
    String typeSource;
    ArrayList<String>chemin;
    String etat = "PROCESS"; // etat : {PROCESS,FOUND,NOTFOUND,IDLE}

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

        chemin = new ArrayList<>();


        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {



                    if (etat.equals("PROCESS")){
                        chemin.add("Container-" + actualId);


                        String receiver = "Agent_Aiguilleur_" + actualId;

                        System.out.println(getLocalName() + " : demande du meilleur chemin à l'" + receiver);

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
                            if (msgRec.getContent().equals("Source")) {
                                //if data source machine Cantact AgentReceveur
                                System.out.println(getLocalName() + " : Je suis à la source");
                                contactReceveur(this);

                            } else {
                                //Else (receive a container name) then move

                                containerName = msgRec.getContent();
                                System.out.println(getLocalName() + " : " + containerName + " choisi");
                                //mobility
                                ContainerID destination = new ContainerID();
                                // Initialize the destination object
                                destination.setName(containerName);
                                // Change of the agent state to move
                                actualId = Integer.valueOf(containerName.substring(10));

                                myAgent.doMove(destination);
                                System.out.println(getLocalName() + " : je suis à la machine " + containerName);
                            }
                        } else {
                            block();
                        }


                    }
                    if (etat.equals("FOUND")) {
                        //move back and increment pheromone

                        chemin.remove(chemin.size() - 1);

                        String containerName = chemin.get(chemin.size() - 1); //Last container (Conteneur précedant)

                        ContainerID destination = new ContainerID();
                        // Initialize the destination object

                        destination.setName(containerName);
                        // Change of the agent state to move
                        actualId = Integer.valueOf(containerName.substring(10));

                        doMove(destination);

                        if (chemin.size() > 1) {
                            System.out.println(getLocalName() + " : je suis à la machine " + containerName);
                            System.out.println(getLocalName()+" : Demander l'incrémentation du pheromone à l'Agent_Compteur_"+actualId);
                            contactCompteur("Inc",this);

                        } else {
                            System.out.println(getLocalName() + " : je suis revenue à la machine origine");
                            //Contact AgentLanceur

                            etat = "IDLE";
                        }

                    }
                    if (etat.equals("NOTFOUND")) {
                        //move back

                        chemin.remove(chemin.size() - 1);

                        String containerName = chemin.get(chemin.size() - 1); //Last container (Conteneur précedant)

                        ContainerID destination = new ContainerID();
                        // Initialize the destination object

                        destination.setName(containerName);
                        // Change of the agent state to move
                        actualId = Integer.valueOf(containerName.substring(10));

                        doMove(destination);

                        if (chemin.size() > 1) {
                            System.out.println(getLocalName() + " : je suis à la machine " + containerName);

                        } else {
                            System.out.println(getLocalName() + " : je suis revenue a la machine origine");

                            //Contact AgentLanceur
                            etat = "IDLE";
                        }

                    }
                    if (etat.equals("IDLE")){

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
        System.out.println("******");
        if (msgRec != null) {

            if(msgRec.getContent().equals("Found")){
                System.out.println(getLocalName()+" : source de donnée trouvée");
                //Incrémenter le phéromone
                System.out.println(getLocalName()+" : Demander l'incrémentation du pheromone à l'Agent_Compteur_"+actualId);
                contactCompteur("Inc",behaviour);
                etat = "FOUND";
            }else{
                System.out.println(getLocalName()+" : source de donnée non trouvée");
                //Decrémenter le pheromone
                System.out.println(getLocalName()+" : Demander la decrémentation du pheromone à l'Agent_Compteur_"+actualId);
                contactCompteur("Dec",behaviour);
                etat = "NOTFOUND";
            }

        }else{
            behaviour.block();
        }

    }

    private void contactCompteur(String action,CyclicBehaviour behaviour){
        //Action : Inc ou Dec le pher
        //Etape 1 : envoyer au compteur l'action Inc ou Dec
        //Etape 2 : recevoire d'un message "ActionOK"
        //Etape 3 : envoyer au compteur le type recherché
        //Etape 4 : recevoire d'un message "TypeOK"

        //Etape 1
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("Agent_Compteur_"+actualId, AID.ISLOCALNAME));
        msg.setLanguage("English");
        msg.setOntology("Search");
        msg.setContent(action);
        send(msg);
        System.out.println(getLocalName()+" : Envoie de l'action à l'Agent_Compteur_"+actualId);

        //Etape 2
        ACLMessage msgRec = null;
        while(msgRec==null) {

            msgRec = receive();
            if (msgRec != null) {
                if (msgRec.getContent().equals("ActionOK")) {
                    System.out.println(getLocalName() + " : Recevoir de message ActionOK de l'Agent_Compteur_" + actualId);
                    //Etape 3
                    ACLMessage msgType = new ACLMessage(ACLMessage.INFORM);
                    msgType.addReceiver(new AID("Agent_Compteur_" + actualId, AID.ISLOCALNAME));
                    msgType.setContent(typeSource);
                    send(msgType);
                    System.out.println(getLocalName() + " : Envoie de type de message à l'Agent_Compteur_" + actualId);

                    //Etape 4
                    msgRec =null;
                    while(msgRec==null) {
                        msgRec = receive();
                        if (msgRec != null) {
                            if (msgRec.getContent().equals("TypeOK")) {
                                System.out.println(getLocalName() + " : Recevoir de message TypeOK de l'Agent_Compteur_" + actualId);
                            }
                        }
                    }
                }
            }
        }
    }

}
