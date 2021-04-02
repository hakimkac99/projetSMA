package Agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import main.VirtualMachine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.jar.JarInputStream;

public class AgentLanceur extends Agent {
    VirtualMachine machine;
    String typeSearch = null; //Source à chercher
    JFrame f;
    int indexLabel=0; //for showing results to user
    int iteration=0;
    ArrayList<JLabel> guiLogs;
    JPanel logPanel = null;

    @Override
    protected void setup() {
        System.out.println(getLocalName()+" : Début d'exécution");



        //Recupérer l'argument (La machine)
        Object[] args = getArguments();
        if (args != null) {
            machine = (VirtualMachine) args[0];



            if(machine.getId()==8) {
                initGUI();

            }
        }

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {

                while (typeSearch==null) {
                    System.out.print("");
                }

                addLabelToGUI("Itération "+(iteration+1));
                System.out.println("Itération "+(iteration+1));
                //Attendre un message
                ACLMessage msgRec = blockingReceive();
                if (msgRec != null) {
                    switch (msgRec.getContent()){
                        case "FOUND":
                            System.out.println(getLocalName()+" : "+msgRec.getSender().getLocalName()+" a trouvé la source de donnée");

                            addLabelToGUI("Source de données trouvée");
                            typeSearch=null;
                            indexLabel=0;
                            iteration=0;
                            break;

                        case "NOTFOUND":
                            iteration++;
                            System.out.println(getLocalName()+" : "+msgRec.getSender().getLocalName()+" n'a pas trouvé la source de donnée");

                            addLabelToGUI("Source de données non trouvée");

                            //Lancer un autre agent
                            System.out.println(getLocalName()+" : "+msgRec.getSender().getLocalName()+" Lancer un autre agent mobile pour faire la recherche");
                            machine.AddAgentMobile(typeSearch);
                            break;
                    }

                }

            }
        });



    }

    private void initGUI(){
        ArrayList<JLabel> guiLogs = new ArrayList<>();

        f=new JFrame();

        JTextField tf = new JTextField();
        tf.setBounds(85,70,200, 40);
        tf.setFont(new Font("SansSerif", Font.BOLD, 20));
        tf.setHorizontalAlignment(JTextField.CENTER);

        JButton b=new JButton("Chercher");//creating instance of JButton
        b.setBounds(130,130,110, 40);//x axis, y axis, width, height
        b.setFont(new Font("SansSerif", Font.BOLD, 15));

        JLabel l = new JLabel(getLocalName());
        l.setBounds(0,10,400, 40);
        l.setFont(new Font("SansSerif", Font.BOLD, 20));
        l.setHorizontalAlignment(JTextField.CENTER);





        f.add(tf);
        f.add(b);//adding button in JFrame
        f.add(l);

        f.setSize(400,500);//400 width and 500 height
        f.setLayout(null);//using no layout managers
        f.setVisible(true);//making the frame visible

        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Clear reslut logs in GUI

                if(logPanel!=null){
                    f.remove(logPanel);
                    f.repaint();
                }

                logPanel=new JPanel();
                logPanel.setBounds(0,200,400,250);
                f.add(logPanel);


                typeSearch = tf.getText();

                machine.AddAgentMobile(typeSearch);

            }
        });
    }

    void addLabelToGUI(String text){
        indexLabel++;

        JLabel l = new JLabel(text);
        l.setBounds(0,(indexLabel*20),400, 40);
        l.setForeground(Color.BLUE);
        l.setFont(new Font("SansSerif", Font.BOLD, 15));
        l.setHorizontalAlignment(JTextField.CENTER);
        logPanel.add(l);


        f.repaint();


    }

}
