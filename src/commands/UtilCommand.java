package commands;

import com.mysql.jdbc.Util;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import signifies.github.io.Helper;
import signifies.github.io.UtilPermissions;
import signifies.github.io.Utils;

/**
 * Created by Signifies on 3/14/2019 - 17:44.
 */
public class UtilCommand extends Helper implements CommandExecutor {

    private Utils instance;
    public UtilCommand(Utils utils){
        this.instance = utils;
    }

    public void setSpawn(Player player)
    {


        instance.getConf().getuConfig().set("spawn.world",player.getLocation().getWorld().getName());
        instance.getConf().getuConfig().set("spawn.x", player.getLocation().getX());
        instance.getConf().getuConfig().set("spawn.y",player.getLocation().getY());
        instance.getConf().getuConfig().set("spawn.z",player.getLocation().getZ());
        instance.getConf().saveuConfig();
        World w = Bukkit. getServer().getWorld(instance.getConf().getuConfig().getString("spawn.world"));
        double x = instance.getConf().getuConfig().getDouble("spawn.x");
        double y = instance.getConf().getuConfig().getDouble("spawn.y");
        double z = instance.getConf().getuConfig().getDouble("spawn.z");
        player.sendMessage(color("&7You have set the &fspawn&7 at the location: &f"+w.getName() +"&b:" +x +"-"+y+"-"+z));
        adminNotifications(player.getName(), "has set the spawn.");
    }


    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]) {

        if(args.length < 1){
            sendText(commandList(),sender);
        }else {
            switch (args[0].toLowerCase()){

                case "pexsetup":
                    if(sender instanceof  Player) {
                       sender.sendMessage(color("&cThis can only be done through the console."));
                    }else {
                        executePexsetup();
                        sender.sendMessage(color("&4Pex setup initated..."));
                    }
                    break;

                case "reload":
                case "rl":
                    if(!UtilPermissions.RELOAD.checkPermission(sender)){
                        sender.sendMessage(defaultMessage(instance.permissionDefault(), instance.getMessage()));
                    }else {
                        instance.getConf().reloaduConfig();
                        sender.sendMessage(color("&7Util configuration has been &breloaded&7."));
                        adminNotifications(sender.getName(), "&7has reloaded the Util configuration file.");
                    }
                    break;

                    //add maintence option.
                case "maintenance":
                case "repair":

                    if(!UtilPermissions.MAINTENANCE.checkPermission(sender)) {
                        sender.sendMessage(color("&cThis command is for administrators only."));
                    }else if(instance.getConf().getuConfig().getBoolean("Maintenance.enabled")){
                        //wl logic and message.
                        instance.getConf().getuConfig().set("Maintenance.enabled", false);
                        instance.getConf().saveuConfig();
                        Bukkit.getServer().setWhitelist(false);
                        Bukkit.getServer().reloadWhitelist();
                        sender.sendMessage(color(""+check(instance.getConf().getuConfig().getBoolean("Maintenance.enabled"), "server maintenance has been")));
                        adminNotifications(sender.getName(), "disabled server maintenance.");
                    }else {
                        instance.getConf().getuConfig().set("Maintenance.enabled", true);
                        instance.getConf().saveuConfig();
                        enforceWhitelist(sender, instance.getConf().getuConfig().getString("Maintenance.msg"));
                        Bukkit.getServer().setWhitelist(true);
                        Bukkit.getServer().reloadWhitelist();
                        sender.sendMessage(color(""+check(instance.getConf().getuConfig().getBoolean("Maintenance.enabled"), "server maintenance has been")));
                        adminNotifications(sender.getName(), "server maintenance enabled.");
                    }
                    break;

                case "info":
                    if(sender instanceof Player) {
                        Player p =(Player)sender;
                        information(instance,p);
                    }
                    break;

                case "setspawn":

                    if(sender instanceof Player) {
                        Player p =(Player)sender;
                        if (!UtilPermissions.SETSPAWN.checkPermission(p)) {
                            sender.sendMessage(defaultMessage(instance.permissionDefault(), instance.getMessage()));
                        }else{
                            setSpawn(p);
                        }
                    }else
                    {
                        sender.sendMessage(color("&cSorry, the console cannot set the spawn."));
                    }
                    break;

                case "setmotd":
                    if(!UtilPermissions.MOTD_SET.checkPermission(sender)){
                        sender.sendMessage(defaultMessage(instance.permissionDefault(), instance.getMessage()));
                    }else if (args.length > 1){
                        StringBuilder builder = new StringBuilder();
                        for (String arg : args) {
                            builder.append(arg + " ");
                        }
                        String result = builder.toString().replace("setmotd","");
                        instance.getConf().getuConfig().set("MOTD.server-list",result);
                        instance.getConf().saveuConfig();
                        sender.sendMessage(color("&7Set the message to: &f" +result));
                    }
                    break;

                    default:
                        sender.sendMessage(color("&7You've probably entered a wrong command. Idot."));
                }

        }

        return true;
    }
}
