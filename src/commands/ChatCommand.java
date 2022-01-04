package commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import signifies.github.io.Helper;
import signifies.github.io.UtilPermissions;
import signifies.github.io.Utils;

/**
 * Created by Signifies on 3/14/2019 - 19:26.
 */
public class ChatCommand extends Helper implements CommandExecutor {

    private Utils instance;
    public ChatCommand(Utils utils) {
        this.instance = utils;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]) {

        if(!UtilPermissions.CHAT.checkPermission(sender)){
            sender.sendMessage(defaultMessage(instance.permissionDefault(), instance.getMessage()));
        }else if(args.length < 1) {
            sender.sendMessage(color("&7/chat <silence | unmute> || <clear> || <clearself> || <clearothers>"));
        }else {
            if(args.length > 0)
            {
                switch (args[0].toLowerCase())
                {
                    case "clear":
                    case "cl":
                    case "c":

                        if(!UtilPermissions.CHAT_CLEAR.checkPermission(sender)) {
                            sender.sendMessage(defaultMessage(instance.permissionDefault(), instance.getMessage()));
                        }else {
                            clear();
                        }
                        break;
                    case "clearself":
                    case "cs":
                        if(!UtilPermissions.CHAT_CLEARSELF.checkPermission(sender)) {
                            sender.sendMessage(defaultMessage(instance.permissionDefault(), instance.getMessage()));
                        }else
                        {
                            selfClear(sender);
                        }
                        break;
                    case "help":
                    case "?":
                        sender.sendMessage(color("&7/chat <silence | unmute> || <clear> || <clearself> || <clearothers>"));
                        break;
                    case "clearuser":
                    case "cu":
                    case "clearothers":

                        if(!UtilPermissions.CHAT_CLEAR_OTHERS.checkPermission(sender)){
                            sender.sendMessage(defaultMessage(instance.permissionDefault(), instance.getMessage()));
                        }else {
                            if(args.length > 1)
                            {
                                Player target = Bukkit.getServer().getPlayer(args[1]);
                                if(target == null)
                                {
                                    sender.sendMessage(color("&7The player, &f"+args[1] +" &7isn't online." ));
                                }else
                                {
                                    clearPlayer(target);
                                    sender.sendMessage(color("&7You have cleared the &fchat &7for the user," +target.getName()));
                                }
                            }
                        }
                        break;

                    case "silence":
                    case "disable":
                    case "mute":
                        //Change default methods to use global notification system
                        if(!UtilPermissions.CHAT_DISABLE.checkPermission(sender)) {
                            sender.sendMessage(defaultMessage(instance.permissionDefault(), instance.getMessage()));
                        }else{
                            if(!instance.getConf().getuConfig().getBoolean("Chat.Enabled")) {
                                sender.sendMessage(color("&7The chat is already &fdisabled&7."));
                            }else{
                                instance.getConf().getuConfig().set("Chat.Enabled", false);
                                instance.getConf().saveuConfig();
                                sender.sendMessage(color(instance.getConf().getuConfig().getString("Chat.chat-disabled")));
                                Bukkit.getServer().broadcastMessage(color("&7The chat has been &fdisabled&7."));
                                adminNotifications(sender.getName(), "The chat has been muted.");
                            }
                        }
                        break;

                    case "unmute":

                        if(!UtilPermissions.CHAT_ENABLE.checkPermission(sender)){
                            sender.sendMessage(defaultMessage(instance.permissionDefault(), instance.getMessage()));
                        }else {
                            if(instance.getConf().getuConfig().getBoolean("Chat.Enabled")) {
                                sender.sendMessage(color("&7The chat is already &fEnabled&7."));
                            }else{
                                instance.getConf().getuConfig().set("Chat.Enabled", true);
                                instance.getConf().saveuConfig();
                                sender.sendMessage(color("&7You have &fenabled &7global chat."));
                                Bukkit.getServer().broadcastMessage(color("&7The chat has been &fenabled&7."));
                                adminNotifications(sender.getName(), "The chat has been unmuted.");
                            }
                        }
                        break;

                    case "namecolor":


                        default:
                        sender.sendMessage(color("&7Unknown argument, &a" + args[0] + "&7!"));
                        sender.sendMessage(color("&7/chat <silence | unmute> || <clear> || <clearself> || <clearothers>"));
                }
            }
        }

        return true;
    }
}
