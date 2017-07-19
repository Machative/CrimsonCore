package com.crimsonnetwork;
import java.io.File;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
public class Main extends JavaPlugin{
	public static PListener plisten = new PListener();
	public static Main plugin;
	
	public static Inventory chooseElement = Bukkit.createInventory(null, 9, ChatColor.BLUE+"Pick your element!");
	
	@Override
	public void onEnable() {
		plugin=this;
		System.out.println("Crimson Core has been enabled!");
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(plisten, this);
		plugin.getDataFolder().mkdir();
		
		Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers();
		for(int i=0;i<onlinePlayers.length;i++) {
			Player cur = onlinePlayers[i];
			File playerfile = getPlayerFile(cur);
			YamlConfiguration playerdata = PListener.playerData(playerfile);
			if(playerdata.get("general.element").equals("fire")) {
				cur.removePotionEffect(PotionEffectType.SPEED);
				cur.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,100000000,1,true));
			}else if(playerdata.get("general.element").equals("ice")) {
				cur.removePotionEffect(PotionEffectType.SPEED);
				cur.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,100000000,1,true));
			}
		}
	}
	@Override
	public void onDisable() {
		System.out.println("Crimson Core is being disabled.");
	}
	public static ItemStack createItem(ItemStack item, String name,
			String[] lore) {
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(name);
		im.setLore(Arrays.asList(lore));
		item.setItemMeta(im);
		return item;
	}
	public static File getPlayerFile(Player p) {
		File playerFile = new File(plugin.getDataFolder()+File.separator+"CrimsonCore","Crimson_"+p.getUniqueId().toString()+".yml");
		return playerFile;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player p = (Player)sender;
		String sent = label.toLowerCase();

		if(sent.equalsIgnoreCase("element")){
			chooseElement.clear();
			chooseElement.setItem(2,createItem(new ItemStack(Material.BLAZE_POWDER),
					""+ChatColor.GOLD+"Choose Fire!",
					new String[]{ChatColor.RED+"Infinite Strength II",""+ChatColor.GRAY+ChatColor.ITALIC+"Note: You can change this with /element."}));
			chooseElement.setItem(6,createItem(new ItemStack(Material.ICE),
					""+ChatColor.AQUA+"Choose Ice!",
					new String[]{ChatColor.DARK_AQUA+"Infinite Speed II",""+ChatColor.GRAY+ChatColor.ITALIC+"Note: You can change this with /element."}));
			p.openInventory(chooseElement);
		}else if(sent.equalsIgnoreCase("cc")){
			if(p.hasPermission("crimson.cc")) {
				for(int i=0;i<250;i++) {
					Bukkit.getServer().broadcastMessage("");
				}
				Bukkit.getServer().broadcastMessage(ChatColor.GREEN+"Chat has been cleared by "+p.getName()+"!");
			}else {
				p.sendMessage(ChatColor.RED+"You can't do that!");
			}
		}else if(sent.equalsIgnoreCase("shockwave")) {
			if(p.hasPermission("crimson.admin")) {
				if(args.length==2) {
					int diam = Integer.valueOf(args[0]);
					if(diam==3||diam==5||diam==9) {
						Player tp = Bukkit.getServer().getPlayer(args[1]);
						Location ploc = tp.getLocation();
						ItemStack shockwavepick;
						if(diam==3) {
							shockwavepick = createItem(new ItemStack(Material.DIAMOND_PICKAXE),
									ChatColor.GREEN+"Shockwave Pickaxe "+ChatColor.DARK_GREEN+"("+args[0]+"x"+args[0]+"x"+args[0]+")",
									new String[]{ChatColor.GRAY+"Destroys all blocks in a "+args[0]+"x"+args[0]+"x"+args[0]+" cube around you.",ChatColor.BLACK+CrimsonItems.SHOCKWAVE3});
						}else if(diam==5) {
							shockwavepick = createItem(new ItemStack(Material.DIAMOND_PICKAXE),
									ChatColor.GREEN+"Shockwave Pickaxe "+ChatColor.DARK_GREEN+"("+args[0]+"x"+args[0]+"x"+args[0]+")",
									new String[]{ChatColor.GRAY+"Destroys all blocks in a "+args[0]+"x"+args[0]+"x"+args[0]+" cube around you.",ChatColor.BLACK+CrimsonItems.SHOCKWAVE5});
						}else {
							shockwavepick = createItem(new ItemStack(Material.DIAMOND_PICKAXE),
									ChatColor.GREEN+"Shockwave Pickaxe "+ChatColor.DARK_GREEN+"("+args[0]+"x"+args[0]+"x"+args[0]+")",
									new String[]{ChatColor.GRAY+"Destroys all blocks in a "+args[0]+"x"+args[0]+"x"+args[0]+" cube around you.",ChatColor.BLACK+CrimsonItems.SHOCKWAVE9});
						}
						if(tp.getInventory().firstEmpty()==-1) {
							ploc.getWorld().dropItemNaturally(ploc,shockwavepick);
						}else {
							tp.getInventory().addItem(shockwavepick);
						}
					}else {
						System.out.println("Buycraft just tried to create an invalid Shockwave Pickaxe with an attempted diameter of "+args[0]+".");
					}
				}else {
					System.out.println("Buycraft is using an incorrect number of arguments with the Shockwave command!");
					System.out.println("The player involved in this instance is either "+args[0]+" or "+args[1]+", and chances are one of those is null.");
					System.out.println("If the player's name was literally \"null\", this would be pretty damn confusing.");
					System.out.println("Good thing this is only for debugging and that would never actually happen.");
				}
			}else {
				p.sendMessage(ChatColor.WHITE+"Unknown command.");
			}
		}
		
		return true;
	}
}
