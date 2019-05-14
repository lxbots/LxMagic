package main;
import Tasks.Alch;
import Tasks.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class InnerGui extends JFrame implements ActionListener {

    private Main main;
    private JButton startButton;

    private JComboBox[] comboBoxes;

    private String taskString;

    private JPanel mainPanel;

    InnerGui(Main m, JFrame mainFrame, String _taskString){
        main = m;


        setLayout(new BorderLayout());

        taskString = _taskString;

        switch (_taskString){
            case "Low Alchemy":
            case "High Alchemy":
                mainPanel = makeAlchPanel(false);
                add(mainPanel, BorderLayout.CENTER);
                break;

        }

        startButton = new JButton("Start");
        startButton.addActionListener(this);
        add(startButton,BorderLayout.PAGE_END);


        setLocation(mainFrame.getSize().width + 10, mainFrame.getY());
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == startButton){
            Task t = main.setCurrentTask(setTask());
            if(t instanceof Alch){
                ((Alch) t).setAlchItemID(getItemIDFromString(comboBoxes[0].getSelectedItem().toString()));
            }
            main.startBot();
        } else if(comboBoxes[0] != null && e.getSource() == comboBoxes[0] && comboBoxes[0].getSelectedItem() != null){
            main.log("Combo box interacted");
            switch(taskString){
                case "Low Alchemy":
                case "High Alchemy":
                    if(comboBoxes[0].getSelectedItem().equals("Other")) {
                        main.log("remaking panel...");
                        mainPanel = makeAlchPanel(true);
                        add(mainPanel, BorderLayout.CENTER);
                    }
                    break;
                }
            }
    }

    private JPanel makeAlchPanel(boolean usingOtherID){
        if(mainPanel != null){
            remove(mainPanel);
        }
        JPanel p = new JPanel();
        p.setBackground(Color.DARK_GRAY);
        setSize(400,150);
        if(usingOtherID){
            p.setLayout(new GridLayout(0,3));
        } else{
            p.setLayout(new GridLayout(0,2));
        }
        JLabel l = new JLabel("Select Item To Alch: ");
        l.setBackground(Color.white);
        l.setHorizontalAlignment(JLabel.CENTER);
        p.add(l);
        comboBoxes = new JComboBox[1];
        comboBoxes[0] = new JComboBox<>(new String[]{"Yew Longbows", "Redwood Shields","Magic Longbows", "Other"});
        comboBoxes[0].addActionListener(this);
        p.add(comboBoxes[0]);
        if(usingOtherID){
            comboBoxes[0].setSelectedItem("Other");
            JTextField t = new JTextField();
            t.setToolTipText("Enter ID...");
            p.add(t);
        }
        return p;
    }

    private Main.Tasks setTask(){
        switch (taskString){
            case "Low Alchemy":
                return Main.Tasks.LowAlch;
            case "High Alchemy":
                return Main.Tasks.HighAlch;
            case "Superglass Make":
                return Main.Tasks.SuperglassMake;
            case "Plank Make":
                return Main.Tasks.PlankMake;
        }
        return null;
    }

    private int getItemIDFromString(String s){
        switch (s){
            case "Redwood Shields":
                return 22267;
            case "Yew Longbows":
                return 856;
            case "Other":
                //id = Integer.parseInt(idField.getText());
                return 0;
            case "Magic Longbows":
                return 860;
        }
        return 0;
    }
}
