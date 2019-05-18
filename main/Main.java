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
                return random(currentTask.getRandomReturnDelay()[0], currentTask.getRandomReturnDelay()[1]);
            }
        }
        return 100;
    }

    @Override
    public void onExit(){
        if(currentTask != null){
            currentTask.finishBot();
        }
    }

    Task setCurrentTask(Tasks t){
        currentTasksEnum = t;
        switch(t){
            case SuperglassMake:
                currentTask = new SuperglassMake(this, new int[]{200,300});
                break;
            case PlankMake:
                currentTask = new PlankMake(this, new int[]{200,300});
                break;
            case HighAlch:
                currentTask = new Alch(this, Alch.AlchMode.High, new int[]{250, 450});
                break;
            case LowAlch:
                currentTask = new Alch(this, Alch.AlchMode.Low, new int[]{250, 450});
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
        g.drawString("LxBots", 10, 40);
        g.drawString("LxMagic v0.1", 10, 60);
        g.drawString("Current Task: " + getTaskString(currentTasksEnum), 10, 80);
        if(currentTask != null){
            currentTask.paintOverride(g);
        }
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


