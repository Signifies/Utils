package commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import signifies.github.io.Helper;
import signifies.github.io.UtilPermissions;
import signifies.github.io.Utils;

import java.util.List;

/**
 * Created by Signifies on 1/27/2020 - 10:50 PM.
 */
public class SetWarpCommand extends Helper implements CommandExecutor {

    private Utils instance;
    public SetWarpCommand(Utils value) {
        instance = value;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[])
    {

        if(!(sender instanceof Player))
        {
            sender.sendMessage(color("%prefix% &2Sorry, the console can't set warps."));
            return true;
        }

        Player p =(Player)sender;

        if (cmd.getName().equalsIgnoreCase("setwarp"))
        {
            if(!UtilPermissions.BUILD_SET_WARPS.checkPermission(p))
            {

                p.sendMessage(color("&2Error you don't have permissions for this."));
            }else
            {
                if (args.length == 0) {
                    p.sendMessage(color("&7What warp name do you want to set?"));
                    return true;
                }
                String warp = args[0];
                List<String> value = instance.getWarps().getWarpConfig().getStringList("Warp-list");
                if(!value.contains(warp)) {
                    value.add(warp);
                }
                instance.getWarps().getWarpConfig().set("Warp-list",value);
                instance.getWarps().getWarpConfig().set("warps." + args[0] + ".world", p.getLocation().getWorld().getName());
                instance.getWarps().getWarpConfig().set("warps." + args[0] + ".x", p.getLocation().getX());
                instance.getWarps().getWarpConfig().set("warps." + args[0] + ".y", p.getLocation().getY());
                instance.getWarps().getWarpConfig().set("warps." + args[0] + ".z", p.getLocation().getZ());
//                warpList(instance,args[0]);

                instance.getWarps().saveWarpConfig();
                log("&cAdding the warp " + warp + " To the config...",1);
                p.sendMessage(color("&7Set the warp, &e" + args[0] + " &7..."));
            }
        }
        return true;
    }

}
