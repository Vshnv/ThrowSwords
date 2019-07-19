package com.rocketmail.vaishnavanil.throwswordsredone.animator;

import com.rocketmail.vaishnavanil.throwswordsredone.messages.Message;
import com.rocketmail.vaishnavanil.throwswordsredone.messages.MsgSend;
import com.rocketmail.vaishnavanil.throwswordsredone.ThrowSwordsREDONE;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;

public enum AnimationHandler {
    get;
    public static BukkitTask animator;
    public void begin(){
        animator = new BukkitRunnable(){

            @Override
            public void run() {
                if(ThrowableItem.getThrownItems().isEmpty())return;
                for(ThrowableItem TI :ThrowableItem.getThrownItems()){
                    if(TI.updateInstance()){
                        MsgSend.sendActionBar(Message.TIMEOUT,TI.getThrower());
                        TI.returnItem();
                        continue;
                    }
                    if(collitionBlock(TI)){
                        MsgSend.sendActionBar(Message.HIT_BLOCK,TI.getThrower());
                        TI.getThrower().getInventory().addItem(TI.getThrown());
                        TI.reportCollide();
                    }
                    if(collitionEntity(TI)){
                        MsgSend.sendActionBar(Message.HIT_ENTITY,TI.getThrower());
                        TI.getThrower().getInventory().addItem(TI.getThrown());
                        TI.reportCollide();
                        Entity Collided = getCollided(TI);
                        induceDamageByItem(TI.getThrown(),Collided);
                        induceKnockback(TI,Collided,1D);
                    }
                    if(TI.collided)continue;
                    TI.moveForward(1D);
                }
            }
        }.runTaskTimer(ThrowSwordsREDONE.get(),5,5);
    }
    public void induceDamageByItem(ItemStack s,Entity e){
        if(e instanceof LivingEntity){
            ((LivingEntity) e).damage(2D);
        }
    }
    public void induceKnockback(ThrowableItem TI,Entity collided,double multiplier){
        collided.setVelocity(TI.getStandLocation().getDirection().normalize().multiply(multiplier));
    }
    public boolean collitionBlock(ThrowableItem TI){
        Location sL = TI.getSwordLocation();
        RayTraceResult r =sL.getWorld().rayTraceBlocks(sL,TI.getStandLocation().getDirection(),1, FluidCollisionMode.NEVER);
        return r != null;
    }
    public boolean collitionEntity(ThrowableItem TI){
        Location sL = TI.getSwordLocation();
        RayTraceResult r = sL.getWorld().rayTraceEntities(sL,TI.getStandLocation().getDirection(),1);
        if(r == null){
            return false;
        }else{
            if(!(r.getHitEntity() instanceof LivingEntity)){
                return false;
            }else{
                return true;
            }
        }
    }
    public Entity getCollided(ThrowableItem TI){
        Location sL = TI.getSwordLocation();
        RayTraceResult r = sL.getWorld().rayTraceEntities(sL,TI.getStandLocation().getDirection(),1);
        if(r == null)throw new NullPointerException("Incorrect usage of #getCollided() method!");
        Entity hit = r.getHitEntity();
        return hit;
    }
}
