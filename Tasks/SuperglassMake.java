package Tasks;
import main.Main;
import java.awt.*;

public class SuperglassMake extends Task{

    public SuperglassMake(Main m, int[] delay) {
        super(m, delay);
    }

    @Override
    public void runTask(){
        getBot().log("Running SGM");
    }

    @Override
    public void paintOverride(Graphics2D g) {

    }
}
