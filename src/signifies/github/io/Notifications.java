package signifies.github.io;

import ru.tehkode.permissions.PermissionManager;


public class Notifications
{
    private boolean global;
    private boolean admin;
    private String prefix;
    private boolean broadcastConsole;
    private PermissionManager required;
    public Notifications(String prefix, PermissionManager required, boolean global, boolean broadcastConsole)
    {

       this.prefix = prefix;
       this.required = required;
       this.global = global;
       this.broadcastConsole = broadcastConsole;
    }

    public Notifications(String prefix, boolean global, boolean broadcastConsole)
    {
        this.prefix = prefix;
        this.global = global;
        this.broadcastConsole = broadcastConsole;
    }

    public boolean isGlobal()
    {
        return this.global;
    }
    public boolean isAdmin()
    {
        return admin;
    }


    public String getBroadcastPrefix()
    {
        return prefix;
    }

    public boolean isBroadcastConsole() {
        return broadcastConsole;
    }

    public PermissionManager getRequired() {
        return required;
    }

    public void setGlobal(boolean global)
    {
        this.global = global;
    }

    public void setBroadcastConsole(boolean broadcastConsole)
    {
        this.broadcastConsole = broadcastConsole;
    }

    public void setBroadcastPrefix(String prefix)
    {
        this.prefix = prefix;
    }

}