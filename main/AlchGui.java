package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

class AlchGui extends OptionsGui implements FocusListener {


    private int[] alchIDS;

    private JComboBox<String> alchDropdown;
    private JTextField idText;

    private JButton addItemButton;

    private ArrayList<Integer> idList;

    private JLabel statusText;

    AlchGui(Main m, Gui g){
        super(m, g);
        idList = new ArrayList<>();
        setSize(400,280);
        setLocation(g.getSize().width + 10, g.getY());

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5,5,5,5);

        //create first panel
        JPanel a = new JPanel();
        c.weightx = 0.5;
        JLabel l = new JLabel("Select Items to Alch: ");
        a.add(l, c);
        c.gridx = 1;
        c.weightx = 1;
        alchDropdown = new JComboBox<>(new String[]{"-Select Item-","Yew Longbows", "Magic Longbows", "Other"});
        alchDropdown.addActionListener(this);
        a.add(alchDropdown, c);

        //create second panel
        c.gridx = 0;
        JPanel b = new JPanel();
        idText = new JTextField("Select item from dropdown menu...");
        idText.setPreferredSize(new Dimension(240, 30));
        idText.setEditable(false);
        idText.addFocusListener(this);
        b.add(idText, c);

        //create third panel
        c.weightx = 0.4;
        JPanel d = new JPanel();
        addItemButton = new JButton("Add");
        addItemButton.addActionListener(this);
        d.add(addItemButton, c);

        //create fourth panel
        c.weightx = 0.6;
        JPanel e = new JPanel();
        startButton = new JButton("Start");
        startButton.addActionListener(this);
        e.add(startButton);

        //create final panel
        JPanel f = new JPanel();
        statusText = new JLabel("");
        statusText.setHorizontalAlignment(JLabel.CENTER);
        c.weightx = 1;
        f.add(statusText, c);

        //Add panels to main frame
        c.gridx = 0;
        add(a, c);
        c.gridy = 1;
        add(b, c);
        c.gridy = 2;
        add(d,c);
        c.gridy = 3;
        add(e,c);
        c.gridy = 4;
        add(f,c);

        setVisible(true);
    }

    private int getPresetID(String s){
        switch (s){
            case "Redwood Shields":
                return 22267;
            case "Yew Longbows":
                return 856;
            case "Magic Longbows":
                return 860;
        }
        return 0;
    }

    @Override
    void sendStart(){

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if(e.getSource() == alchDropdown){
            if(alchDropdown.getSelectedItem() != null){
                if(!alchDropdown.getSelectedItem().equals("Other")) {
                    if(!alchDropdown.getSelectedItem().equals("-Select Item-")) {
                        idText.setText(alchDropdown.getSelectedItem().toString());
                        idText.setEditable(false);
                    }
                } else{
                    idText.setText("");
                    idText.setEditable(true);
                }
            }
        } else if(e.getSource() == addItemButton){
            if(alchDropdown.getSelectedItem() != null){
                if(isValidID(idText.getText())){

                }
            }
        }
    }

    private boolean isValidID(String s){
        if(getPresetID(s) != 0){
            return true;
        }else{
            try{
                Integer.parseInt(s);
                return true;
            }catch(NumberFormatException e){
                return false;
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e){
        if(e.getSource() == idText){
            if(alchDropdown.getSelectedItem() != null && alchDropdown.getSelectedItem().equals("Other")) {
                idText.setText("");
            }
        }
    }

    @Override
    public void focusLost(FocusEvent e){

    }

}