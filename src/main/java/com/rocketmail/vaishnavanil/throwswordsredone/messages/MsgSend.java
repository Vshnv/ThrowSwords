package com.rocketmail.vaishnavanil.throwswordsredone.messages;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MsgSend {
    public static void sendActionBar(Message type, Player p){
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',type.getMessage())));
    }
}
