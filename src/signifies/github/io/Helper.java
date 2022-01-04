package signifies.github.io;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionType;
import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.PermissionsUserData;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by Signifies on 3/14/2019 - 18:07.
 */
public class Helper {
    /**
     * Plugin prefix.
     */
    public static String prefix = ChatColor.translateAlternateColorCodes('&',"&bUtils&7->");

    String author = "9c5dd792-dcb3-443b-ac6c-605903231eb2";
    private String permission = color("&cUnknown command. Type \"/help\" for help.");

    public boolean checkAuthor(UUID uuid)
    {
        return uuid.toString().equals(author);
    }

    private Notifications staff = new Notifications(Action.NOTIFY_ADMIN.getMessage(), false,true);

    public void displayAuthInfo(Player p)
    {
        if(checkAuthor(p.getUniqueId()))
        {
            p.sendMessage(color("&a&l&oHello, &7"+ p.getUniqueId().toString() +"\n &aThis server is using ") + getPrefix());
        }
    }

    public String defaultMessage(boolean value, String msg)
    {
        return value ? color(permission) : color(msg);
    }

    public ArrayList<String> commandList()
    {
        ArrayList<String> value = new ArrayList<String>();

        value.add("     &f----- &7Util Commands &f-----");
        value.add("&a/Util help");
        value.add("&a/Util &7about");
        value.add("&a/util &finfo");
        value.add("");
        value.add("&7Util commands and features");
        value.add("&7Use &a#&flocation &7to display your location in chat.");
        value.add("&7chat");
        value.add("&a/whitelist");
        value.add("&a/spawn");
        value.add("&a/util &fsetspawn");
        value.add("&a/util &fsetmotd");
        value.add("&a/util &freload");
        value.add("&a/util &fmaintenance");
        value.add("&a/sus");
        value.add("&a-help");
        return value;
    }

    public void sendText(List<String> text, Player sender)
    {
        int amt = Bukkit.getServer().getOnlinePlayers().size();
        int max = Bukkit.getServer().getMaxPlayers();

        for(String txt: text)
        {
            txt = txt.replace("{online_players}", ""+amt);
            txt = txt.replace("{max_players}", ""+max);
            txt = txt.replace("{player}",sender.getName());
            txt = txt.replace("{uuid}",sender.getUniqueId().toString());
            txt = txt.replace("{display_name}",sender.getDisplayName());
            txt = txt.replace("{IP}", sender.getAddress().toString());
            txt = txt.replace("{time}",getStamp().toString());
            sender.sendMessage(color(txt));
        }
    }
    public void sendText(ArrayList<String> text, CommandSender sender)
    {
        for(String txt: text)
        {
            txt = txt.replace("{player}",sender.getName());
            sender.sendMessage(color(txt));
        }
    }
    public void sendText(ArrayList<String> text, Player sender)
    {
        for(String txt: text)
        {
            txt = txt.replace("{player}",sender.getName());
            sender.sendMessage(color(txt));
        }
    }

    /**
     * Gets the set plugin prefix.
     *
     * @return
     */
    public String getPrefix()
    {
        return prefix;
    }

    public void clearPlayer(Player p)
    {
        for(int i=0; i < 100; i++)
        {
            p.sendMessage("");
        }
        p.sendMessage(color("&7Your chat has been &7&nCleared&c, by an Admin, &a&n" + p.getName()));
    }

    public void selfClear(CommandSender sender) {
        for(int i=0; i <100; i++) {
            sender.sendMessage("");
        }
        sender.sendMessage( ChatColor.GRAY + "You have cleared your own chat, "+ ChatColor.GREEN +sender.getName());
    }

    public void clear() {
        for(Player p : Bukkit.getServer().getOnlinePlayers())
        {
            for(int i=0; i <100; i ++)
            {
                p.sendMessage("");
            }
        }
        Bukkit.broadcastMessage(color("&7The chat has been &acleared."));
    }




    /**
     * Method that handles all the color formatting
     *
     * @param message
     * @return
     */
    public static String color(String message) {
        String msg =  message;
//        msg = msg.replace("&", "§");
//        ChatColor.translateAlternateColorCodes('&',msg);
        return ChatColor.translateAlternateColorCodes('&',msg);
    }

