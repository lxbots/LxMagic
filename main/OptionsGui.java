package main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

abstract class OptionsGui extends JFrame implements ActionListener {

    private Main main;
    private Gui gui;

    JButton startButton;

    OptionsGui(Main m, Gui g){
        main = m;
        gui = g;
    }

    Main getMain(){
        return main;
    }

    Gui getGui(){
        return gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == startButton){
            sendStart();
        }
    }

    abstract void sendStart();

}

