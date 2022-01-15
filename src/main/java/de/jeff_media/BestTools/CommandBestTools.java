package de.jeff_media.BestTools;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class CommandBestTools implements CommandExecutor {

    final Main main;

    CommandBestTools(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        Player p;
        PlayerSetting setting;

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            CommandReload.reload(sender, command, main);
            return true;
        }

        if(args.length > 0 && (args[0].equalsIgnoreCase("debug")
                || args[0].equalsIgnoreCase("performance"))) {
            CommandDebug.debug(sender,command,main,args[0]);
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command.");
            return true;
        }

        p = (Player) sender;
        setting = main.getPlayerSetting(p);
        setting.getBtcache().invalidated();

        if(args.length>0 && (args[0].equalsIgnoreCase("blacklist") || args[0].equalsIgnoreCase("bl"))) {
            String[] newArgs = Arrays.copyOfRange(args,1,args.length);
            main.commandBlacklist.onCommand(sender,command,alias,newArgs);
            return true;
        }

        setting.setHasSeenBestToolsMessage(true);

        // Toggle hotbarOnly
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("hotbaronly")
                    || args[0].equalsIgnoreCase("hotbar")) {

                if (setting.toggleHotbarOnly()) {
                    Messages.sendMessage(p,main.messages.MSG_HOTBAR_ONLY_ENABLED);
                } else {
                    Messages.sendMessage(p,main.messages.MSG_HOTBAR_ONLY_DISABLED);
                }
                return true;
            }

            if(args[0].equalsIgnoreCase("settings")
                    || args[0].equalsIgnoreCase("gui")) {
                main.guiHandler.open(p);
                return true;
            }

        }

        // Toggle bestToolEnabled
        if (main.getPlayerSetting(p).toggleBestToolsEnabled()) {
            Messages.sendMessage(p,main.messages.MSG_BESTTOOL_ENABLED);
        } else {
            Messages.sendMessage(p,main.messages.MSG_BESTTOOL_DISABLED);
        }
        return true;
    }
}
