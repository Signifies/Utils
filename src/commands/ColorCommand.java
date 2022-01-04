package commands;

import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import signifies.github.io.Helper;
import signifies.github.io.UtilPermissions;
import signifies.github.io.Utils;

import java.util.HashMap;

/**
 * Created by Signifies on 3/17/2019 - 23:55.
 */
public class ColorCommand extends Helper implements CommandExecutor {

    private Utils instance;
    public ColorCommand(Utils util) {
        this.instance = util;
    }

    public HashMap<String, Long> cooldowns = new HashMap<>();

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]) {

        if(args.length < 1) {
            sender.sendMessage(color("&7/color &f<color code> &a||"));
            sender.sendMessage(color("Example: &7/color &b&l  >>" +"Gives the following format: &b&l"+sender.getName()));
        }else if (args.length >=1){
            if(sender instanceof Player){
                Player p =(Player)sender;

                if(UtilPermissions.SUFFIX_SELF.checkPermission(p)) {

                    int cooldownTime = instance.getConf().getuConfig().getInt("Chat.custom-chat.delay-suffix");// Get number of seconds from wherever you want
                    if(cooldowns.containsKey(sender.getName())) {
                        long secondsLeft = ((cooldowns.get(p.getName())/1000)+cooldownTime) - (System.currentTimeMillis()/1000);
                        if(secondsLeft>0) {
                            // Still cooling down
                            p.sendMessage(color("&7You can use the color command in &f"+ secondsLeft +"&7 seconds."));
                            return true;
                        }
                    }
                    // No cooldown found or cooldown has expired, save new cooldown
                    if(!UtilPermissions.BYPASS_STATUS.checkPermission(p)) {
                        cooldowns.put(p.getName(), System.currentTimeMillis());
                    }
                    StringBuilder builder = new StringBuilder();

                    for(String arg : args){
                        builder.append(arg + " ");
                    }
                    String result = builder.toString();
                    char value[] = result.toCharArray();
                    int count = value.length;


                    if(count > 6) {

                        p.sendMessage(color("&7Sorry, but your color is too long."));
                    }else{
                        if(instance.getConf().getuConfig().getBoolean("Economy.chat.prefix-suffix.enabled")) {
                            EconomyResponse econ = instance.econ.withdrawPlayer(p, instance.getConf().getuConfig().getDouble("Economy.chat.prefix-suffix.price"));
                            if(econ.transactionSuccess()) {
                                setSuffix(result,p);
                                p.sendMessage(color("&9Prefix&7> Enjoy your new name color! &f" +instance.getConf().getuConfig().getDouble("Economy.chat.prefix-suffix.price") +" &7 was taken from your account."));
                            }else {
                                p.sendMessage(color("&cAn error has occurred. Contact an Administrator."));
                            }
                        }else {
                            setSuffix(result,p);
                            p.sendMessage(color("&9Prefix&7> Enjoy your new name color! &f" ));
                        }
                    }
                }else {
                    p.sendMessage(color("&7You aren't allowed to set the color."));
                }
            }else {
                sender.sendMessage(color("&7Please use /color <player> <color> for console."));
            }
        }
        return true;
    }

}
