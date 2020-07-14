package de.jeff_media.BestTools;

import org.bukkit.ChatColor;

public class Messages {

    Main main;

    final String
            MSG_BESTTOOL_ENABLED,
            MSG_BESTTOOL_DISABLED,
            MSG_BESTTOOL_USAGE,
            MSG_REFILL_USAGE,
            MSG_REFILL_ENABLED,
            MSG_REFILL_DISABLED,
            MSG_HOTBAR_ONLY_ENABLED,
            MSG_HOTBAR_ONLY_DISABLED;

    Messages(Main main) {
        this.main=main;

        MSG_BESTTOOL_ENABLED = getMsg("besttools-enabled","&7BestTools has been &aenabled&7.");
        MSG_BESTTOOL_DISABLED = getMsg("besttools-disabled","&7BestTools has been &cdisabled&7.");

        MSG_REFILL_ENABLED = getMsg("refill-enabled","&7Refill has been &aenabled&7.");
        MSG_REFILL_DISABLED = getMsg("refill-disabled","&7Refill has been &cdisabled&7.");

        MSG_BESTTOOL_USAGE = getMsg("besttools-usage","&7Hint: Type &6/besttools&7 or &6/bt&7 to automatically use the best tool when breaking blocks.");
        MSG_REFILL_USAGE = getMsg("refill-usage","&7Hint: Type &6/refill&7 or &6/rf&7 to automatically refill empty items from your inventory.");

        MSG_HOTBAR_ONLY_ENABLED = getMsg("hotbar-only-enabled","&7BestTools will only use tools from your hotbar.");
        MSG_HOTBAR_ONLY_DISABLED = getMsg("hotbar-only-disabled","&7BestTools will use all tools from your inventory.");

    }

    String getMsg(String path, String defaultMessage) {
        return ChatColor.translateAlternateColorCodes('&',main.getConfig().getString("message-"+path,defaultMessage));
    }

}
