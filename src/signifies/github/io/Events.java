package signifies.github.io;

import commands.CMSCommand;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
/**
 * Created by Signifies on 3/14/2019 - 18:22.
 */
public class Events extends Helper implements Listener {

    private Utils instance;
    public Events(Utils utils) {
        this.instance = utils;
    }
    public HashMap<UUID, Long> cooldowns = new HashMap<>();


    @EventHandler
    public void chat (AsyncPlayerChatEvent event) {

        Player p = event.getPlayer();
        int cooldownTime = 2;//instance.getConf().getuConfig().getInt("Chat.custom-chat.Delay"); // Get number of seconds from wherever you want
        boolean chat = instance.getConf().getuConfig().getBoolean("Chat.Enabled");
        boolean useperm = instance.getConf().getuConfig().getBoolean("use-perm");
        boolean perm = UtilPermissions.CHAT.checkPermission(p);

        if(useperm && !perm){
            event.setCancelled(true);
            p.sendMessage(color(instance.getConf().getuConfig().getString("Chat.no-permission")));
        }else if(cooldowns.containsKey(p.getUniqueId())) {
            event.setCancelled(true);
            long secondsLeft = ((cooldowns.get(p.getUniqueId())/1000)+cooldownTime) - (System.currentTimeMillis()/1000);
            if(secondsLeft > 0) {
                event.setCancelled(true);
                p.sendMessage(color("&7You cannot speak for another &f"+ secondsLeft +" &7seconds!"));
            }else{
                event.setCancelled(false);
            }
        }else if(chat){
            event.setCancelled(false);
            cooldowns.put(p.getUniqueId(), System.currentTimeMillis());
        }else if(!UtilPermissions.BYPASS_STATUS.checkPermission(p)){
            event.setCancelled(true);
            p.sendMessage(color(instance.getConf().getuConfig().getString("Chat.chat-disabled")));
            return;
        }

        String message = event.getMessage();
        List<String> list = instance.getConf().getuConfig().getStringList("blocked-words");

        if(list.contains(message.toLowerCase().replaceAll("\\s", "").replaceAll("&[a-z0-9]", ""))){
            event.setCancelled(true);
            p.sendMessage(color("&4You just did a no no. "));
        }
        if(message.contains("-help"))
        {
            p.sendMessage(color("&8========== [Chat &b&Functions&8] &8=========="));
            p.sendMessage("");
            p.sendMessage(color("&b#name &8&l- &7Displays player's name in shout."));
            p.sendMessage(color("&b#location &8&l- &7Will display user location. "));
            p.sendMessage(color("&eColor formatting in shout: ") + "&1, &2, &3, &4, &5, &6, &7, &8, &9 ETC.");
            p.sendMessage(color("&b#world &8&l- &7Displays the players current world."));
            p.sendMessage(color("&b#exp &8&l- Displays players Exp level."));
            p.sendMessage(color("&2&nFor Permissions to each function please check the Authors site."));
            p.sendMessage(color("&9How to use color codes: &3&nhttp://minecraftcolorcodes.com/"));
            p.sendMessage(color("&8==================================="));
        }



        String location =  color("&7X:&a"+p.getLocation().getBlockX() +" &7Y&a:" +p.getLocation().getBlockY() + " &7Z&a:" + p.getLocation().getBlockZ() +"&r" );

        message = message.replace("#cms", ChatColor.GREEN +instance.getCMSStatus().listIterator().toString());

        if(UtilPermissions.CHAT_COLOR.checkPermission(p))
        {
            message = message.replace("&",ChatColor.translateAlternateColorCodes('&',""));
        }

        if(UtilPermissions.CHAT_WORLD.checkPermission(p))
        {
            message = message.replace("#world", p.getWorld().getName());
        }

        if(UtilPermissions.CHAT_EXP.checkPermission(p))
        {
            message = message.replace("#exp", ""+p.getExpToLevel());
        }

        if(message.contains("#bflag")) {

                event.setCancelled(true);
                TextComponent mt = new TextComponent( ChatColor.RED + p.getName()+ " " + ChatColor.WHITE+"stole" +ChatColor.BLUE + " Blue" +ChatColor.RESET+" flag!");
                if(instance.getCMSStatus().contains(p.getUniqueId())) {
                mt.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/flagblue"));
                mt.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder( "Teleport to "+ChatColor.BLUE+"Blue "+ChatColor.RESET+"flag!" ).create() ) );
            }
                p.spigot().sendMessage(mt);

        }

        if(message.contains("#rflag")) {

                event.setCancelled(true);
                TextComponent mt = new TextComponent( ChatColor.BLUE + p.getName()+ " " + ChatColor.WHITE+"stole" +ChatColor.RED + " Red" +ChatColor.RESET+" flag!");
            if(instance.getCMSStatus().contains(p.getUniqueId())){
                mt.setClickEvent( new ClickEvent( ClickEvent.Action.RUN_COMMAND,"/flagred") );
                mt.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Teleport to"+ChatColor.RED + " Red" +ChatColor.RESET+" flag!" ).create()));
            }
                p.spigot().sendMessage(mt);
            //testing adding this line

        }

        if(UtilPermissions.CHAT_LOCATION.checkPermission(p)) //TODO Add hover event here.
        {

           if(message.contains("#location")) {
               event.setCancelled(true);
               TextComponent msg = new TextComponent(p.getName()+"'s #Location");
               msg.setColor(net.md_5.bungee.api.ChatColor.GOLD);
               msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(location).create()));
               for(Player player: Bukkit.getServer().getOnlinePlayers()) {
                   player.spigot().sendMessage(msg);
               }
           }
        }

        if(!instance.getConf().getuConfig().getBoolean("Chat.custom-chat.Enabled")) return;

        String format = instance.getConf().getuConfig().getString("Chat.custom-chat.Format");
        format = format.replace("{name}", p.getName());
        format = format.replace("{msg}", message); //TODO
        String prettyWorld = p.getWorld().getName().contains("world_nether") ? color("&cnether&r ") : p.getWorld().getName();//changes the world name, fixed spacing issue as well.
        format = format.replace("{world}", prettyWorld);
       // format = format.replace("{waypoint}",msg.toString());
        format = format.replace("{UUID}", p.getUniqueId().toString());
        format = format.replace("{location}",location);
        format = format.replace("{prefix}", getPrefix(p));
        format = format.replace("{suffix}",getSuffix(p));
        //suffix
            event.setFormat(color(format));

    }
    @EventHandler
    public void serverPing(ServerListPingEvent event)
    {

        String pingmessage = color(instance.getConf().getuConfig().getString("MOTD.server-list"));

        pingmessage = pingmessage.replaceAll("&", "\u00A7");
        pingmessage = pingmessage.replace("#nl", "\n");
//        pingmessage = pingmessage.replace("%staff%",getStaff(main));
          pingmessage = pingmessage.replace("{users}",getUsers());
//        pingmessage = pingmessage.replace("%time%",getStamp().toString());

        String result = instance.getConf().getuConfig().getBoolean("Maintenance.enabled")
                ? color(instance.getConf().getuConfig().getString("Maintenance.msg"))
                : pingmessage;
        event.setMotd(result);
        //uitl.logToConsole("TEST: " +event.getAddress());
    }

    @EventHandler
    public void onCmd(PlayerCommandPreprocessEvent event)
    {

        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();

        String denied = instance.getConf().getuConfig().getString("restriction-msg");
        denied = denied.replace("{player}", p.getName());
        String message[] = event.getMessage().split(" ");
        String command = message[0];
        List<String> denyList = instance.getConf().getuConfig().getStringList("blocked-cmds");
//TODO Flagtp don't feel like doign an entire fucking command.

        if(denyList.contains(command))
        {
            if(p.isOp() || !UtilPermissions.COMMAND_BYPASS.checkPermission(p))
            {
                event.setCancelled(true);
                p.sendMessage(color(denied));
            }
        }else
        {
            event.setCancelled(false);
        }
    }

    boolean checkWhitelist()
    {
        return Bukkit.getServer().hasWhitelist();
    }

    //TODO for pvp interaction.
    @EventHandler
    //Might need to make a user data file eventually.
    //If the player is in the arraylist, their PVP is ON. If not it's OFF.
    public void onDamage(EntityDamageByEntityEvent event) {
        if(!instance.getConf().getuConfig().getBoolean("PVP.toggle-pvp")) return;
        log("&cCalling damage event",0);
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player attacker = (Player)event.getDamager();
            if(event.getEntity() == null) return;
            Player victim = (Player)event.getEntity();
            String msg = msg(instance.getConf().getuConfig().getString("PVP.message"),victim.getName(),attacker.getName());
            if(!instance.getPVPToggle().contains(attacker.getUniqueId()) || !instance.getPVPToggle().contains(victim.getUniqueId())) { //Also need to check if victim has pvp disabled.
                event.setCancelled(true);
                attacker.sendMessage(msg);
            }else {
                event.setCancelled(false);
            }
        }
    }


    /*
     if(cms.getCMSStatus().contains(event.getWhoClicked().getUniqueId())){
            log("&6CMS Event is called",1);

            if(item == null || !item.hasItemMeta()) {
                return;
            }else { //GetItemInHand method??
                if(type.isLeftClick() || type.isRightClick() && item.getItemMeta().getDisplayName().equalsIgnoreCase("RED")){
                    event.setCancelled(true);
                    event.getWhoClicked().sendMessage(color("&cTeleported to &cRED's &fflag Room."));
                    log("The click worked and we see the message.",1);
                }
            }
        }
     */

    @EventHandler
    public void join(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        event.setJoinMessage(null);
        String format = instance.getConf().getuConfig().getString("Messages.join");
        format = format.replace("{player}", p.getName());
        format = format.replace("{display_name}", p.getDisplayName());
        format = format.replace("{uuid}", p.getUniqueId().toString());
        format = format.replace("{suffix}", getSuffix(p));
        format = format.replace("{prefix}",getPrefix(p));
        Bukkit.getServer().broadcastMessage(color(format));
        List<String> motd = instance.getConf().getuConfig().getStringList("Messages.motd");
        sendText(motd,p);

    }

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        event.setQuitMessage(null);
        String format = instance.getConf().getuConfig().getString("Messages.quit");
        format = format.replace("{player}", p.getName());
        format = format.replace("{display_name}", p.getDisplayName());
        format = format.replace("{uuid}", p.getUniqueId().toString());
        format = format.replace("{suffix}", getSuffix(p));
        format = format.replace("{prefix}",getPrefix(p));
        Bukkit.getServer().broadcastMessage(color(format));

    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player p = event.getPlayer();
        UUID uuid = p.getUniqueId();
        String config = instance.getConf().getuConfig().getString("Whitelist.kick-message");
        config = config.replace("{name}", p.getName());
        config = config.replace("{uuid}", "" + uuid);
        config = config.replace("\n", "\n");
        String alert = instance.getConf().getuConfig().getString("Whitelist.whitelist-alert");
        alert = alert.replace("{name}", p.getName());
        alert = alert.replace("{uuid}", "" + uuid);

        String result = instance.getConf().getuConfig().getBoolean("Maintenance.enabled") ? instance.getConf().getuConfig().getString("Maintenance.msg") : config;

        // IF WL = TRUE; THEN DO ALLOWANCE; THEN DO DISALLOWANCE.
        // IF WL = TRUE; AND P HAS PERM; THEN ALLOW

        if (checkWhitelist()) {
            /*This logic is important, because we want them to join regardless if they are whitelisted if permission specific control is enabled.*/
            if (checkWhitelist() && UtilPermissions.OVERRIDE.checkPermission(p) || (instance.getConf().getuConfig().getBoolean("Whitelist.use-permission") && p.hasPermission(instance.getConf().getuConfig().getString("Whitelist.permission")))) {
                event.setResult(PlayerLoginEvent.Result.ALLOWED);
            }else if(!p.isWhitelisted()) {
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.translateAlternateColorCodes('&', result));
                Bukkit.getServer().getConsoleSender().sendMessage(color(alert));
                for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
                    if (UtilPermissions.NOTIFY.checkPermission(staff)) {
                        if (p.isWhitelisted()) {
                            return;
                        }
                        staff.sendMessage(color(alert));
                    }
                }
            }
        }
    }
}