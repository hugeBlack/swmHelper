package com.hugeblack.swmhelper.commands;

import com.grinderwolf.swm.api.exceptions.CorruptedWorldException;
import com.grinderwolf.swm.api.exceptions.NewerFormatException;
import com.grinderwolf.swm.api.exceptions.UnknownWorldException;
import com.grinderwolf.swm.api.exceptions.WorldInUseException;
import com.grinderwolf.swm.api.loaders.SlimeLoader;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.grinderwolf.swm.plugin.config.ConfigManager;
import com.grinderwolf.swm.plugin.config.WorldData;
import com.grinderwolf.swm.plugin.config.WorldsConfig;
import com.hugeblack.swmhelper.SwmHelper;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class LoadAll extends BaseCmd{
    public LoadAll() {
        super("loadall", "la", true, "/swmh loadall [isReadOnly] [source]");
    }

    @Override
    public String execute(Player player, Command cmd, String label, String[] args) {
        boolean isReadOnly = false;
        boolean isReadOnlySpecified = false;
        if(args.length>1){
            if(args[1].equals("true")) {
                isReadOnly = true;
                isReadOnlySpecified = true;
            }
            else if(args[1].equals("false")) {
                isReadOnly = false;
                isReadOnlySpecified = true;
            }
            else if(args[1].equals("depends"))
                isReadOnlySpecified = false;
            else return "arg2 should be a boolean.";
        }
        SlimeLoader slimeLoader = SwmHelper.swmPlugin.getLoader(args.length<3?"file":args[1]);
        if(slimeLoader==null) return "source not found.";
        WorldsConfig worldsConfig = ConfigManager.getWorldConfig();
        try {
            for(String worldName: slimeLoader.listWorlds()){
                WorldData worldData = worldsConfig.getWorlds().get(worldName);
                // Test if the world already exists in memory
                World world = Bukkit.getWorld(worldName);
                if (world != null) continue;
                // If world not already loaded, we fetch & load world from SWM
                try {
                    SlimeWorld slimeWorld = SwmHelper.swmPlugin.loadWorld(slimeLoader, worldName, isReadOnlySpecified?isReadOnly:worldData.isReadOnly(), worldData.toPropertyMap());
                    SwmHelper.swmPlugin.generateWorld(slimeWorld);
                } catch (IOException | CorruptedWorldException | WorldInUseException | NewerFormatException |
                         UnknownWorldException e) {
                    e.printStackTrace();
                    return "error occurred while loading"+worldName;
                }
                player.sendMessage(worldName+" loaded.");
            }
        } catch (Exception e) {
            return "loader io error.";
        }
        return null;
    }

    @Override
    public List<String> tabCompleter(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==2) return new LinkedList<>(Arrays.asList("true","false","depends"));
        else if(args.length==3) return new LinkedList<>(Arrays.asList("file"));
        return null;
    }
}
