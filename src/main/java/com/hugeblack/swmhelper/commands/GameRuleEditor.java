package com.hugeblack.swmhelper.commands;

import com.grinderwolf.swm.api.world.SlimeWorld;
import com.hugeblack.swmhelper.SwmHelper;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class GameRuleEditor extends BaseCmd{
    public GameRuleEditor() {
        super("gamerule", "gr", true, "/swmh gamerule <world> <rule> <value>");
    }

    @Override
    public String execute(Player player, Command cmd, String label, String[] args) {
        if(args.length!=4) return "arg count mismatch";
        boolean isAll = args[1].equals("__all__");
        if(isAll){
            GameRule rule = GameRule.getByName(args[2]);
            if(rule==null) return "rule not found";
            if(rule.getType()== Boolean.class){
                for(SlimeWorld slimeWorld:SwmHelper.swmPlugin.getLoadedWorlds()){
                    if(slimeWorld.isReadOnly()) {
                        player.sendMessage("world "+slimeWorld.getName()+" is readonly.");
                    }else{
                        World bukkitWorld = Bukkit.getWorld(slimeWorld.getName());
                        if(bukkitWorld==null) continue;
                        if(args[3].equals("true")) bukkitWorld.setGameRule(rule,true);
                        else if(args[3].equals("false")) bukkitWorld.setGameRule(rule,false);
                        else return "boolean value required.";
                    }
                    player.sendMessage(slimeWorld.getName()+"'s rule modified.");
                }
                return null;
            }else if(rule.getType()== Integer.class){
                for(SlimeWorld slimeWorld:SwmHelper.swmPlugin.getLoadedWorlds()) {
                    World bukkitWorld = Bukkit.getWorld(slimeWorld.getName());
                    if(bukkitWorld==null) continue;
                    try {
                        int val = Integer.parseInt(args[3]);
                        bukkitWorld.setGameRule(rule, val);
                        return null;
                    } catch (NumberFormatException e) {
                        return "integer value required.";
                    }
                }
            }
        }
        SlimeWorld world = SwmHelper.swmPlugin.getWorld(args[1]);
        World bukkitWorld = Bukkit.getWorld(args[1]);
        if(world==null) return "world not found or not a slime world.";
        if(bukkitWorld==null) return "bukkit world not found?This should not happen.";
        if(world.isReadOnly()) return "world is readonly.";
        GameRule rule = GameRule.getByName(args[2]);
        if(rule==null) return "rule not found";
        if(rule.getType()== Boolean.class){
            if(args[3].equals("true")) bukkitWorld.setGameRule(rule,true);
            else if(args[3].equals("false")) bukkitWorld.setGameRule(rule,false);
            else return "boolean value required.";
            return null;
        }else if(rule.getType()== Integer.class){
            try{
                int val = Integer.parseInt(args[3]);
                bukkitWorld.setGameRule(rule,val);
                return null;
            }catch (NumberFormatException e){
                return "integer value required.";
            }
        }
        player.sendMessage(args[1]+"'s rule modified.");
        return null;
    }

    public List<String> tabCompleter(CommandSender sender, Command command, String label, String[] args) {

        if(args.length==2){
            LinkedList<String> ans = new LinkedList<>();
            for(SlimeWorld slimeWorld: SwmHelper.swmPlugin.getLoadedWorlds()){
                ans.add(slimeWorld.getName());
            }
            ans.add("__all__");
            return ans;
        }else if(args.length==3){
            LinkedList<String> ans = new LinkedList<>();
            for(GameRule rule: GameRule.values()){
                ans.add(rule.getName());
            }
            return ans;
        }else if(args.length==4){
            GameRule rule = GameRule.getByName(args[2]);
            if(rule==null) return null;
            if(rule.getType()== Boolean.class) return new LinkedList<>(Arrays.asList("true","false"));
        }
        return null;
    }
}
