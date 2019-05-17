package Tasks;
import main.Main;

import java.awt.*;

public class PlankMake extends Task {

    public PlankMake(Main m, int[] delay){
        super(m, delay);
    }

    @Override
    public void runTask(){
        getBot().log("Running Plank Make...");
    }

    @Override
    public void paintOverride(Graphics2D g) {

    }

}
