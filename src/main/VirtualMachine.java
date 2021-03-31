package main;

import Agents.AgentAiguilleur;
import Agents.AgentCompteur;
import Agents.AgentReceveur;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;

public class VirtualMachine {
    int id;
    AgentContainer agentContainer;
    ArrayList<VirtualMachine> pred;
    ArrayList<VirtualMachine> succ;
    DataSource dataSource = null;

    /*AgentAiguilleur agentAiguilleur=null;
    AgentCompteur agentCompteur=null;
    AgentReceveur agentReceveur=null;*/

    public VirtualMachine(int id) {
        this.id = id;
        pred = new ArrayList<>();
        succ = new ArrayList<>();

        //Create Container
        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl(false);
        profile.setParameter(Profile.MAIN_HOST,"localhost");
        agentContainer = runtime.createAgentContainer(profile);
    }

    public void AddAgentAiguilleur(){
        try {
            AgentController agentController = agentContainer.createNewAgent("Agent_Aiguilleur_"+(id+1),"Agents.AgentAiguilleur",new Object[]{this});
            agentController.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AddAgentCompteur(){
        try {
            AgentController agentController = agentContainer.createNewAgent("Agent_Compteur_"+(id+1),"Agents.AgentCompteur",new Object[]{this});
            agentController.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AddAgentReceveur(){
        try {
            AgentController agentController = agentContainer.createNewAgent("Agent_Receveur_"+(id+1),"Agents.AgentReceveur",new Object[]{this});
            agentController.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AddAgentLanceur(){
        try {
            AgentController agentController = agentContainer.createNewAgent("Agent_Lanceur_"+(id+1),"Agents.AgentLanceur",new Object[]{this});
            agentController.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void AddAgentMobile(String typeSourceSearch){
        try {
            AgentController agentController = agentContainer.createNewAgent("Agent_Mobile_"+(id+1),"Agents.AgentMobile",new Object[]{id,typeSourceSearch});
            agentController.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addDataSource(String type){
        dataSource = new DataSource(type);
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
