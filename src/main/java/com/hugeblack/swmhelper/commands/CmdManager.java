package com.hugeblack.swmhelper.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CmdManager implements CommandExecutor {
    public static ArrayList<BaseCmd> commands = new ArrayList<>();

    static {
        commands.add(new GameRuleEditor());
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        BaseCmd cmdObj = null;
        for (BaseCmd nowCmd : commands) {
            if(nowCmd.args.equals(args[0])){
                cmdObj = nowCmd;
                break;
            }
        }
        if(cmdObj==null){
            sender.sendMessage("command not fount");
        }
        String ans = cmdObj.execute((Player) sender,command,label,args);
        if(ans!=null) sender.sendMessage(ans);
        return true;
    }
}
