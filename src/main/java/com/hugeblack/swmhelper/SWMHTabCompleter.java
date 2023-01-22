package com.hugeblack.swmhelper;

import com.google.common.collect.Lists;
import com.hugeblack.swmhelper.commands.BaseCmd;
import com.hugeblack.swmhelper.commands.CmdManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Iterator;
import java.util.List;

public class SWMHTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> ans;
        if (args.length == 1) {
            ans = Lists.newArrayList();
            for (BaseCmd cmd : CmdManager.commands) {
                if (cmd.args.startsWith(args[0])) ans.add(cmd.args);
            }
            return ans;
        } else {
            for (BaseCmd cmd : CmdManager.commands) {
                if (cmd.args.equals(args[0]) || cmd.argsAlias.equals(args[0])) {
                    ans = cmd.tabCompleter(sender, command, label, args);
                    if(ans ==null) return null;
                    Iterator<String> it = ans.iterator();
                    while(it.hasNext()) {
                        if (!it.next().startsWith(args[args.length - 1])) it.remove();
                    }
                    return  ans;
                }
            }
        }
        return null;
    }
}