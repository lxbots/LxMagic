package Tasks;

import main.Main;
import org.osbot.rs07.api.ui.Spells;
import org.osbot.rs07.input.mouse.InventorySlotDestination;

import java.util.ArrayList;

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
        if(!getBot().myPlayer().isAnimating()){
            if(canCastAlch()){
                getBot().log(isItemInCorrectPosition());
                if(isItemInCorrectPosition()) {
                    AlchItem();
                } else{
                    getBot().log("Moving item...");
                    placeItemInCorrectPosition();
                }
            }
        }
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
                return getBot().getInventory().getSlot(alchItemID) == 12;
            case Low:
                return getBot().getInventory().getSlot(alchItemID) == 4;
        }
        return false;
    }

    private int getCorrectAlchItemSlot(){
        switch (mode){
            case High:
                return 12;
            case Low:
                return 4;
        }
        return 0;
    }

    private void placeItemInCorrectPosition(){
        getBot().getMouse().move(new InventorySlotDestination(getBot().getBot(),getBot().getInventory().getSlot(alchItemID)));
        getBot().getMouse().move(new InventorySlotDestination(getBot().getBot(), getCorrectAlchItemSlot()), true);

    }

    private void AlchItem(){
        switch(mode){
            case High:
                getBot().log("Trying to alch item...");

        }
    }

    public void setAlchItemID(int i){
        getBot().log("item id set to: " + i);
        alchItemID = i;
    }

    void createItemQueue(ArrayList<Integer> itemList){
        itemIDQueue = new int[itemList.size()];
        for(int i = 0; i < itemIDQueue.length; i++){
            itemIDQueue[i] = itemList.get(i);
        }
    }

    int[] getItemIDQueue(){
        return itemIDQueue;
    }

    private int[] getUpdatedQueue(){
        int[] newQ = new int[itemIDQueue.length - 1];
        for(int i = 0; i < newQ.length; i++){
            newQ[i] = itemIDQueue[i + 1];
        }
        return newQ;
    }

    private void finishItemInQueue(){
        itemIDQueue = getUpdatedQueue();
    }
}
