package commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import signifies.github.io.Helper;
import signifies.github.io.UtilPermissions;
import signifies.github.io.Utils;

/**
 * Created by Signifies on 3/14/2019 - 23:26.
 */
public class StaffBroadcast extends Helper implements CommandExecutor {

    private Utils instance;
    public StaffBroadcast(Utils utils){
        this.instance = utils;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]) {

        if(!UtilPermissions.STAFF_CHAT.checkPermission(sender)) {
            sender.sendMessage(defaultMessage(instance.permissionDefault(), instance.getMessage()));
        }else if(args.length < 1){
            sender.sendMessage(color("&7/sc <message>"));
        }else {
            StringBuilder str = new StringBuilder();

            for (String arg : args) {
                str.append(arg + " ");
            }
            String alert = str.toString();
            alert = alert.replace("{msg}",alert);
            alert = alert.replace("{admin}",sender.getName());

            String result = instance.getConf().getuConfig().getString("mod-broadcast.format");
            result = result.replace("{msg}",alert);
            result = result.replace("{admin}",sender.getName());


            for(Player p : Bukkit.getServer().getOnlinePlayers()) {
                if(UtilPermissions.STAFF_CHAT.checkPermission(p)) {
                    p.sendMessage(color(result));
                }
            }
            //adminNotifications(sender.getName(), result, UtilPermissions.STAFF_CHAT); DO this later.
        }
        return true;
    }
}
