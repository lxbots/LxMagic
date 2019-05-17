package Tasks;
import main.Main;

public abstract class Task implements ITask{

    private Main main;

    private int[] randomReturnDelay;

    private String exitMessage = "Thank you for using an LxBot!";

    Task(Main m, int[] delay){
        main = m;
        setRandomReturnDelay(delay);
    }

    Main getBot(){
        return main;
    }

    public int[] getRandomReturnDelay(){
        return randomReturnDelay;
    }

    private void setRandomReturnDelay(int[] delay){
        randomReturnDelay = new int[2];
        randomReturnDelay[0] = delay[0];
        randomReturnDelay[1] = delay[1];
    }

    public void finishBot(){
        getBot().log(exitMessage);
    }

    void setExitMessage(String s){
        exitMessage = s;
    }

}
