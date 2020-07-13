package de.jeff_media.BestTool;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandBestTool implements CommandExecutor {

    Main main;

    CommandBestTool(Main main) {
        this.main=main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        Player p;

        // Reload command start //
        if(args.length>0 && args[0].equalsIgnoreCase("reload")) {
            if(!sender.hasPermission("besttool.reload")) {
                sender.sendMessage(command.getPermissionMessage());
                return true;
            }
            main.load(true);
            sender.sendMessage(ChatColor.GREEN+main.getName()+" has been reloaded.");
            return true;
        }
        // Reload command end //

        if(!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command.");
            return true;
        }

        p = (Player) sender;

        // Toggle BestToolEnabled //
        if(main.getPlayerSetting(p).toggleBestToolEnabled()) {
            // Enabled
        } else {
            // Disabled
        }

        return false;
    }
}
