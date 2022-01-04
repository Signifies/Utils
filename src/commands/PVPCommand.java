package commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import signifies.github.io.Helper;
import signifies.github.io.UtilPermissions;
import signifies.github.io.Utils;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Signifies on 5/5/2019 - 15:07.
 */
public class PVPCommand extends Helper implements CommandExecutor {

    private Utils instance;
    public HashMap<UUID, Long> cooldowns = new HashMap<>();

    public PVPCommand(Utils value) {
        instance = value;
    }


    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]) {

        if(!(sender instanceof Player)){
            sender.sendMessage(color("&7Not implemented for console usage yet."));
        }else {
            Player p =(Player)sender;

            int cooldownTime = instance.getConf().getuConfig().getInt("PVP.delay"); // Get number of seconds from wherever you want
            if(cooldowns.containsKey(p.getUniqueId())) {
                long secondsLeft = ((cooldowns.get(p.getUniqueId())/1000)+cooldownTime) - (System.currentTimeMillis()/1000);
                if(secondsLeft>0) {
                    // Still cooling down
                    p.sendMessage(color("&7The PVP command has a delay of &f"+ secondsLeft +"&7."));
                    return true;
                }
            }

            // No cooldown found or cooldown has expired, save new cooldown
            if(!UtilPermissions.BYPASS_STATUS.checkPermission(p)) {
                cooldowns.put(p.getUniqueId(), System.currentTimeMillis());
            }

            if(!UtilPermissions.PVP_TOGGLE.checkPermission(p)) {
                p.sendMessage(defaultMessage(instance.getConf().getuConfig().getBoolean("perm-message.default"), "&cNot allowed to use the /pvp command."));
            }else{
                if(args.length > 0){
                    p.sendMessage(color("&7Sorry, this command only uses one argument."));
                }else {

                    if(instance.getPVPToggle().contains(p.getUniqueId())) {
                        instance.getPVPToggle().remove(p.getUniqueId());
                        p.sendMessage(color("&7You have &6disabled &7your PVP."));
                    }else {
                       instance.getPVPToggle().add(p.getUniqueId());
                       p.sendMessage(color("&7You have &6enabled &7your PVP."));
                    }
                }
            }
        }
        return true;
    }
}
