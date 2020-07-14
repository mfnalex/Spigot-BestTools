package de.jeff_media.BestTools;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandBestTools implements CommandExecutor {

    Main main;

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

        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command.");
            return true;
        }

        p = (Player) sender;
        setting = main.getPlayerSetting(p);

        setting.setHasSeenBestToolsMessage(true);

        // Toggle hotbarOnly
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("hotbaronly")
                    || args[0].equalsIgnoreCase("hotbar")) {

                if (setting.toggleHotbarOnly()) {
                    p.sendMessage(main.messages.MSG_HOTBAR_ONLY_ENABLED);
                } else {
                    p.sendMessage(main.messages.MSG_HOTBAR_ONLY_DISABLED);
                }
                return true;
            }
        }

        // Toggle bestToolEnabled
        if (main.getPlayerSetting(p).toggleBestToolsEnabled()) {
            p.sendMessage(main.messages.MSG_BESTTOOL_ENABLED);
        } else {
            p.sendMessage(main.messages.MSG_BESTTOOL_DISABLED);
        }
        return true;
    }
}
