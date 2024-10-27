package de.jeff_media.BestTools;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;


public class CommandDebug {

    static void debug(CommandSender sender, Command command, Main main, String arg) {


        if (!sender.hasPermission("besttools.debug")) {
            sender.sendMessage(ChatColor.YELLOW + main.getName() + ": you don't have permission to use this command.");
            return;
        }
        if(arg.equalsIgnoreCase("debug")) {
            main.debug=!main.debug;
            if(main.debug) {
                sender.sendMessage(ChatColor.RED + main.getName() + " debug mode has been enabled.");
            } else {
                sender.sendMessage(ChatColor.GREEN + main.getName() + " debug mode has been disabled.");
            }
        }
        else if(arg.equals("performance")) {
            main.measurePerformance=!main.measurePerformance;
            if(main.measurePerformance) {
                sender.sendMessage(ChatColor.RED + main.getName() + " performance test has been enabled.");
            } else {
                sender.sendMessage(ChatColor.GREEN + main.getName() + " performance test has been disabled.");
            }
        }
    }
}
