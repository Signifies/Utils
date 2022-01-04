package commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import signifies.github.io.Helper;
import signifies.github.io.UtilPermissions;
import signifies.github.io.Utils;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Signifies on 1/27/2020 - 6:19 PM.
 */
public class CMSCommand extends Helper  implements CommandExecutor {


    private Utils instance;
    public CMSCommand(Utils value) {
        instance = value;
    }

    /*
    cms notes:

    915 pointed out that CTF already has chat messages for general gameplay.
    So If we make the chat messages clickable to anyone who is in CMS mode,
    they can just teleport by using using TextComponent somehow.

    For stale mates we can use commands such as /fr /fb. Which will teleport or just scroll up.

    /hidespecs command for the streamer.

     */

    public boolean onCommand(CommandSender sender, Command cmd, String CommandLabel, String args[]){

        if(!(sender instanceof Player)) {
            sender.sendMessage(color("&cThis ain't for the console. "));
            return true;
        }

        Player p = (Player)sender;
        // if(User.getRank() >= Rank.MOD) { //Do shit.}
        if(args.length < 1 && UtilPermissions.CMS_COMMAND.checkPermission(p)) {
            enableCMSMode(p);
        }else {
            if(args.length > 0) {
                Player target = Bukkit.getServer().getPlayer(args[0]);
                if(target == null) {
                    p.sendMessage(color("The user " +args[0] + " could not be found."));
                    return true;
                }
                else {
                    enableCMSMode(target);
                    p.sendMessage(color("&7>> &fYou enabled &cCMS Mode &f for &a" +target.getName()+"&f."));
                }
            }
        }
        return true;
    }

    /**
     * Permission check comes with the command.
     * @param p
     */
    public void enableCMSMode(Player p) {
        if(!instance.isCMS.contains(p.getUniqueId())) {
            instance.getCMSStatus().add(p.getUniqueId());
            log("Added UUID of "+ p.getName() + "to the CMS list with the following UUID." + p.getUniqueId().toString(), 1);
            p.setGameMode(GameMode.SPECTATOR);
            clearArmor(p);
            p.getInventory().clear();
            //TODO What if I set the specific item in the said slot, like slot [0], [1]? Would that override the other inventory?
            /*
            p.getInventory().addItem(createItem(Material.RED_WOOL,1,"RED")); //This could be changed to flag location. So if it is dropped, it will go to the flag.
            p.getInventory().addItem(createItem(Material.BLUE_WOOL,1,"&fTeleport to &9Blue's &fFlag"));//This could be changed to flag location. So if it is dropped, it will go to the flag.
            p.getInventory().addItem(createItem(Material.RED_BANNER,1,"&fTeleport to &cRed's &fFlag holder"));
            p.getInventory().addItem(createItem(Material.BLUE_BANNER,1,"&fTeleport to &9Blue's &fFlag Holder"));
            p.getInventory().addItem(createItem(Material.RED_STAINED_GLASS,1, "&fHealth info for &cRed's &fFlag holder")); //Display name, Amount of steak, and health (in Dec)
            p.getInventory().addItem(createItem(Material.BLUE_STAINED_GLASS,1, "&fHealth Info for &9Blue's &fFlag holder.")); //Display name, Amount of steak, and health (in Dec)
            */
            p.setFlySpeed(0.5f);
            p.sendMessage(color("You have been set into &cCMS &fmode."));
        }else {
            p.getInventory().clear();
            setArmor(p);
            p.setFallDistance(0);
            p.setGameMode(GameMode.SURVIVAL);
            p.sendMessage(color("You are no longer in &cCMS &fmode."));
            p.setFlySpeed(0.2f);
            instance.getCMSStatus().remove(p.getUniqueId());

        }
    }

    public void CTFWarp(Player p, String warpName) {
        if(instance.getWarps().getWarpConfig().getConfigurationSection("warps."+warpName) == null) return;

        World w = Bukkit.getServer().getWorld(instance.getWarps().getWarpConfig().getString("warps." + warpName + ".world"));
        double x = instance.getWarps().getWarpConfig().getDouble("warps." + warpName + ".x");
        double y = instance.getWarps().getWarpConfig().getDouble("warps." + warpName + ".y");
        double z = instance.getWarps().getWarpConfig().getDouble("warps." + warpName + ".z");
        p.teleport(new Location(w, x, y, z));

        //p.sendMessage(color("&7Warping to &6" + args[0] + "&7..."));
    }

}
