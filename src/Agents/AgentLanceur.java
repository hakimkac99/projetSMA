package Agents;

import jade.core.Agent;

public class AgentLanceur extends Agent {
    @Override
    protected void setup() {
        System.out.println("Je suis "+getLocalName());
    }
}