    public String check(boolean value, String name)
    {
        return  value ? name +ChatColor.GREEN +" [Enabled]"  : name + ChatColor.DARK_RED+" [Disabled]";
    }
    public String msg(String configPath, boolean removeColor, String[] values)
    {
        String result = configPath;
        if(result !=null)
        {
            if(removeColor)
            {
                result = color(result);
            }
            for(int k=0; k < values.length; k++)
            {
                String replaced = (values[k] != null) ? values[k] : "NULL";
                result = result.replace("{"+ k +"}",replaced);
            }
            return removeColor ? result : color(result);
        }
       log("Error finding a message!",1);
        return null;
    }

    public String msg(String path, String... replaced)
    {
        return msg(path,false,replaced);
    }

    public String msg(String path)
    {
        return msg(path,false,new String[0]);
    }

    /**
     * TAGS: {0}:name {1}:world {2}:rank {1}:message {3}:admin
     * @param admin The user that triggers the message.
     * @param notification Notification we need to broadcast.
     */
    public void adminNotifications(String admin, String notification)
    {
        for(Player player : Bukkit.getServer().getOnlinePlayers())
        {
            String format = msg(staff.getBroadcastPrefix(),player.getName(),player.getWorld().getName(),notification, admin);
            if(UtilPermissions.NOTIFICATIONS.checkPermission(player))
            {
                Bukkit.getServer().getConsoleSender().sendMessage(format);
                player.sendMessage(format);
            }
        }
    }

    /*

    **
     * //TODO make sure to update tag system to suport internal functions.
     * TAGS: {0}:name {1}:world {2}:rank {3}:message {4}:admin
     * @param admin The user that triggers the message.
     * @param notification Notification we need to broadcast.
     **/

    public void adminNotifications(String admin, String notification, UtilPermissions perms)
    {
        for(Player player : Bukkit.getServer().getOnlinePlayers())
        {
            String format = msg(staff.getBroadcastPrefix(),notification, admin);
            if(perms.checkPermission(player))
            {
                Bukkit.getServer().getConsoleSender().sendMessage(format);
                player.sendMessage(format);
            }
        }
    }


    /**
     *
     *    we want to send. Variables: {0}: name {1}: rank {2}: notifications
     *
    //TODO Need to have a broadcast that accepts parameters that aren't default.
    public void globalNotifications(String notifications)
    {
        for(Player player : Bukkit.getServer().getOnlinePlayers())
        {
            String format = msg(global.getBroadcastPrefix(),player.getName(), User.getRank(player).toString(), notifications);

            if(global.isGlobal())
            {
                player.sendMessage(format);
                if(global.isBroadcastConsole())
                {
                    Bukkit.getServer().getConsoleSender().sendMessage(format);
                }
            }else
            {
                //if not global -> admin
                adminNotifications("",notifications);
            }
        }
    }

    public void globalNotifications(String trigger, String notifications)
    {
        for(Player player : Bukkit.getServer().getOnlinePlayers())
        {
            String format = msg(global.getBroadcastPrefix(),player.getName(), User.getRank(player).toString(), notifications);

            if(global.isGlobal())
            {
                player.sendMessage(format);
                if(global.isBroadcastConsole())
                {
                    Bukkit.getServer().getConsoleSender().sendMessage(format);
                }
            }else
            {
                //if not global -> admin
                adminNotifications(trigger,notifications);
            }
        }
    }

    public void customAdminNotification(String prefix, String user, String notification)
    {
        for(Player player : Bukkit.getServer().getOnlinePlayers())
        {
            String format = msg(prefix+"",player.getName(),player.getWorld().getName(), User.getRank(player).toString(),notification, user);

            if(User.isPermissible(player,Rank.MOD))
            {
                player.sendMessage(format);
                Bukkit.getServer().getConsoleSender().sendMessage(format);
            }
        }

    }


    public void customNotification(String prefix, String user, String notification, boolean isglobal, boolean isconsole)
    {
        for(Player player :  Bukkit.getServer().getOnlinePlayers())
        {
            String format = msg(prefix +" "+notification,player.getName(), User.getRank(player).toString(), notification);
            player.sendMessage(format);
            if(isglobal)
            {
                player.sendMessage(format);
                if(isconsole)
                {
                    Bukkit.getServer().getConsoleSender().sendMessage(format);
                }
            }else
            {
                customAdminNotification(prefix,user,notification);
            }
        }
    }

    */



    Calendar cal = Calendar.getInstance();
    Date now = cal.getTime();
    public java.sql.Timestamp stamp = new java.sql.Timestamp(now.getTime());
    public java.sql.Timestamp getStamp() {
        return stamp;
    }

