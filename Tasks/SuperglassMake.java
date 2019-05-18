package Tasks;
import Exceptions.OutOfSuppliesException;
import main.Main;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.ui.Spells;
import org.osbot.rs07.input.mouse.BankSlotDestination;
import org.osbot.rs07.input.mouse.EntityDestination;

import java.awt.*;

public class SuperglassMake extends Task{

    enum Material {GiantSeaweed, Seaweed, SodaAsh, SwampWeed}

    private final int SAND_ID = 1783;
    private final int MOLTEN_GLASS_ID = 1775;
    private final int ASTRAL_ID = 9075;

    private Material material;

    public SuperglassMake(Main m, int[] delay) {
        super(m, delay);
    }

    @Override
    public void runTask() throws InterruptedException{
        getBot().log("Running SGM");
        if(!getBot().myPlayer().isAnimating()){
            if(hasSupplies()){
                if(getBot().getBank().isOpen()){
                    getBot().getBank().close();
                    sleep(2000, !getBot().getBank().isOpen());
                }
                if(getBot().getMagic().canCast(Spells.LunarSpells.SUPERGLASS_MAKE, false)){
                    makeGlass();
                } else{
                    setExitMessage("We are unable to cast Superglass make, stopping...");
                    getBot().stop(false);
                }
            } else{
                try {
                    getSupplies();
                } catch(OutOfSuppliesException e){}
            }
        }
    }

    private void makeGlass(){
        getBot().getMagic().castSpell(Spells.LunarSpells.SUPERGLASS_MAKE);
        sleep(5000, !getBot().myPlayer().isAnimating());
    }

    private void depositGlass(boolean hasExcess) throws InterruptedException{
        if(!getBot().getBank().isOpen()){
            getBot().getBank().open();
            sleep(2000, getBot().getBank().isOpen());
        }
        getBot().getBank().depositAllExcept(ASTRAL_ID);
        if(material.equals(Material.GiantSeaweed) && hasExcess){
            getBot().getBank().close();
            sleep(5000, !getBot().getBank().isOpen());
            while(getBot().getGroundItems().closest(MOLTEN_GLASS_ID) != null){
                pickupFloorGlass();
            }
        }
    }

    private void pickupFloorGlass() throws InterruptedException{
        getBot().log("Picking up floor glass...");
        GroundItem g = getBot().getGroundItems().closest(i -> i != null && i.getId() == MOLTEN_GLASS_ID);
        while(!getBot().getMouse().isOnCursor(g)){
            getBot().getMouse().move(new EntityDestination(getBot().getBot(), g));
        }
        while(g != null && g.getPosition().equals(getBot().myPosition())){
            getBot().getMouse().click(false);
            getBot().botSleep(10,50);
            g = getBot().getGroundItems().closest(i -> i != null && i.getId() == MOLTEN_GLASS_ID);
        }
    }

    private int getMaterialID(Material m){
        switch(m){
            //TODO: add IDS
            case Seaweed:
                return 401;
            case SodaAsh:
                return 1781;
            case SwampWeed:
                return 10978;
            case GiantSeaweed:
                return 21504;
        }
        return 0;
    }

    public void setMaterial(Material m){
        material = m;
    }

    private boolean hasSupplies(){
        if(material == null) return false;
        if(getBot().getInventory().contains(SAND_ID)){
            return getBot().getInventory().contains(getMaterialID(material));
        }
        return false;
    }

    private void getSupplies() throws InterruptedException, OutOfSuppliesException{
        if(getBot().getInventory().contains(MOLTEN_GLASS_ID)){
            depositGlass(getBot().getGroundItems().closest(MOLTEN_GLASS_ID) != null);
        }else {
            if (!getBot().getBank().isOpen()) {
                getBot().getBank().open();
            }
            sleep(10000, getBot().getBank().isOpen());
            if (!getBot().getBank().contains(SAND_ID, getMaterialID(material))) {
                throw new OutOfSuppliesException(getBot(), this);
            } else {
                if (material == Material.GiantSeaweed) {
                    getBot().getBank().withdraw(SAND_ID, 18);
                    while (getBot().getInventory().getItem(getMaterialID(material)) == null || getBot().getInventory().getAmount(getMaterialID(material)) < 3) {
                        getBot().getMouse().click(new BankSlotDestination(getBot().getBot(), getBot().getBank().getSlot(getMaterialID(material))));
                        getBot().botSleep(60, 160);
                        //getBot().getBank().withdraw(getMaterialID(material), 1);
                    }
                    getBot().botSleep(500, 800);
                    if (getBot().getInventory().getAmount(getMaterialID(material)) > 3) {
                        long l = getBot().getInventory().getAmount(getMaterialID(material)) - 3;
                        for (int i = 0; i < l; i++) {
                            getBot().getBank().deposit(getMaterialID(material), 1);
                        }
                    }
                } else {
                    getBot().getBank().withdraw(SAND_ID, 13);
                    getBot().getBank().withdraw(getMaterialID(material), 13);
                }
                if(!getBot().getInventory().contains(ASTRAL_ID)){
                    getBot().getBank().withdrawAll(ASTRAL_ID);
                }
            }
        }
    }

    @Override
    public void paintOverride(Graphics2D g) {

    }

    public Material getMaterialByName(String s){
        switch (s){
            case "Giant Seaweed":
                return Material.GiantSeaweed;
            case "Seaweed":
                return Material.Seaweed;
            case "Swamp Weed":
                return Material.SwampWeed;
            case "Soda Ash":
                return Material.SodaAsh;
        }
        return null;
    }
}
