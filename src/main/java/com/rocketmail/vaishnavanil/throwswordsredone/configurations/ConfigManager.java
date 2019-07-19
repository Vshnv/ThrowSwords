package com.rocketmail.vaishnavanil.throwswordsredone.configurations;

import com.rocketmail.vaishnavanil.throwswordsredone.ThrowSwordsREDONE;
import com.rocketmail.vaishnavanil.throwswordsredone.messages.Message;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public enum ConfigManager {
    get;
    boolean loreSupport = false;
    ThrowSwordsREDONE ins;
    List<Material> throwbales = new ArrayList<>();
    List<Material> swingables = new ArrayList<>();
    public boolean isThrowable(ItemStack i){
        return throwbales.contains(i.getType());
    }
    public boolean isSwingable(ItemStack i){
        return swingables.contains(i.getType());
    }
    public void init(){
        ins = ThrowSwordsREDONE.get();
        List<Material> throwableMat = new ArrayList<>();
        throwableMat.add(Material.DIAMOND_SWORD);
        throwableMat.add(Material.GOLDEN_SWORD);
        throwableMat.add(Material.IRON_SWORD);
        throwableMat.add(Material.STONE_SWORD);
        throwableMat.add(Material.WOODEN_SWORD);
        ins.getConfig().addDefault("Throwables",throwableMat);
        for(Message m :Message.values()){
            ins.getConfig().addDefault("Messages." + m.toString(),m.getMessage());
        }
        List<Material> swingable = new ArrayList<>();
        swingable.add(Material.GOLDEN_AXE);
        ins.getConfig().addDefault("Swingables",swingable);
        ins.saveConfig();

        //LOADING
        loadThrowables();
        loadSwingables();
        loadMessages();
    }


    public void loadThrowables(){
        List<Material> th = new ArrayList<>();
        if(ins.getConfig().getStringList("Throwables").isEmpty()){
            throwbales.clear();
            return;
        }
        for(String s:ins.getConfig().getStringList("Throwables")){
            try{
                Material a = Material.getMaterial(s);
                th.add(a);
            }catch (Exception e){
                continue;
            }
        }
        if(th.isEmpty())return;
        throwbales = th;
    }
    public void loadSwingables(){
        List<Material> th = new ArrayList<>();
        if(ins.getConfig().getStringList("Swingables").isEmpty()){
            swingables.clear();
            return;
        }
        for(String s:ins.getConfig().getStringList("Swingables")){
            try{
                Material a = Material.getMaterial(s);
                th.add(a);
            }catch (Exception e){
                ins.getLogger().log(Level.WARNING,"ThrowSwords could not find Material:" + s+". Skipping the item");
                continue;
            }
        }
        if(th.isEmpty())return;
        swingables = th;
    }
    public void loadMessages(){
        if(ins.getConfig().getStringList("Messages").isEmpty()){
            return;
        }
        for(Message m:Message.values()){
            String Message = ins.getConfig().getString("Messages." + m.toString());
            m.setMessage(Message);
        }
    }
}
