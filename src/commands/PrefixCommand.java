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
import java.util.List;

/**
 * Created by Signifies on 3/16/2019 - 18:05.
 */
public class PrefixCommand extends Helper implements CommandExecutor {

    private Utils instance;
    public PrefixCommand(Utils utils) {
        this.instance = utils;
    }

    public HashMap<String, Long> cooldowns = new HashMap<>();

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]) {

        if(args.length < 1) {
            sender.sendMessage(color("&7/prefix &f<prefix> &a||"));
            sender.sendMessage(color("&7Example: ") +"/prefix &4idot" + color(" &fGives the prefix &4idot"));
        }else if (args.length >=1){
            if(sender instanceof Player){
                Player p =(Player)sender;

                if(UtilPermissions.PREFIX_SELF.checkPermission(p)) {

                    int cooldownTime = instance.getConf().getuConfig().getInt("Chat.custom-chat.delay-prefix"); // Get number of seconds from wherever you want
                    if(cooldowns.containsKey(sender.getName())) {
                        long secondsLeft = ((cooldowns.get(p.getName())/1000)+cooldownTime) - (System.currentTimeMillis()/1000);
                        if(secondsLeft>0) {
                            // Still cooling down
                            p.sendMessage(color("&7The prefix command has a delay of &f"+ secondsLeft +"&7."));
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
                    List<String> list = instance.getConf().getuConfig().getStringList("blocked-words");
                    String test[] = result.split(" ");
                    String filter = test[0];

                    // /prefix [&4prefix]

                    if(count > 17) {
                        p.sendMessage(color("&7Sorry, but your prefix is too long."));
                    }else if(list.contains(filter.toLowerCase().replaceAll("\\s","").replaceAll("&[a-z0-9]",""))) {
                        p.sendMessage(color("&4You can't use that word in a prefix, idot."));
                        p.kickPlayer(color("&4Don't use the word " +"in a prefix you degenerate."));
                    }else {
                        if(instance.getConf().getuConfig().getBoolean("Economy.chat.prefix-suffix.enabled")) {
                            EconomyResponse econ = instance.econ.withdrawPlayer(p, instance.getConf().getuConfig().getDouble("Economy.chat.prefix-suffix.price"));

                            if(econ.transactionSuccess()) {
                                setPrefix(result,p);
                                p.sendMessage(color("&9Prefix&7> Enjoy your new prefix! &f" +instance.getConf().getuConfig().getDouble("Economy.chat.prefix-suffix.price") +" &7 was taken from your account."));
                            }else {
                                p.sendMessage(color("&cAn error has occurred. Contact an Administrator."));
                            }
                        }else {
                            setPrefix(result,p);
                            p.sendMessage(color("&9Prefix&7> Enjoy your new prefix! &f"));
                        }
                    }
                }else {
                    p.sendMessage(color("&7You aren't allowed to set the prefix."));
                }
            }else {
                sender.sendMessage(color("&7Please use /prefix <player> <prefix> for console."));
            }
        }
        return true;
    }
}