    public void setArmor(Player p)
    {
        p.getInventory().setHelmet(createItem(Material.CHAINMAIL_HELMET,1,"&cHat"));
        p.getInventory().setChestplate(createItem(Material.CHAINMAIL_CHESTPLATE,1,"&cChestplate"));
        p.getInventory().setLeggings(createItem(Material.CHAINMAIL_LEGGINGS,1,"&cLeggings"));
        p.getInventory().setBoots(createItem(Material.CHAINMAIL_BOOTS,1,"&cBoots"));
    }

    public ItemStack createItem(Material mat, int amount, String name) {
        ItemStack is = new ItemStack(mat,amount);
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(color(name));
        is.setItemMeta(meta);
        //
        return is;
    }

    public void clearArmor(Player p)
    {
        p.getInventory().setHelmet(null);
        p.getInventory().setChestplate(null);
        p.getInventory().setLeggings(null);
        p.getInventory().setBoots(null);
    }

    public void information(Plugin plugin, Player author)
    {
            String text = color("&0---- &6Your Information &0----");
            author.sendMessage(color(text));
            author.sendMessage(color("&cIP: &a"+ author.getAddress().toString()));
            author.sendMessage(color("&cUUID: &3"+author.getUniqueId().toString()));
            author.sendMessage(color("&cName: &e"+author.getName()));
            String format = color("&0------------------------");
            author.sendMessage(format);
            String os =  System.getProperty("os.name");
            author.sendMessage(color("&4OS: &a&l"+os));
            double freeD=new File(plugin.getDataFolder()+"/..").getFreeSpace()/1073741824;
            double totalD=new File(plugin.getDataFolder()+"/..").getTotalSpace()/1073741824;
            author.sendMessage(ChatColor.AQUA+"Disk space used: "+ChatColor.GREEN+new DecimalFormat("#.##").format(totalD-freeD)+ChatColor.YELLOW+"/"+new DecimalFormat("#.##").format(totalD)+ChatColor.YELLOW+" GB ("+new DecimalFormat("#.##").format(((totalD-freeD)/totalD)*100)+"% used)");

            double free = Runtime.getRuntime().freeMemory() / 1048576;
            double total = Runtime.getRuntime().totalMemory() / 1048576;
            author.sendMessage(ChatColor.RED + "RAM Used: " + ChatColor.GREEN + new DecimalFormat("#.###").format(total - free) + ChatColor.YELLOW + "/" + new DecimalFormat("#.###").format(total) + ChatColor.YELLOW + " MB (" + new DecimalFormat("#.##").format(((total - free) / total) * 100) + "% used)");
            author.sendMessage(ChatColor.RED+"Number of cores: "+ChatColor.YELLOW+Runtime.getRuntime().availableProcessors());
            author.sendMessage(ChatColor.RED+"Java version: "+ChatColor.YELLOW+System.getProperty("java.version"));
            int c=0;
            for(World w:Bukkit.getWorlds())c+=w.getLoadedChunks().length;
            author.sendMessage(ChatColor.RED+"Chunks loaded: "+ChatColor.YELLOW+c);
    }
    public String getUsers()
    {
        StringBuilder sb = new StringBuilder();
        for(Player p : Bukkit.getServer().getOnlinePlayers())
        {
            sb.append(p.getName() + ", ");
        }
        return ""+sb.toString();
    }

    /**
     * Runs permissionsEx default permissions setup, if the default permissions are not the only system being used.
     * @return
     * @throws NullPointerException
     */
    public ArrayList<String> pexSetup() throws NullPointerException
    {
        ArrayList<String> value = new ArrayList<>();
       // value.add("pex group Guest create");
        //value.add("pex group Guest prefix &7Guest");
        //value.add("pex group Guest add Utils.Guest");
        //value.add("pex group Guest rank 50");
        value.add("pex group Member create");
        value.add("pex group Member prefix &2Member");
        value.add("pex group Member add Utils.member");
        value.add("pex group Member rank 40");
        value.add("pex group Builder create");
        value.add("pex group Builder prefix &bBuilder");
        value.add("pex group Builder add Build.Builder");
        value.add("pex group Builder rank 30");
        value.add("pex group Mod create");
        value.add("pex group Mod prefix &dMod");
        value.add("pex group Mod add Build.mod");
        value.add("pex group Mod rank 20");
        value.add("pex group Admin create");
        value.add("pex group Admin prefix &cAdmin");
        value.add("pex group Admin add Builder.admin");
        value.add("pex group Admin rank 10");
        value.add("pex group Developer create");
        value.add("pex group Developer prefix &6Dev");
        value.add("pex group Developer add '*'");
        value.add("pex group Developer rank 5");
        value.add("pex group Owner create");
        value.add("pex group Owner prefix &4Owner");
        value.add("pex group Owner add '*'");
        value.add("pex group Owner rank 1");
        value.add("pex groups");
        System.out.println(color("%prefix% &4PermissionsEx File has been created with ranks!"));

        return value;

    }

