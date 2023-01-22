package com.hugeblack.swmhelper;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.api.world.SlimeWorld;
import com.hugeblack.swmhelper.commands.CmdManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class SwmHelper extends JavaPlugin {
    public static SlimePlugin swmPlugin= null;
    @Override
    public void onEnable() {
        // Plugin startup logic
        swmPlugin = (SlimePlugin) Bukkit.getPluginManager().getPlugin("SlimeWorldManager");
        SWMHTabCompleter tc = new SWMHTabCompleter();
        CmdManager cm = new CmdManager();
        getCommand("swmh").setTabCompleter(tc);
        getCommand("swmh").setExecutor(cm);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
