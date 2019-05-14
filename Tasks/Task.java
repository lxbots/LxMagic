package Tasks;
import main.Main;
public abstract class Task implements ITask{

    private Main main;

    Task(Main m){
        main = m;
    }

    Main getBot(){
        return main;
    }
}
