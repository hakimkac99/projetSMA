package Agents;

import jade.core.Agent;

public class AgentCompteur extends Agent {
    @Override
    protected void setup() {
        System.out.println("Je suis "+getLocalName());
    }
}
