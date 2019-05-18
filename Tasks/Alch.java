package Tasks;

import main.Main;
import org.osbot.rs07.api.ui.Spells;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.input.mouse.InventorySlotDestination;
import org.osbot.rs07.utility.Condition;

import java.awt.*;
import java.util.ArrayList;

public class Alch extends Task {

    public enum AlchMode {High, Low}
    private AlchMode mode;

    private int  alchItemID;

    private int[] itemIDQueue;

    private int alchsCast = 0;
    private int alchsRemaining = 0;

    public Alch(Main m, AlchMode alchMode, int[] delay){
        super(m, delay);
        mode = alchMode;
        if(!getBot().getTabs().getOpen().equals(Tab.INVENTORY)){
            getBot().getTabs().open(Tab.INVENTORY);
        }
    }

    @Override
    public void runTask() throws InterruptedException{
        if(!getBot().myPlayer().isAnimating()){
            if(canCastAlch()){
                if(hasCurrentAlchingItem()){
                    if(isItemInCorrectPosition()){
                        alchItem();
                    }else{
                        placeItemInCorrectPosition();
                    }
                }else{
                    finishItemInQueue();
                }
            } else{
                setExitMessage("LxMagic is unable to cast the appropriate spell and has stopped");
                getBot().stop(false);
            }
        }
    }

    private boolean hasCurrentAlchingItem(){
        return getBot().getInventory().contains(alchItemID);
    }

    private boolean canCastAlch() throws InterruptedException{
        switch(mode){
            case High:
                return getBot().getMagic().canCast(Spells.NormalSpells.HIGH_LEVEL_ALCHEMY, false);
            case Low:
                return getBot().getMagic().canCast(Spells.NormalSpells.LOW_LEVEL_ALCHEMY, false);
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
        if(!getBot().getTabs().isOpen(Tab.INVENTORY)){
            getBot().getTabs().open(Tab.INVENTORY);
        }
        getBot().getMouse().continualClick(new InventorySlotDestination(getBot().getBot(), getBot().getInventory().getSlot(alchItemID)), new Condition() {
            @Override
            public boolean evaluate() {
                getBot().getMouse().move(new InventorySlotDestination(getBot().getBot(), getCorrectAlchItemSlot()), true);
                return getBot().getInventory().getSlotBoundingBox(getCorrectAlchItemSlot()).contains(getBot().getMouse().getPosition());
            }
        });
        sleep(5000, isItemInCorrectPosition());
    }

    private void alchItem(){
        long l = getBot().getInventory().getAmount(alchItemID);
        Spells.NormalSpells s = Spells.NormalSpells.HIGH_LEVEL_ALCHEMY;
        if(mode == AlchMode.Low) {
            s = Spells.NormalSpells.LOW_LEVEL_ALCHEMY;
        }
        if(getBot().getMagic().castSpell(s)) {
            sleep(2000, getBot().getTabs().getOpen().equals(Tab.INVENTORY));
        }
        if(getBot().getMouse().click(false)) {
            sleep(2000, getBot().getTabs().getOpen().equals(Tab.MAGIC));
        }
        int j = 0;
        for(int i : itemIDQueue){
            j += getBot().getInventory().getItem(i).getAmount();
        }
        if(l != getBot().getInventory().getAmount(alchItemID)) {
            alchsCast++;
        }
        alchsRemaining = j;
    }

    private void setAlchItemID(int i){
        alchItemID = i;
    }

    public void createItemQueue(ArrayList<Integer> itemList) {
        itemIDQueue = new int[itemList.size()];
        for (int i = 0; i < itemIDQueue.length; i++) {
            itemIDQueue[i] = itemList.get(i);
        }
        setAlchItemID(itemIDQueue[0]);
    }

    private int[] getUpdatedQueue(){
        int[] newQ = new int[itemIDQueue.length - 1];
        System.arraycopy(itemIDQueue, 1, newQ, 0, newQ.length);
        return newQ;
    }

    private void finishItemInQueue(){
        if(itemIDQueue.length > 1) {
            itemIDQueue = getUpdatedQueue();
            setAlchItemID(itemIDQueue[0]);
            placeItemInCorrectPosition();
        } else{
            setExitMessage("LxMagic has finished alching!\n" + alchsCast + " alchs were cast,\nfor a total of " + alchsCast * 65 + "xp");
            getBot().stop(false);
        }
    }

    private String getPresetName(int i){
        switch (i){
            case 860:
                return "Magic Longbows";
            case 856:
                return "Yew Longbows";
            case 22267:
                return "Redwood Shields";
        }
        return "";
    }

    public void paintOverride(Graphics2D g){
        String s = getPresetName(alchItemID);
        if(s.equals("")){
            s = "ID #" + alchItemID;
        }
        g.drawString(String.format("Current Alch Item: %s", s), 10, 100);
        g.drawString(String.format("Alchs Completed: %d", alchsCast), 10, 120);
        g.drawString(String.format("Xp Earned: %d", alchsCast * 65), 10, 140);
        g.drawString(String.format("Alchs Remaining: %d", alchsRemaining), 10, 160);
    }

}
