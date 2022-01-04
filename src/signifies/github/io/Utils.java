package signifies.github.io;

import commands.*;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Signifies on 3/14/2019 - 17:38.
 */
public class Utils extends JavaPlugin{

    public   Economy econ = null;
    private static Permission perms = null;
    private static Chat chat = null;

    public PluginDescriptionFile pdfFile = this.getDescription();
    private UtilsConfig uconfig;
    private ArrayList<UUID> notify = new ArrayList<>();
    private Warps warp = new Warps(this);
    private ArrayList<UUID> pvpToggle = new ArrayList<>();
    public ArrayList<UUID> isCMS = new ArrayList<>(); //moved this up here out of the constructor.
    public static boolean DEBUG = false;
    private CMSCommand cmsCommand;


    public void onEnable() {
        uconfig = new UtilsConfig(this);
        configuration();
        cmsCommand = new CMSCommand(this);
        loadWarps();
       if(!uconfig.getuConfig().getBoolean("Economy.enabled")) {
           Bukkit.getConsoleSender().sendMessage(Helper.prefix +ChatColor.RED +"[WARN] -> Economy is disabled in configuration. Enable it for economic use.");
       }else
       {
           if (!setupEconomy() ) {
               //log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
               getServer().getPluginManager().disablePlugin(this);
               return;
           }
       }
        setupPermissions();
        setupChat();

        System.out.println("EAT SHIT");

        registerCmd("util", new UtilCommand(this));
        registerCmd("spawn", new SpawnCommand(this));
        registerCmd("whitelist", new WhitelistCommand(this));
        registerCmd("chat", new ChatCommand(this));
        registerCmd("staffchat", new StaffBroadcast(this));
        registerCmd("prefix", new PrefixCommand(this));
        registerCmd("broadcast", new BroadcastCommand(this));
        registerCmd("color", new ColorCommand(this));
        registerCmd("pvp", new PVPCommand(this));
        registerCmd("cms", new CMSCommand(this));
        registerCmd("warp",new WarpCommand(this));
        registerCmd("setwarp", new SetWarpCommand(this));
        Bukkit.getServer().getPluginManager().registerEvents(new Events(this),this);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }

    void configuration() {
        uconfig.saveDefaultuConfig();
        uconfig.saveuConfig();

    }

    void loadWarps()
    {
       // Helper.log( "&aLoading Warps...",1);
        warp.saveDefaultWarpConfig();
        warp.saveWarpConfig();
    }

    private void registerCmd(String command, CommandExecutor commandExecutor) {
        Bukkit.getServer().getPluginCommand(command).setExecutor(commandExecutor);
    }

    public boolean permissionDefault() {
        return uconfig.getuConfig().getBoolean("perm-message.default");
    }

    public String getMessage() {
        return uconfig.getuConfig().getString("perm-message.format");
    }
    public ArrayList<UUID> getNotifications() {
        return notify;
    }

    public UtilsConfig getConf() {
        return uconfig;
    }

    public Warps getWarps()
    {
        return warp;
    }

    public HashMap<String, Long> cooldowns = new HashMap<>();


    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]) {
        if (sender instanceof Player) {
            Player p = (Player) sender;

            if (cmd.getName().equalsIgnoreCase("flagblue") && args.length < 1) {
                if (isCMS.contains(p.getUniqueId())) {
                    getCMS().CTFWarp(p, "bflag");
                    return true;
                }
            }

            if (cmd.getName().equalsIgnoreCase("flagred") && args.length < 1) {
                if (isCMS.contains(p.getUniqueId())) {
                    getCMS().CTFWarp(p, "rflag");
                    return true;
                }
            }
        }
        return true;
    }

    public static Permission getPermissions() {
        return perms;
    }

    public static Chat getChat() {
        return chat;
    }

    public ArrayList<UUID> getPVPToggle() {
        return this.pvpToggle;
    }

    public CMSCommand getCMS(){
        return cmsCommand;
    }

    public ArrayList<UUID> getCMSStatus() {
        return isCMS;
    }
}
