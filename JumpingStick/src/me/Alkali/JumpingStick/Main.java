package me.Alkali.JumpingStick;


import org.bukkit.command.Command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{

	public List<String> list = new ArrayList<String>();
	
	@Override
	public void onEnable() {
		// startup
		// reload
		// plugin reload
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {
		// close
		// reloads
		// plugin reloads
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(label.equalsIgnoreCase("jumpstick")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(player.getInventory().firstEmpty() == -1) {
					Location loc = player.getLocation();
					World world = player.getWorld();
					world.dropItemNaturally(loc, getItem());
					player.sendMessage("Enjoy!");
					return true;
				}
				player.getInventory().addItem(getItem());
				player.sendMessage("Enjoy!");
				return true;
			}
		}
		return false;
	}
	
	
	public ItemStack getItem() {
		ItemStack item = new ItemStack(Material.STICK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Jump Stick");
		List<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add(ChatColor.YELLOW + "" + ChatColor.BOLD + "RIGHT CLICK");
		lore.add("Jump!");
		lore.add("");
		lore.add(ChatColor.YELLOW + "" + ChatColor.BOLD + "LEFT CLICK");
		lore.add("Hit your enemies into the sky!");
		meta.setLore(lore);
		meta.setUnbreakable(true);
		meta.addEnchant(Enchantment.DURABILITY, 1, true);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		item.setItemMeta(meta);
		return item;
	}
	
	
	@EventHandler()
	public void onRightClick(PlayerInteractEvent event) {
		Player player = (Player) event.getPlayer();
		if(player.getInventory().getItemInMainHand().getType().equals(Material.STICK))
			if(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Jump Stick")) 
				if(player.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
					Location loc = event.getPlayer().getLocation();
						if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
							if(player.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.AIR) {
								player.setVelocity(player.getLocation().getDirection().multiply(2).setY(2));
								loc.setY(loc.getY());
								loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 5);
								loc.getWorld().playSound(loc, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 5, 1);
								if(!list.contains(player.getName())) {
									list.add(player.getName());
								}
							}
							
						}
						
					
					
					
					
				}
				
	}
	
//	@EventHandler()
//	public void onLeftCLick(PlayerHitEvent event) {
//		
//	}
	
	
	@EventHandler()
	public void onLand(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if(event.getCause() == DamageCause.FALL) {
				if(list.contains(player.getName())) {
					event.setCancelled(true);
					list.remove(player.getName());
				}
			}
		}
			
	}
	
	@EventHandler()
	public void onHit(EntityDamageByEntityEvent event) {
		if(event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			if(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains("Jump Stick"))
				if(player.getInventory().getItemInMainHand().getItemMeta().hasLore()){
							event.getEntity().setVelocity(event.getEntity().getLocation().getDirection().multiply(2).setY(2));
						}
			}
		}
			
	

}
