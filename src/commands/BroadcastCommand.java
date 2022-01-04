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
 * Created by Signifies on 3/15/2019 - 00:02.
 */
public class BroadcastCommand extends Helper implements CommandExecutor {

    private Utils instance;
    public BroadcastCommand(Utils utils) {
        this.instance = utils;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]) {

            if(!UtilPermissions.BROADCAST.checkPermission(sender)) {
                sender.sendMessage(defaultMessage(instance.getConf().getuConfig().getBoolean("perm-message.default"), instance.getConf().getuConfig().getString("perm-message.format")));
            }else {
                if(args.length < 1){
                    sender.sendMessage(color("&7/broadcast <msg> &a||&r &7<msg> <repeat>"));
                }else {
                    StringBuilder builder = new StringBuilder();
                    for(String arg : args) {
                        builder.append(arg + " ");
                    }
                    String result = builder.toString();
                    String format = instance.getConf().getuConfig().getString("Chat.Broadcast.format");
                    format = format.replace("{msg}",result);
                    Bukkit.getServer().broadcastMessage(color(format));

                    //TODO Fix this later. Im fucking tired.
               /*
               *  int msgCount = Integer.parseInt(args[0]);
                if( msgCount> 0) {
                    String r = builder.toString().replace(args[0],"");
                    format = format.replace("{msg}",r);

                    for (int i=0; i <msgCount; i++) {
                        Bukkit.getServer().broadcastMessage(color(format));
                    }
               * */
                }
            }
        return true;
    }

}
