package Tasks;

import main.Main;
import org.osbot.rs07.api.ui.Spells;
import org.osbot.rs07.input.mouse.InventorySlotDestination;
import org.osbot.rs07.utility.Condition;
import org.osbot.rs07.utility.ConditionalSleep;

import java.util.ArrayList;
import java.util.Arrays;

public class Alch extends Task {

    public enum AlchMode {High, Low}
    private AlchMode mode;

    private int  alchItemID;

    private int[] itemIDQueue;

    public Alch(Main m, AlchMode alchMode){
        super(m);
        mode = alchMode;
    }

    @Override
    public void runTask() throws InterruptedException{
        getBot().log("Running task...");
        if(!getBot().myPlayer().isAnimating()){
            if(canCastAlch()){
                getBot().log("Can cast alch");
                if(hasCurrentAlchingItem()){
                    getBot().log("has current alch item");
                    if(isItemInCorrectPosition()){
                        getBot().log("item in correct position");
                        alchItem();
                    }else{
                        getBot().log("item in wrong position");
                        placeItemInCorrectPosition();
                    }
                }else{
                    getBot().log("current item not found");
                    finishItemInQueue();
                }
            }
        }
    }

    private boolean hasCurrentAlchingItem(){
        return getBot().getInventory().contains(alchItemID);
    }

    private boolean canCastAlch() throws InterruptedException{
        switch(mode){
            case High:
                return getBot().getMagic().canCast(Spells.NormalSpells.HIGH_LEVEL_ALCHEMY);
            case Low:
                return getBot().getMagic().canCast(Spells.NormalSpells.LOW_LEVEL_ALCHEMY);
        }
        return false;
    }

    private boolean isItemInCorrectPosition(){
        switch (mode){
            case High:
                return getBot().getInventory().getSlot(alchItemID) == 11;
            case Low:
                return getBot().getInventory().getSlot(alchItemID) == 3;
        }
        return false;
    }

    private int getCorrectAlchItemSlot(){
        switch (mode){
            case High:
                return 11;
            case Low:
                return 3;
        }
        return 0;
    }

    private void placeItemInCorrectPosition(){
        getBot().log("Trying to move item...");
        getBot().getMouse().continualClick(new InventorySlotDestination(getBot().getBot(), getBot().getInventory().getSlot(alchItemID)), new Condition() {
            @Override
            public boolean evaluate() {
                getBot().getMouse().move(new InventorySlotDestination(getBot().getBot(), getCorrectAlchItemSlot()), true);
                return getBot().getInventory().getSlotBoundingBox(getCorrectAlchItemSlot()).contains(getBot().getMouse().getPosition());
            }
        });
        new ConditionalSleep(5000){
            public boolean condition(){
                return isItemInCorrectPosition();
            }
        }.sleep();
    }

    private void alchItem(){
        getBot().log("Trying to alch item...");
        switch(mode){
            case High:
                break;
            case Low:
                break;
        }
    }

    private void setAlchItemID(int i){
        getBot().log("item id set to: " + i);
        alchItemID = i;
    }

    public void createItemQueue(ArrayList<Integer> itemList) {
        for (int i : itemList) {
            getBot().log(i);
        }
        itemIDQueue = new int[itemList.size()];
        for (int i = 0; i < itemIDQueue.length; i++) {
            itemIDQueue[i] = itemList.get(i);
        }
        setAlchItemID(itemIDQueue[0]);
    }

    private int[] getUpdatedQueue(){
        int[] newQ = new int[itemIDQueue.length - 1];
        System.arraycopy(itemIDQueue, 1, newQ, 0, newQ.length);
        getBot().log(Arrays.toString(newQ));
        return newQ;
    }

    private void finishItemInQueue(){
        if(itemIDQueue.length > 1) {
            itemIDQueue = getUpdatedQueue();
            setAlchItemID(itemIDQueue[0]);
        } else{
            finishBot("All Alchs finished successfully");
        }
    }

    private void finishBot(String exitMessage){
        getBot().log(exitMessage);
        getBot().stop(false);
    }
}
