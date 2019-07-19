package com.rocketmail.vaishnavanil.throwswordsredone.throwhandle;

import com.rocketmail.vaishnavanil.throwswordsredone.animator.ThrowableItem;
import com.rocketmail.vaishnavanil.throwswordsredone.configurations.ConfigManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ThrowListener implements Listener
{


    @EventHandler
    public void onThrow(PlayerDropItemEvent e){
        if(e.getPlayer().isSneaking()){
            if(ConfigManager.get.isThrowable(e.getItemDrop().getItemStack())) {
                new ThrowableItem(e.getPlayer().getInventory().getItemInMainHand(), e.getPlayer().getLocation(), e.getPlayer());
                e.setCancelled(true);
            }
        }
    }


}
