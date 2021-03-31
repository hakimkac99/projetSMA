package Agents;

import jade.core.Agent;
import main.VirtualMachine;

public class AgentLanceur extends Agent {
    VirtualMachine machine;

    @Override
    protected void setup() {
        System.out.println("Je suis "+getLocalName());

        //Recupérer l'argument (La machine)
        Object[] args = getArguments();
        if (args != null) {
            machine = (VirtualMachine) args[0];
            machine.AddAgentMobile("Image");
        }





    }
}
