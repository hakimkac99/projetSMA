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
        pheromone = new HashMap<>();
        pheromone.put("image",0);
        pheromone.put("texte",0);

        System.out.println("Je suis "+getLocalName());

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                //Attendre un message
                ACLMessage msgRec = myAgent.receive();
                if (msgRec != null) {
                    // Message received. Process it
                    System.out.println(msgRec.getContent());


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
