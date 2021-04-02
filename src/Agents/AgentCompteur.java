package Agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashMap;
import java.util.Map;

public class AgentCompteur extends Agent {
    Map<String, Integer> pheromone;

    @Override
    protected void setup() {
        System.out.println(getLocalName()+" : Début d'exécution");

        pheromone = new HashMap<>();
        pheromone.put("Image",0);
        pheromone.put("Texte",0);


        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {

                //Attendre un message
                ACLMessage msgRec = myAgent.blockingReceive();
                if (msgRec != null) {
                    // Message received. Process it

                    if(msgRec.getContent().contains("type ")){
                        //Get Pheromone
                        String typeSource = msgRec.getContent().substring(5);
                        //retourner le pheromone
                        ACLMessage reply = msgRec.createReply();
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContent(String.valueOf(getPheromone(typeSource)));
                        send(reply);
                        System.out.println(getLocalName()+" : Valeur de phéromone "+getPheromone(typeSource));

                    }else {
                        //Inc ou Dec
                        String action = msgRec.getContent();

                        ACLMessage reply = msgRec.createReply();
                        reply.setPerformative(ACLMessage.INFORM);

                        reply.setContent("ActionOK");

                        send(reply);

                        // recevoir le type
                        String typeSearch="";
                        ACLMessage msgType = myAgent.blockingReceive();
                        if (msgType != null) {
                            typeSearch = msgType.getContent();
                            if (action.equals("Inc"))
                                incPheromone(typeSearch);
                            else
                                decPheromone(typeSearch);

                            reply.setContent("TypeOK");
                            send(reply);

                            System.out.println(getLocalName()+" : Nouvelle valeur du phéromone "+getPheromone(typeSearch));
                        }else{
                            block();
                        }

                    }

                }
            }
        });



    }

    public void incPheromone(String type){
        pheromone.put(type,(pheromone.get(type)+1));
    }

    public void decPheromone(String type){
        pheromone.put(type,(pheromone.get(type)-1));
    }

    public int getPheromone(String type){
        return pheromone.get(type);
    }
}
