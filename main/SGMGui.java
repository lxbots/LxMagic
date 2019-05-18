package main;

import Tasks.SuperglassMake;
import Tasks.Task;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

class SGMGui extends OptionsGui {

    private JComboBox<String> materials;

    SGMGui(Main m, Gui g){
        super(m, g);

        setSize(400,280);
        setLocation(g.getSize().width + 10, g.getY());

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;

        JPanel a = new JPanel();
        a.setLayout(new GridLayout(0,2));
        JLabel l = new JLabel("Select Material used: ");
        l.setHorizontalAlignment(JLabel.CENTER);
        a.add(l);
        materials = new JComboBox<>(new String[]{"Giant Seaweed", "Seaweed", "Swamp Weed", "Soda Ash"});
        materials.setSelectedItem("Giant Seaweed");
        a.add(materials);

        add(a,c);

        c.gridy = 1;

        startButton = new JButton("Start");

        add(startButton, c);

    }

    @Override
    void sendStart() {
        Task t = getMain().setCurrentTask(Main.Tasks.SuperglassMake);
        if(t instanceof SuperglassMake){
            ((SuperglassMake) t).setMaterial(((SuperglassMake) t).getMaterialByName(materials.getSelectedItem().toString()));
        }
    }
}
