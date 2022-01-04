package signifies.github.io;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Signifies on 3/14/2019 - 17:45.
 */
public enum UtilPermissions {

    ACCESS("utils.access"),
    BYPASS_CHAT("utils.bypass.chat"),
    BYPASS_PLACE("utils.bypass.place"),
    BYPASS_BREAK("utils.bypass.break"),
    BYPASS_INTERACT("utils.bypass.interact"),
    BYPASS_DROP("utils.bypass.drop"),
    BYPASS_PICKUP("utils.bypass.pickup"),
    COMMAND_BYPASS("utils.bypass"),
    LIST_COMMAND("utils.list"),
    CHAT_CLEARSELF("utils.chat.clearself"),
    CHAT_CLEAR_OTHERS("utils.chat.clearothers"),
    CHAT("utils.chat"),
    CHAT_CLEAR("utils.chat.clear"),
    CHAT_DISABLE("utils.chat.disable"),
    CHAT_ENABLE("utils.chat.enable"),
    MESSAGE_COMMAND("utils.message"),
    BROADCAST("utils.alert"),
    COMMAND_KICK("utils.kick"),
    EDIT_COMMAND("utils.command.edit"),
    BYPASS_TPTOGGLE("utils.bypass.toggle"),
    COMMAND_BYPASS_TOGGLE("utils.bypass.toggle"),
    COMMAND_WHITELIST("utils.whitelist"),
    BYPASS_STATUS("utils.status"),
    MANGEMENT("utils.manger"),
    GAY("utils.gay"),
    SETSPAWN("utils.setspawn"),
    PVP_TOGGLE("utils.pvp.toggle"),
    CHAT_LOCATION("utils.chat.location"),
    CHAT_EXP("utils.chat.exp"),
    CHAT_COLOR("utils.chat.color"),
    CHAT_WORLD("utils.chat.world"),
    MOTD_SET("utils.motd.set"),
    CHAT_ITEM("utils.chat.item"),
    PREFIX_SELF("utils.prefix.self"),
    PREFIX_OTHERS("utils.prefix.others"),
    SUFFIX_SELF("utils.suffix"),
    MAINTENANCE("utils.maintenance"),
    STAFF_CHAT("utils.staffchat"),
    RELOAD("utils.reload"),
    NOTIFY("utils.whitelist.notify"),
    OVERRIDE("utils.whitelist.override"),
    NOTIFICATIONS("utils.notify"),
    CMS_COMMAND("CMS.access"),
    BUILD_SET_WARPS("warps.set"),
    BUILD_COMMAND_WARP("warps.warp"),
    BUILD_COMMAND_WARP_LIST("Warps.list"),
    PLUGIN_DONATOR_2 ("plugin.donator.2");



    private String key;

    UtilPermissions(String key) {
        this.key = key;
    }

    public boolean checkPermission(Player p){
        //System.out.println(check(p));
        return p.hasPermission(getKey());
    }

    public boolean checkPermission(CommandSender sender)
    {
        //System.out.println(check(sender));
        return sender.hasPermission(getKey());
    }

    public String getKey() {
        return key;
    }



    public String check(Player p)
    {
        String s = p.hasPermission(getKey()) ? "" : Helper.prefix +"" + ChatColor.RED +"Error, " + p.getName() + " does not have the permission: " + getKey();

        return s;
    }
    public String check(CommandSender p)
    {
        String s = p.hasPermission(getKey()) ? "" : Helper.prefix +"" + ChatColor.RED +"Error, " + p.getName() + " does not have the permission: " + getKey();
        return  s;
    }


}
