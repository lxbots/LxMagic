package Tasks;

import main.Main;

public class SuperglassMake extends Task{

    public SuperglassMake(Main m) {
        super(m);
    }

    @Override
    public void runTask(){
        getBot().log("Running SGM");
    }
}
