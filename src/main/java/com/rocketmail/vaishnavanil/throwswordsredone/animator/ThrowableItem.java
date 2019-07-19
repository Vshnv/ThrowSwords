package com.rocketmail.vaishnavanil.throwswordsredone.animator;

import com.rocketmail.vaishnavanil.throwswordsredone.messages.Message;
import com.rocketmail.vaishnavanil.throwswordsredone.messages.MsgSend;
import com.rocketmail.vaishnavanil.throwswordsredone.ThrowSwordsREDONE;
import net.minecraft.server.v1_14_R1.EnumItemSlot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ThrowableItem {
    private static HashMap<UUID,ThrowableItem> stands = new HashMap<>();
    public static long TIMEOUT = 10000;
    private UUID id;
    private Instant start;
    private ItemStack thrown;
    private ArmorStand ThrownStand;
    private Player thrower;
    private static ItemStack AIR = new ItemStack(Material.AIR);
    public static  List<ThrowableItem> getThrownItems(){
        return (List<ThrowableItem>) stands.values();
    }
    public ThrowableItem(ItemStack item, Location throwerloc, Player thr){
        thr.getInventory().setItemInMainHand(AIR.clone());
        thrown = item;
        if(!stands.containsValue(this))stands.put(getUniqueID(),this);

        start = Instant.now();
        ThrownStand =  spawnItemAt(throwerloc);
        thrower = thr;
        MsgSend.sendActionBar(Message.THROW,thr);
    }
    public void returnItem(){
       List<ItemStack> l = (List<ItemStack>) thrower.getInventory().addItem(thrown);
       if(!l.isEmpty()){
           l.forEach(i -> thrower.getWorld().dropItemNaturally(thrower.getLocation(),i));
       }
       stands.remove(id);
       ThrownStand.remove();
    }
    private UUID getUniqueID(){
        if(id==null){
            id = genUUID();
        }
        return id;
    }
    public Player getThrower(){
        return thrower;
    }
    public ItemStack getThrown(){
        return thrown;
    }
    public boolean updateInstance(){
        if(Duration.between(start,Instant.now()).toMillis() > TIMEOUT){
            new BukkitRunnable(){
                @Override
                public void run() {
                    stands.remove(id);
                }}.runTask(ThrowSwordsREDONE.get());
            ThrownStand.remove();
            return true;
        }
        return false;
    }
    private UUID genUUID(){
        boolean NotFound = true;
        UUID newID = null;
        while(NotFound) {
            newID = UUID.randomUUID();
            if (stands.containsKey(newID)) {
                continue;
            }
            NotFound = false;
        }
        return newID;
    }
    public void moveForward(double speedFactor){
        ThrownStand.teleport(ThrownStand.getLocation().add(ThrownStand.getLocation().getDirection().normalize().multiply(speedFactor)));
    }
    public boolean collided = false;
    private ArmorStand spawnItemAt(Location ThrownerLoc){
        Location l =ThrownerLoc.clone();
        //l.setY(l.getY()-1.5);
        net.minecraft.server.v1_14_R1.World w = ((CraftWorld)l.getWorld()).getHandle();
        net.minecraft.server.v1_14_R1.EntityArmorStand nmsEntity = new net.minecraft.server.v1_14_R1.EntityArmorStand(w,l.getX(),l.getY(),l.getZ());
        nmsEntity.setLocation(l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
        nmsEntity.setInvisible(true);
        nmsEntity.setNoGravity(true);
        nmsEntity.setArms(true);
        nmsEntity.setEquipment(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(thrown));
        nmsEntity.setBasePlate(false);
        w.addEntity(nmsEntity);
        ArmorStand s = (ArmorStand) nmsEntity.getBukkitEntity();
        s.setRightArmPose(new EulerAngle(Math.toRadians(0.0), Math.toRadians(-ThrownerLoc.getPitch()), Math.toRadians(90.0)));
        tag(s);
        return s;
    }
    public void reportCollide(){
        if(collided)return;
        collided = true;
        new BukkitRunnable(){
            @Override
            public void run() {
                stands.remove(id);
            }
        }.runTask(ThrowSwordsREDONE.get());
        ThrownStand.remove();

    }
    private void tag(Entity e){
        e.setMetadata("ThrownMeta",new FixedMetadataValue(ThrowSwordsREDONE.get(),id.toString()));
    }
    public Location getSwordLocation(){
        Location sL = getRightLocation(ThrownStand.getLocation());
        sL.add(0,1.4,0);
        return sL;
    }
    public Location getStandLocation(){
        return ThrownStand.getLocation();
    }

    private Location getRightLocation(Location l){
        Location RL = l.clone();
        return RL.add(RL.getDirection().rotateAroundY(90).normalize());
    }
    public static boolean isThrownItem(ArmorStand e){
        return e.hasMetadata("ThrownMeta");
    }
    public static ThrowableItem getThrowableItem(ArmorStand e){
        if(!isThrownItem(e))throw new NullPointerException("Provided ARMORSTAND is not a THROWN ITEM! please report to developer!");
        UUID uuid = UUID.fromString(e.getMetadata("ThrownMeta").get(0).asString());
        return stands.get(uuid);
    }
}
