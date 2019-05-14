package Tasks;

import main.Main;

public class PlankMake extends Task {

    public PlankMake(Main m){
        super(m);
    }

    @Override
    public void runTask(){
        getBot().log("Running Plank Make...");
    }

}
