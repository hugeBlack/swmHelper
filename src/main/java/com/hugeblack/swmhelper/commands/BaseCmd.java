package com.hugeblack.swmhelper.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class BaseCmd {
    public String args;
    public String argsAlias;
    public boolean enabled;
    public String usage;

    public BaseCmd(String args, String argsAlias, Boolean enabled, String usage) {
        this.args = args;
        this.argsAlias = argsAlias;
        this.enabled = enabled;
        this.usage = usage;
    }

    public abstract String execute(Player player, Command cmd, String label, String[] args);

    public List<String> tabCompleter(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}