    public void runCommands(ArrayList<String> commands)
    {
        for(String cmd : commands)
        {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),cmd);
        }
    }

    public void runCommand(String cmd) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
        log("&4[!]Command execution: &6" +cmd,1);
    }

    public void runCommands(List<String> commands, Player p, String inform) {
        p.sendMessage(color(inform));
        for (String cmd : commands)
        {
            cmd = cmd.replace("{player}",p.getName());
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), cmd);

        }
    }

    public void executePexsetup(){
            runCommands(pexSetup());
    }

    public void log(String msg, int priority) {
        if(Utils.DEBUG || priority > 0) {
           Bukkit.getServer().getConsoleSender().sendMessage(prefix + color("&f[&4LOG&f]&r &6" + msg));
        }
    }

    public ArrayList<String> warps(List<String> s)
    {
        ArrayList<String> value = new ArrayList<>();
        value.add("     &f----- &bWarps &f-----");
        value.add("&6"+s);
        value.add("&7------------------");
        return value;
    }

    //TODO our get prefix method will be the replacement value inside our chat API.

    public void enforceWhitelist(CommandSender staff) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if(!p.isWhitelisted() && !(UtilPermissions.BYPASS_STATUS.checkPermission(p))) {
                p.kickPlayer(color("&4[!] Server whitelist has been enforced!"));
            }
        }
        staff.sendMessage(color("&7The whitelist has been &aenforced."));
        //TODO Notifications API here.
        adminNotifications(staff.getName(), "Enforced the whitelist.");
    }
    public void enforceWhitelist(CommandSender staff, String msg) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if(!p.isWhitelisted() && !(UtilPermissions.BYPASS_STATUS.checkPermission(p))) {
                p.kickPlayer(color(msg));
            }
        }
        //TODO Notifications API here.
    }


    public String getPrefix(Player player) {

        log("Vault prefix: "+Utils.getChat().getPlayerPrefix(player),0);

        //return condition ? "" : "";

        return Utils.getChat().getPlayerPrefix(player);
    }

    //
    public String getSuffix(Player player) {
        return Utils.getChat().getPlayerSuffix(player);
    }
    //

    @Deprecated
    public boolean hasCustomPrefix(Player player) {
        PermissionUser user = PermissionsEx.getUser(player);
        return user.getOwnPrefix() != null;
    }

    /*
    @Deprecated
    public String getPrefix(Player player) {
        PermissionUser user = PermissionsEx.getUser(player);
        log("GetOwnPrefix for: "+player.getName()+"-> " +user.getOwnPrefix(),1);
        log("Getprefix for: "+player.getName()+"-> " +user.getPrefix(),1);

        return hasCustomPrefix(player) ? user.getOwnPrefix() : user.getPrefix(); // neither one returns a group prefix. We'll figure it out tomorrow.
    }

*/

    public void setPrefix(String prefix, Player player) {
        Utils.getChat().setPlayerPrefix(player,prefix);
        log("Prefix> "+prefix,0);
    }

    /*
    public void setPrefix(String prefix, Player player) {
        log("setPrefix is being called...", 0);
        PermissionUser user = PermissionsEx.getUser(player);
        if(prefix.contains("\"\"")) {
            runCommand("pex user " +player.getName() + " prefix \"\"");
        }
        user.setPrefix(prefix,null);
        player.sendMessage(color("&7You have set your prefix to: " +prefix));
        log("Prefix: " +prefix,1);
    }

*/

    public void setSuffix(String suffix, Player player) {
        PermissionUser user = PermissionsEx.getUser(player);
        Utils.getChat().setPlayerSuffix(player,suffix);
        player.sendMessage(color("&7You have set your name color to: " +suffix+ player.getName() +"&7."));
    }



}
