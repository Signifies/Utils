package commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import signifies.github.io.Helper;
import signifies.github.io.UtilPermissions;
import signifies.github.io.Utils;

/**
 * Created by Signifies on 3/14/2019 - 19:17.
 */
public class WhitelistCommand extends Helper implements CommandExecutor {

    private Utils instance;
    public WhitelistCommand(Utils utils) {
        this.instance = utils;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]) {

        if(!UtilPermissions.COMMAND_WHITELIST.checkPermission(sender))
        {
            sender.sendMessage(defaultMessage(instance.permissionDefault(), instance.getMessage()));
        }else
        {
            if(args.length < 1)
            {
                sender.sendMessage(color("&7> &f/whitelist &7<[add] [remove] [clear] [list] [on] [off] [enforce]>"));
            }else
            {
                switch (args[0].toLowerCase())
                {
                    case "list":

                        StringBuilder str = new StringBuilder();

                        for(OfflinePlayer player : Bukkit.getServer().getWhitelistedPlayers())
                        {
                            if(str.length() > 0)
                            {
                                str.append(", ");
                            }
                            str.append(player.getName());
                        }

                        sender.sendMessage(color("&7    ----- &aWhitelisted Players &7-----"));
//                            sender.sendMessage(color("&a" + Bukkit.getServer().getWhitelistedPlayers().toString()));
                        sender.sendMessage(color("&6"+str.toString()));
                        break;
                    case "on":
                    case "enabled":
                        Bukkit.getServer().setWhitelist(true);
                        sender.sendMessage(color("&7You have turned on the &awhitelist&7."));
                        adminNotifications(sender.getName(), "Turned on the whitelist.");
                        break;
                    case "off":
                    case "disabled":
                        Bukkit.getServer().setWhitelist(false);
                        sender.sendMessage(color("&7You have turned off the &awhitelist&7."));
                        adminNotifications(sender.getName(),"Turned off the whitelist.");
                        break;

                    case "add":
                    case "+":
                        if(args.length >1)
                        {
                            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                            target.setWhitelisted(true);

                            sender.sendMessage(color("&7The player, &a"+ target.getName() + "&7 has been added to the whitelist."));
                            adminNotifications(sender.getName(), "Whitelisted the player, " +args[1] +".");
                        }else
                        {
                            sender.sendMessage(color("&7/whitelist &aadd &f<playername>"));
                        }
                        break;
                    case "remove":
                    case "rm":
                    case "-":

                        if(args.length >1)
                        {
                            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                            target.setWhitelisted(false);
                            sender.sendMessage(color("&7The player, &a"+ target.getName() + "&7 has been removed to the whitelist."));
                            adminNotifications(sender.getName(), "Removed the player, " +args[1] +" from the whitelist.");
                        }else
                        {
                            sender.sendMessage(color("&7/whitelist &cremove &f<playername>"));
                        }
                        break;

                    case "clear":
                    case "ci":

//                            Bukkit.getServer().getWhitelistedPlayers().clear();
                        for(OfflinePlayer p : Bukkit.getWhitelistedPlayers())
                        {
                            p.setWhitelisted(false);
                        }
                        sender.sendMessage(color("&7Cleared the &fwhitelist&7."));
                        Bukkit.getServer().reloadWhitelist();
                        adminNotifications(sender.getName(), "Cleared the entire whitelist.");
                        break;

                    case "enforce":
                        enforceWhitelist(sender);
                        break;

                    default:
                        sender.sendMessage(color("&7/whitelist"));
                }
            }
        }

        return true;
    }
}
