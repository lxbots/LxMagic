package Tasks;

import java.awt.*;

interface ITask {

    void runTask() throws InterruptedException;

    void paintOverride(Graphics2D g);

}
