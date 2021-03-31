package main;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;

import java.util.ArrayList;

public class VirtualMachine {
    int id;
    AgentContainer agentContainer;
    ArrayList<VirtualMachine> pred;
    ArrayList<VirtualMachine> succ;

    public VirtualMachine(int id) {
        this.id = id;
        pred = new ArrayList<>();
        succ = new ArrayList<>();
        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl(false);
        profile.setParameter(Profile.MAIN_HOST,"localhost");
        agentContainer = runtime.createAgentContainer(profile);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AgentContainer getAgentContainer() {
        return agentContainer;
    }


    public ArrayList<VirtualMachine> getPred() {
        return pred;
    }

    public void setPred(ArrayList<VirtualMachine> pred) {
        this.pred = pred;
    }

    public ArrayList<VirtualMachine> getSucc() {
        return succ;
    }

    public void setSucc(ArrayList<VirtualMachine> succ) {
        this.succ = succ;
    }
}
