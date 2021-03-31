package Agents;

import jade.core.Agent;

public class AgentAiguilleur extends Agent {
    @Override
    protected void setup() {
        System.out.println("Je suis "+getLocalName());
    }
}
