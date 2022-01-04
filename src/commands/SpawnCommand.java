package commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import signifies.github.io.Helper;
import signifies.github.io.Utils;

/**
 * Created by Signifies on 3/14/2019 - 20:11.
 */
public class SpawnCommand extends Helper implements CommandExecutor {
    private Utils instance;
    public SpawnCommand(Utils utils){
        this.instance = utils;
    }

    public void spawn(Player player) {
        World w = Bukkit.getServer().getWorld(instance.getConf().getuConfig().getString("spawn.world"));
        double x = instance.getConf().getuConfig().getDouble("spawn.x");
        double y = instance.getConf().getuConfig().getDouble("spawn.y");
        double z = instance.getConf().getuConfig().getDouble("spawn.z");

        if(instance.getConf().getuConfig().getConfigurationSection("spawn") == null)
        {
            player.sendMessage(color("&cError: The spawn hasn't been set on this server!"));
        }else
        {
            player.teleport(new Location(w,x,y,z));
            player.sendMessage(color("&7Teleporting to spawn..."));
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String comandLabel, String args[]) {

        if(sender instanceof Player){

            Player p = (Player)sender;

            if(args.length < 1) {
                //gotospawn.
                spawn(p);
            }
        }else{
            sender.sendMessage(color("&cSorry, the console cannot use this command."));
        }

        return true;
    }
}