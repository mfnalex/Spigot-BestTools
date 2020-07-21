package de.jeff_media.BestTools;

import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;

import java.util.List;

public class Messages {

    final Main main;

    final String
            MSG_BESTTOOL_ENABLED,
            MSG_BESTTOOL_DISABLED,
            MSG_BESTTOOL_USAGE,
            MSG_REFILL_USAGE,
            MSG_REFILL_ENABLED,
            MSG_REFILL_DISABLED,
            MSG_HOTBAR_ONLY_ENABLED,
            MSG_HOTBAR_ONLY_DISABLED;

    final String
            GUI_BESTTOOLS_ENABLED,
            GUI_BESTTOOLS_DISABLED,
            GUI_REFILL_ENABLED,
            GUI_REFILL_DISABLED,
            GUI_FAVORITE_SLOT;
            //GUI_BESTTOOLS_FAVORITE_SLOT,
            //GUI_BESTTOOLS_HOTBAR_ONLY;

    final String
            GUI_FAVORITE_SLOT_LORE,
            GUI_REFILL_LORE,
            GUI_BESTTOOLS_LORE,
            GUI_HOTBAR_LORE;

    final String BL_ADDED,
            BL_INVALID,
            BL_REMOVED,
            BL_NOTHINGSPECIFIED,
            BL_EMPTY;

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

        GUI_BESTTOOLS_ENABLED = getMsg("gui","besttools-enabled","&6BestTools: &aenabled");
        GUI_BESTTOOLS_DISABLED = getMsg("gui","besttools-enabled","&6BestTools: &cdisabled");
        GUI_REFILL_ENABLED = getMsg("gui","refill-enabled","&6Refill: &aenabled");
        GUI_REFILL_DISABLED = getMsg("gui","refill-enabled","&6Refill: &cdisabled");
        GUI_FAVORITE_SLOT = getMsg("gui","favorite-slot","&7Set as favorite slot");

        GUI_FAVORITE_SLOT_LORE = getMsgs("gui", "favorite-slot-lore",
                "&7If your hotbar is full and BestTools uses a tool from your inventory, it will be places in this slot."
        );

        GUI_REFILL_LORE = getMsgs("gui","refill-lore",
                "&7Automatically refills empty hotbar slots."
        );

        GUI_BESTTOOLS_LORE = getMsgs("gui","besttools-lose",
                "&7Automatically chooses the best tools when breaking blocks.");

        GUI_HOTBAR_LORE = getMsgs("gui","hotbar-lore",
                "&7When enabled, BestTools will only use tools from your hotbar instead of from your whole inventory."
        );

        BL_EMPTY = getMsg("blacklist-empty","&7You blacklist is empty.");
        BL_ADDED = getMsg("blacklist-added","&2Added to blacklist:&7 %s");
        BL_INVALID = getMsg("blacklist-invalid","&4Invalid items:&7 %s");
        BL_REMOVED = getMsg("blacklist-removed","&2Removed from blacklist:&7 %s");
        BL_NOTHINGSPECIFIED = getMsg("blacklist-nothing-specified","&7You must either hold an item in your hand or specify at least material as parameter.");

        main.getConfig().addDefault("blacklist-title","----- &cBlacklist&r -----");


    }

    String getMsgs(String prefix, String path, String defaultMessage) {
        final int maxLength = 30;
        //List<String> msgs = main.getConfig().getStringList(prefix+"-"+path);
        //if(msgs==null && msgs.size()!=0) return translateAlternateColorCodes(msgs.toArray(new String[0]));
        String msg = main.getConfig().getString(prefix+"-"+path);
        if(msg==null) {
            return WordUtils.wrap(ChatColor.translateAlternateColorCodes('&',defaultMessage),maxLength);
        }
        msg = WordUtils.wrap(ChatColor.translateAlternateColorCodes('&',msg),maxLength);
        return msg;

    }

    String getMsg(String path, String defaultMessage) {
        return ChatColor.translateAlternateColorCodes('&',main.getConfig().getString("message-"+path,defaultMessage));
    }
    String getMsg(String prefix, String path, String defaultMessage) {
        return ChatColor.translateAlternateColorCodes('&',main.getConfig().getString(prefix+"-"+path,defaultMessage));
    }

    static String[] translateAlternateColorCodes(String[] strings) {
        for(int i=0;i<strings.length;i++) {
            strings[i]=ChatColor.translateAlternateColorCodes('&',strings[i]);
        }
        return strings;
    }

}
