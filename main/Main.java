package main;
import Tasks.*;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

import java.awt.*;

@ScriptManifest(name="LxMagic", author="Alex", info="An AIO Magic Trainer", version=0.1, logo="")
public class Main extends Script {

    enum Tasks {SuperglassMake, HighAlch, LowAlch, PlankMake}

    private Task currentTask;
    private Tasks currentTasksEnum;

    private boolean hasStarted = false;

    @Override
    public void onStart(){
        new Gui(this);
    }

    @Override
    public int onLoop() throws InterruptedException{
        if(hasStarted) {
            if (currentTask != null) {
                currentTask.runTask();
            }
        }
        return random(40, 100);
    }

    Task setCurrentTask(Tasks t){
        currentTasksEnum = t;
        log("Current task set to: " +t.toString());
        switch(t){
            case SuperglassMake:
                currentTask = new SuperglassMake(this);
                break;
            case PlankMake:
                currentTask = new PlankMake(this);
                break;
            case HighAlch:
                currentTask = new Alch(this, Alch.AlchMode.High);
                break;
            case LowAlch:
                currentTask = new Alch(this, Alch.AlchMode.Low);
                break;
        }
        return currentTask;
    }

    Task getCurrentTask(){
        return currentTask;
    }

    void startBot(){
        hasStarted = true;
    }

    @Override
    public void onPaint(Graphics2D g) {
        super.onPaint(g);
        g.drawString("Current Task: " + getTaskString(currentTasksEnum), 10, 40);
    }

    private String getTaskString(Tasks t){
        switch(t){
            case SuperglassMake:
                return "SuperGlass Make";
            case PlankMake:
                return "Plank Make";
            case LowAlch:
                return "Low Alch";
            case HighAlch:
                return "High Alch";

        }
        return "Null";
    }
}


