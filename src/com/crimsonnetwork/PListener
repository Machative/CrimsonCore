package com.crimsonnetwork;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
public class PListener implements Listener{
	public String loreID(List<String> lore) {
		return lore.get(lore.size()-1);
	}
	public static File playerFile(Player p) {
		return Main.getPlayerFile(p);
	}
	public static YamlConfiguration playerData(File playerFile) {
		return YamlConfiguration.loadConfiguration(playerFile);
	}
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerJoinForFirstTime(PlayerJoinEvent e) {
		if(!(playerFile(e.getPlayer()).exists())) {
			File f = new File(Main.plugin.getDataFolder()+File.separator+"CrimsonCore","Crimson_"+e.getPlayer().getUniqueId().toString()+".yml");
			FileConfiguration playerdata = YamlConfiguration.loadConfiguration(f);
			try {
				playerdata.createSection("info");
				playerdata.set("info.name",e.getPlayer().getName());
				
				playerdata.createSection("general");
				playerdata.set("general.element","none");
				
				playerdata.save(f);
				System.out.println("Created data file for "+e.getPlayer().getName());
			}catch(IOException exception) {
				exception.printStackTrace();
			}
			Main.chooseElement.clear();
			Main.chooseElement.setItem(2,Main.createItem(new ItemStack(Material.BLAZE_POWDER),
					""+ChatColor.GOLD+"Choose Fire!",
					new String[]{ChatColor.RED+"Infinite Strength II",""+ChatColor.GRAY+ChatColor.ITALIC+"Note: You can change this with /element."}));
			Main.chooseElement.setItem(6,Main.createItem(new ItemStack(Material.ICE),
					""+ChatColor.AQUA+"Choose Ice!",
					new String[]{ChatColor.DARK_AQUA+"Infinite Speed II",""+ChatColor.GRAY+ChatColor.ITALIC+"Note: You can change this with /element."}));
			final int run = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable(){
			    @Override
			    public void run(){
			        e.getPlayer().openInventory(Main.chooseElement);
			    }
			},5);
		}
	}
	@EventHandler
	public void playerDataUpdatePerLogin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		File playerFile = playerFile(p);
        FileConfiguration playerdata = playerData(playerFile);
		
        playerdata.set("info.name",p.getName());
		try {
			playerdata.save(playerFile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		final int run = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable(){
		    @Override
		    public void run(){
		    	Player p = e.getPlayer();
				if(playerFile(p).exists()) {
					File playerFile = playerFile(p);
					FileConfiguration playerdata = playerData(playerFile);
					if(playerdata.get("general.element").equals("fire")) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,100000000,1,true));
					}else if(playerdata.get("general.element").equals("ice")) {
						p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,100000000,1,true));
					}else {
						p.sendMessage(ChatColor.RED+"You have not yet chosen an element. Use /element to do so.");
					}
				}
		    }
		},5);
	}
	@EventHandler
	public void warnPlayerOfUnpickedElement(InventoryCloseEvent e) {
		Player p = (Player)e.getPlayer();
		if(e.getInventory().equals(Main.chooseElement)) {
			File playerFile = playerFile(p);
			FileConfiguration playerdata = playerData(playerFile);
			if(playerdata.get("general.element").equals("none")) {
				p.sendMessage(ChatColor.RED+"You have not yet chosen an element! Use /element to do so.");
			}
		}
	}
	@EventHandler
	public void antiSpawnerWatering(BlockFromToEvent e) {
		Block block = e.getBlock();
		if(block.getType()==Material.WATER||block.getType()==Material.STATIONARY_WATER||block.getType()==Material.LAVA||block.getType()==Material.STATIONARY_LAVA) {
			for(int x=-2;x<=2;x++) {
				for(int y=-2;y<=2;y++) {
					for(int z=-2;z<=2;z++) {
						Location loc = new Location(block.getWorld(),block.getX()+x,block.getY()+y,block.getZ()+z);
						Block blocktotest = loc.getBlock();
						if(blocktotest.getType()==Material.MOB_SPAWNER) {
							block.setType(Material.AIR);
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void instantSpongeBreak(PlayerInteractEvent e) {
		if(e.getAction()==Action.LEFT_CLICK_BLOCK&&e.getClickedBlock().getType()==Material.SPONGE) {
			e.getClickedBlock().breakNaturally();
		}
	}
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerLogin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if(playerFile(p).exists()&&p.hasPlayedBefore()) {
			File playerFile = playerFile(p);
			FileConfiguration playerdata = playerData(playerFile);
			if(playerdata.get("general.element").equals("none")) {
				Main.chooseElement.clear();
				Main.chooseElement.setItem(2,Main.createItem(new ItemStack(Material.BLAZE_POWDER),
						""+ChatColor.GOLD+"Choose Fire!",
						new String[]{ChatColor.RED+"Infinite Strength II",""+ChatColor.GRAY+ChatColor.ITALIC+"Note: You can change this with /element."}));
				Main.chooseElement.setItem(6,Main.createItem(new ItemStack(Material.ICE),
						""+ChatColor.AQUA+"Choose Ice!",
						new String[]{ChatColor.DARK_AQUA+"Infinite Speed II",""+ChatColor.GRAY+ChatColor.ITALIC+"Note: You can change this with /element."}));
				final int run = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable(){
				    @Override
				    public void run(){
				        e.getPlayer().openInventory(Main.chooseElement);
				    }
				},5);
			}else {
				if(playerdata.get("general.element").equals("fire")) {
					p.removePotionEffect(PotionEffectType.SPEED);
					p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,100000000,1,true));
				}else if(playerdata.get("general.element").equals("ice")) {
					p.removePotionEffect(PotionEffectType.SPEED);
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,100000000,1,true));
				}
			}
		}
	}
	@EventHandler
	public void shockwavePickaxe(BlockBreakEvent e) {
		ItemStack pick = e.getPlayer().getItemInHand();
		if(loreID(pick.getItemMeta().getLore()).contains(CrimsonItems.SHOCKWAVE3)) {
			int radius=1;
			for(int x=-radius;x<=radius;x++) {
				for(int y=-radius;y<=radius;y++) {
					for(int z=-radius;z<=radius;z++) {
						Location loc = new Location(e.getPlayer().getWorld(),e.getBlock().getLocation().getBlockX()+x,e.getBlock().getLocation().getBlockY()+y,e.getBlock().getLocation().getBlockZ()+z);
						if(!(loc.getBlock().getType()==Material.BEDROCK)) {
							loc.getBlock().breakNaturally(pick); //see if you can make it so that it makes the player themself try to break each block (like each block is broken as if the player broke it) so that they can't just grief faction shit, and you have to do each individual block, not just the center one
						}
					}
				}
			}
		}else if(loreID(pick.getItemMeta().getLore()).contains(CrimsonItems.SHOCKWAVE5)) {
			int radius=2;
			for(int x=-radius;x<=radius;x++) {
				for(int y=-radius;y<=radius;y++) {
					for(int z=-radius;z<=radius;z++) {
						Location loc = new Location(e.getPlayer().getWorld(),e.getBlock().getLocation().getBlockX()+x,e.getBlock().getLocation().getBlockY()+y,e.getBlock().getLocation().getBlockZ()+z);
						if(!(loc.getBlock().getType()==Material.BEDROCK)) {
							loc.getBlock().breakNaturally(pick);
						}
					}
				}
			}
		}else if(loreID(pick.getItemMeta().getLore()).contains(CrimsonItems.SHOCKWAVE9)) {
			int radius=4;
			for(int x=-radius;x<=radius;x++) {
				for(int y=-radius;y<=radius;y++) {
					for(int z=-radius;z<=radius;z++) {
						Location loc = new Location(e.getPlayer().getWorld(),e.getBlock().getLocation().getBlockX()+x,e.getBlock().getLocation().getBlockY()+y,e.getBlock().getLocation().getBlockZ()+z);
						if(!(loc.getBlock().getType()==Material.BEDROCK)) {
							loc.getBlock().breakNaturally(pick);
						}
					}
				}
			}
		}
	}
	@EventHandler
	public void onPlayerUseElementGUI(InventoryClickEvent e) {
		Player p = (Player)e.getWhoClicked();
		if(e.getInventory().equals(Main.chooseElement)){
			if(e.getCurrentItem().getType()==Material.ICE) {
				File playerFile = playerFile(p);
				FileConfiguration playerdata = playerData(playerFile);
				if(playerdata.get("general.element").equals("ice")) {
					p.closeInventory();
					p.sendMessage(ChatColor.RED+"You've already chosen Ice!");
				}else {
					playerdata.set("general.element","ice");
					try {
						playerdata.save(playerFile);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,100000000,1,true));
					p.sendMessage(ChatColor.AQUA+"You chose Ice! You now have Infinite Speed II.");
					p.closeInventory();
				}
			}else if(e.getCurrentItem().getType()==Material.BLAZE_POWDER) {
				File playerFile = playerFile(p);
				FileConfiguration playerdata = playerData(playerFile);
				if(playerdata.get("general.element").equals("fire")) {
					p.closeInventory();
					p.sendMessage(ChatColor.RED+"You've already chosen Fire!");
				}else {
					playerdata.set("general.element","fire");
					try {
						playerdata.save(playerFile);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					p.removePotionEffect(PotionEffectType.SPEED);
					p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,100000000,1,true));
					p.sendMessage(ChatColor.GOLD+"You chose Fire! You now have Infinite Strength II.");
					p.closeInventory();
				}
			}
		}
	}
}
