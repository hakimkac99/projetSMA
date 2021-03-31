package main;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;

import java.util.ArrayList;

public class MainContainer {

    public static void main(String[] args) {
        try {
            Runtime runtime = Runtime.instance();
            Properties properties = new ExtendedProperties();
            properties.setProperty(Profile.GUI, "true");
            Profile profile = new ProfileImpl(properties);
            AgentContainer mainContainer = runtime.createMainContainer(profile);
            mainContainer.start();

            createNetwork();

        }catch(ControllerException e){
            e.printStackTrace();
        }
    }

    public static void createNetwork(){
        //Create network
        ArrayList<VirtualMachine> network = new ArrayList();

        for (int i=0;i<9;i++) {
            VirtualMachine machine = new VirtualMachine(i);
            network.add(machine);
        }

        network.get(0).pred.add(network.get(2));
        network.get(0).pred.add(network.get(3));
        network.get(0).pred.add(network.get(4));
        network.get(0).succ = null;


        network.get(1).pred.add(network.get(2));
        network.get(1).pred.add(network.get(3));
        network.get(1).pred.add(network.get(4));
        network.get(1).succ = null;

        network.get(2).pred.add(network.get(5));
        network.get(2).pred.add(network.get(6));
        network.get(2).succ.add(network.get(0));
        network.get(2).succ.add(network.get(1));

        network.get(3).pred.add(network.get(5));
        network.get(3).pred.add(network.get(6));
        network.get(3).pred.add(network.get(7));
        network.get(3).pred.add(network.get(8));
        network.get(3).succ.add(network.get(0));
        network.get(3).succ.add(network.get(1));

        network.get(4).pred.add(network.get(7));
        network.get(4).pred.add(network.get(8));
        network.get(4).succ.add(network.get(0));
        network.get(4).succ.add(network.get(1));

        network.get(5).succ.add(network.get(2));
        network.get(5).succ.add(network.get(3));
        network.get(5).pred = null;

        network.get(6).succ.add(network.get(2));
        network.get(6).succ.add(network.get(3));
        network.get(5).pred = null;

        network.get(7).succ.add(network.get(3));
        network.get(7).succ.add(network.get(4));
        network.get(5).pred = null;

        network.get(8).succ.add(network.get(3));
        network.get(8).succ.add(network.get(4));
        network.get(5).pred = null;

        startNetwork(network);
    }

    public static void startNetwork(ArrayList<VirtualMachine> network){
        for (VirtualMachine machine : network){

            machine.AddAgentAiguilleur();
            machine.AddAgentCompteur();
            if (machine.getSucc() == null)
                machine.AddAgentReceveur();
        }
    }
}
