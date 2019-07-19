package com.rocketmail.vaishnavanil.throwswordsredone;

import com.rocketmail.vaishnavanil.throwswordsredone.animator.AnimationHandler;
import com.rocketmail.vaishnavanil.throwswordsredone.animator.ThrowableItem;
import com.rocketmail.vaishnavanil.throwswordsredone.configurations.ConfigManager;
import com.rocketmail.vaishnavanil.throwswordsredone.throwhandle.QuitListener;
import com.rocketmail.vaishnavanil.throwswordsredone.throwhandle.ThrowListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ThrowSwordsREDONE extends JavaPlugin {
    private static ThrowSwordsREDONE TSR;
    public static ThrowSwordsREDONE get(){
        return TSR;
    }
    @Override
    public void onEnable() {
        TSR = this;



        ConfigManager.get.init();
        AnimationHandler.get.begin();



        this.getServer().getPluginManager().registerEvents(new ThrowListener(),this);
        this.getServer().getPluginManager().registerEvents(new QuitListener(),this);
    }

    @Override
    public void onDisable() {
        if(!ThrowableItem.getThrownItems().isEmpty()){
            for(ThrowableItem i:ThrowableItem.getThrownItems()){
                i.returnItem();
            }
        }
        AnimationHandler.animator.cancel();
    }
}
