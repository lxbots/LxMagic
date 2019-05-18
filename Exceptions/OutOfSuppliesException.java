package Exceptions;

import Tasks.Task;
import main.Main;

public class OutOfSuppliesException extends Exception {

    public OutOfSuppliesException(Main m, Task t){
        super();
        t.setExitMessage("LxMagic is stopping due to lack of supplies...");
        m.stop(false);
    }

}
