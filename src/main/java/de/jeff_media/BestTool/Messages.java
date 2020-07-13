package de.jeff_media.BestTool;

import org.bukkit.ChatColor;

public class Messages {

    Main main;

    final String
            MSG_BESTTOOL_ENABLED,
            MSG_BESTTOOL_DISABLED,
            MSG_BESTTOOL_USAGE,
            MSG_REFILL_ENABLED,
            MSG_REFILL_DISABLED;

    Messages(Main main) {
        this.main=main;

        MSG_BESTTOOL_ENABLED = getMsg("besttool-enabled","&7BestTool has been &aenabled&7.");
        MSG_BESTTOOL_DISABLED = getMsg("besttool-disabled","&7BestTool has been &cdisabled&7.");

        MSG_REFILL_ENABLED = getMsg("besttool-enabled","&7Refill has been &aenabled&7.");
        MSG_REFILL_DISABLED = getMsg("besttool-disabled","&7Refill has been &cdisabled&7.");

        MSG_BESTTOOL_USAGE = getMsg("besttool-usage","&7Hint: Type &6/besttool&7 to automatically use the best tool when breaking blocks.");
    }

    String getMsg(String path, String defaultMessage) {
        return ChatColor.translateAlternateColorCodes('&',main.getConfig().getString("message-"+path,defaultMessage));
    }

}
