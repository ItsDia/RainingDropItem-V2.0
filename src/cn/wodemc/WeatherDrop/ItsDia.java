package cn.wodemc.WeatherDrop;

import org.bukkit.plugin.java.*;
import org.bukkit.*;
import java.io.*;
import org.bukkit.plugin.*;
import org.bukkit.event.weather.*;
import org.bukkit.event.*;
import org.bukkit.scheduler.*;
import org.bukkit.block.*;
import java.util.*;
import org.bukkit.inventory.*;
import org.bukkit.entity.*;

public final class ItsDia extends JavaPlugin implements Listener
{
    List<World> list;
    List<Integer> items;
    
    public ItsDia() {
        this.list = new ArrayList<World>();
        this.items = new ArrayList<Integer>();
    }
    
    public void onEnable() {
        final File file = new File(this.getDataFolder(), "config.yml");
        if (!file.exists()) {
            this.saveDefaultConfig();
        }
        this.reloadConfig();
        this.items = (List<Integer>)this.getConfig().getIntegerList("items");
        final BukkitScheduler bs = this.getServer().getScheduler();
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
        bs.scheduleSyncRepeatingTask((Plugin)this, (Runnable)new Task(this), 0L, this.getConfig().getInt("time") * 20L);
    }
    
    public void onDisable() {
    }
    
    @EventHandler
    public void WeatherEvents(final WeatherChangeEvent e) {
        if (!e.toWeatherState()) {
            return;
        }
        if (!this.list.contains(e.getWorld())) {
            this.list.add(e.getWorld());
        }
    }
    
    class Task extends BukkitRunnable
    {
        private ItsDia plugin;
        int Number;
        
        public Task(final ItsDia plugin) {
            this.Number = 5;
            this.plugin = plugin;
        }
        
        public void seti(final int i) {
            this.Number = i;
        }
        
        public void run() {
            if (this.Number == 0) {
                this.cancel();
                return;
            }
            final Player[] arr$;
            @SuppressWarnings({ "deprecation", "unused" })
			final Player[] players = arr$ = this.plugin.getServer().getOnlinePlayers();
            final int len$ = arr$.length;
            final int i$ = 0;
            if (i$ >= len$) {
                return;
            }
            final Player p = arr$[i$];
            if (!ItsDia.this.list.contains(p.getWorld()) || !p.getWorld().hasStorm()) {
                return;
            }
            final Biome b = p.getWorld().getBiome((int)p.getLocation().getX(), (int)p.getLocation().getZ());
            if (b != Biome.DESERT) {
                final Random random = new Random();
                @SuppressWarnings("deprecation")
				final ItemStack is = new ItemStack((int)ItsDia.this.items.get(random.nextInt(ItsDia.this.items.size()) % (ItsDia.this.items.size() - 0 + 1) + 0));
                p.getInventory().addItem(new ItemStack[] { is });
                if (this.plugin.getConfig().getBoolean("enable")) {
                    p.sendMessage(this.plugin.getConfig().getString("messages").replace("&", "§"));
                }
            }
        }
    }
}