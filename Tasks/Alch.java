package Tasks;

import main.Main;
import org.osbot.rs07.api.ui.Spells;
import org.osbot.rs07.api.ui.Tab;
import org.osbot.rs07.input.mouse.InventorySlotDestination;
import org.osbot.rs07.utility.Condition;
import org.osbot.rs07.utility.ConditionalSleep;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Alch extends Task {

    public enum AlchMode {High, Low}
    private AlchMode mode;

    private int  alchItemID;

    private int[] itemIDQueue;

    private int alchsCast = 0;

    public Alch(Main m, AlchMode alchMode, int[] delay){
        super(m, delay);
        mode = alchMode;
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
        Spells.NormalSpells s = Spells.NormalSpells.HIGH_LEVEL_ALCHEMY;
        if(mode == AlchMode.Low) {
            s = Spells.NormalSpells.LOW_LEVEL_ALCHEMY;
        }
        if(getBot().getMagic().castSpell(s)) {
            new ConditionalSleep(2000) {
                @Override
                public boolean condition(){
                    return getBot().getTabs().getOpen().equals(Tab.INVENTORY);
                }
            }.sleep();
        }
        if(getBot().getMouse().click(false)) {
            new ConditionalSleep(2000) {
                @Override
                public boolean condition(){
                    return getBot().getTabs().getOpen().equals(Tab.MAGIC);
                }
            }.sleep();
        }
        alchsCast++;
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
        getBot().log(Arrays.toString(newQ));
        return newQ;
    }

    private void finishItemInQueue(){
        if(itemIDQueue.length > 1) {
            itemIDQueue = getUpdatedQueue();
            setAlchItemID(itemIDQueue[0]);
        } else{
            finishBot("LxMagic has finished alching!\n" + alchsCast + " alchs were cast,\nfor a total of " + alchsCast * 65 + "xp");
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
    }

    private void finishBot(String exitMessage){
        getBot().log(exitMessage);
        getBot().stop(false);
    }
}
