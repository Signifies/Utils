package signifies.github.io;

public enum Action
{
    SPEAK(Helper.color("")),
    PLACE(Helper.color("")),
    DESTROY(Helper.color("")),
    INTERACT(Helper.color("")),
    PICKUP(Helper.color("")),
    DROP(Helper.color(""))
    ,BAN(Helper.color("&4[Ban]&r"))
    ,KICK(Helper.color("&e[Kick]&r")),
    MUTE(Helper.color("&9[Mute]&r")),
    REPORT(Helper.color("&6[Report]&r")),
    WARN(Helper.color("&c[Warn]&r")),
    STANDARD_ALERT("&c&l>>&6&l!!&c&l<< &c*** {} &c*** &c&l>>&6&l!!&c&l<<"),
    NOTIFY_ADMIN("&7[&4&lA&r&7] &7&o@{3}> {2}"),
    NOTIFY_GLOBAL("&7[&b&lN&r&7] {2}");
    String msg;
    Action (String msg)
    {
        this.msg = msg;
    }

    public String getMessage()
    {
        return msg;
    }

    public void setMessage(String format)
    {
        this.msg = format;
    }


}