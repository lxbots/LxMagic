package main;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gui extends JFrame implements ActionListener {

    private Main main;

    private JComboBox<String> spellbookCombo;
    private String[] NormalTaskOptions = new String[]{"Low Alchemy", "High Alchemy"};
    private String[] LunarTaskOptions = new String[]{"Plank Make", "Superglass Make"};
    private JComboBox<String> taskCombo;

    private JButton nextButton;

    Gui(Main m){
        main = m;
        setSize(250,200);
        setLocation(10,10);
        setTitle("LxMagic - GUI");

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 1;
        JPanel top = new JPanel();
        top.setLayout(new GridLayout(0,2));
        JLabel l = new JLabel("Select Spellbook: ");
        top.add(l);
        spellbookCombo = new JComboBox<>(new String[]{"Normal", "Lunar"});
        spellbookCombo.addActionListener(this);
        top.add(spellbookCombo);
        add(top,c);

        c.gridy = 1;
        JPanel bttm = new JPanel();
        bttm.setLayout(new GridLayout(0,2));
        l = new JLabel("Select Spell: ");
        bttm.add(l);
        taskCombo = new JComboBox<>(NormalTaskOptions);
        taskCombo.addActionListener(this);
        bttm.add(taskCombo);
        add(bttm,c);

        c.gridy = 2;
        c.weighty = 0.2;
        nextButton = new JButton("Next");
        nextButton.addActionListener(this);
        add(nextButton, c);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == spellbookCombo){
            if(spellbookCombo.getSelectedItem() != null) {
                taskCombo.removeAllItems();
                switch (spellbookCombo.getSelectedItem().toString()) {
                    case "Normal":
                        for (String s : NormalTaskOptions) {
                            taskCombo.addItem(s);
                        }
                        break;
                    case "Lunar":
                        for (String s : LunarTaskOptions) {
                            taskCombo.addItem(s);
                        }
                        break;
                }
            }
        } else if(e.getSource() == nextButton){
            if(taskCombo.getSelectedItem() != null) {
                new AlchGui(main, this);
            }
        }
    }

}
