package Tasks;
import main.Main;
import org.osbot.rs07.api.filter.Filter;
import org.osbot.rs07.api.model.GroundItem;
import org.osbot.rs07.api.ui.Spells;
import org.osbot.rs07.utility.ConditionalSleep;

import java.awt.*;

public class SuperglassMake extends Task{

    enum Material {GiantSeaweed, Seaweed, SodaAsh, SwampWeed}

    private final int SAND_ID = 1;
    private final int MOLTEN_GLASS_ID = 1;

    private Material material;

    public SuperglassMake(Main m, int[] delay) {
        super(m, delay);
    }

    @Override
    public void runTask() throws InterruptedException{
        getBot().log("Running SGM");
        if(!getBot().myPlayer().isAnimating()){
            if(hasSupplies()){
                if(getBot().getMagic().canCast(Spells.LunarSpells.SUPERGLASS_MAKE, false)){
                    makeGlass();
                }
            } else{
                getSupplies();
            }
        }
    }

    private void makeGlass(){
        getBot().getMagic().castSpell(Spells.LunarSpells.SUPERGLASS_MAKE);
        sleep(10000, getBot().getInventory().contains(MOLTEN_GLASS_ID));
    }

    private void depositGlass(){
        getBot().getBank().depositAll();
        if(material.equals(Material.GiantSeaweed)){
            getBot().getBank().close();

        }
    }

    private void pickupFloorGlass(){
        GroundItem g = getBot().getGroundItems().closest(i -> i != null && i.getId() == MOLTEN_GLASS_ID);
        while(g != null && g.getPosition().equals(getBot().myPosition())){
            g.interact("Pick Up");
            g = getBot().getGroundItems().closest(i -> i != null && i.getId() == MOLTEN_GLASS_ID);
        }

    }

    int getMaterialID(Material m){
        switch(m){
            case Seaweed:
                return 1;
            case SodaAsh:
                return 2;
            case SwampWeed:
                return 3;
            case GiantSeaweed:
                return 4;
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

    private void getSupplies(){
        if(getBot().getInventory().contains(MOLTEN_GLASS_ID)){
            depositGlass();
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
