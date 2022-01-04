package commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import signifies.github.io.Helper;
import signifies.github.io.UtilPermissions;
import signifies.github.io.Utils;

import java.util.List;

/**
 * Created by Signifies on 1/27/2020 - 10:59 PM.
 */
public class WarpCommand extends Helper implements CommandExecutor {

    private Utils instance;
    public WarpCommand(Utils value) {
        instance = value;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[])
    {

        if(!(sender instanceof Player))
        {
            sender.sendMessage(color("%prefix% &2Sorry, the console cannot use this command."));
            return true;
        }

        Player p =(Player)sender;

        if(cmd.getName().equalsIgnoreCase("warp"))
        {
            if(!UtilPermissions.BUILD_COMMAND_WARP.checkPermission(p))
            {
               p.sendMessage(color("&2Error you don't have permissions for this."));
            }else
            {
                if (args.length == 0)
                {
                    p.sendMessage(color("&7Where do you need to warp to?"));
                }else
                {
                    if(args[0].equalsIgnoreCase("list"))
                    {
                        if(UtilPermissions.BUILD_COMMAND_WARP_LIST.checkPermission(p))
                        {
                            List<String> list = instance.getWarps().getWarpConfig().getStringList("Warp-list");
                            sendText(warps(list),p);
//                        p.sendMessage(color("&aTesting to see if this is working.."));
//                        warpList(instance,p);
                        }else
                        {
                            p.sendMessage(color("&2Error - &7You don't have permission to list warps."));
                        }
                    }else
                    {
                        if(instance.getWarps().getWarpConfig().getConfigurationSection("warps." + args[0]) == null)
                        {
                            p.sendMessage(color("&2Error &7- The warp, &e" + args[0] + " &7doesn't exist. "));
                        }else
                        {
                            World w = Bukkit.getServer().getWorld(instance.getWarps().getWarpConfig().getString("warps." + args[0] + ".world"));
                            double x = instance.getWarps().getWarpConfig().getDouble("warps." + args[0] + ".x");
                            double y = instance.getWarps().getWarpConfig().getDouble("warps." + args[0] + ".y");
                            double z = instance.getWarps().getWarpConfig().getDouble("warps." + args[0] + ".z");
                            p.teleport(new Location(w, x, y, z));
                            p.sendMessage(color("&7Warping to &6" + args[0] + "&7..."));
                        }
                    }
                }
            }
        }

        return true;
    }
}
