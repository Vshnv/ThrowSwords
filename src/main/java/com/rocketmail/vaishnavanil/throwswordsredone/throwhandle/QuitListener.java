package com.rocketmail.vaishnavanil.throwswordsredone.throwhandle;

import com.rocketmail.vaishnavanil.throwswordsredone.animator.ThrowableItem;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitListener implements Listener {
    @EventHandler
    public void onQ(PlayerQuitEvent e){
        for(ThrowableItem t:ThrowableItem.getThrownItems()){
            if(t.getThrower().equals(e.getPlayer())){
                t.returnItem();
            }
        }
    }
}
