package me.Iorgreths.NuggetPayment;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class HandlePlayer {

	public NuggetPayment np;
	private Player p;
	
	public HandlePlayer(NuggetPayment np, Player p){
		this.p = p;
		this.np = np;
	}
	
	// check if the player has enough nuggets
		public boolean owesEnoughNuggets(int price){
			HashMap<Integer,? extends ItemStack> hm = p.getInventory().all(Material.GOLD_NUGGET);
			int count = 0;
			for(Entry<Integer, ? extends ItemStack> entry : hm.entrySet()){
				count += entry.getValue().getAmount();
			}
			if( (count - price) >=0 ){
				return true;
			}else{
				p.sendRawMessage("NuggetPayment: The player doesn't owes enough nuggets.");
				return false;
			}
		}
		
	// check if the player owes the required item
		public boolean owesItem(Material m){
			if(p.getInventory().first(m) >= 0){
				return true;
			}else{
				p.sendRawMessage("NuggetPayment: You don't owe the required item.");
				return false;
			}
		}
		
	// delete nuggets from player
		public void deleteNuggets(int price){
			HashMap<Integer,? extends ItemStack> hm = p.getInventory().all(Material.GOLD_NUGGET);
			for(Entry<Integer, ? extends ItemStack> entry : hm.entrySet()){
				ItemStack is = entry.getValue();
				if( price < is.getAmount()){
					is.setAmount(is.getAmount() - price);
					return;
				}else{
					price = price - is.getAmount();
					p.getInventory().remove(is);
				}
				if( price == 0){
					return;
				}
			}
		}
		
	// refill nuggets to the player
		public void refillNuggets(int price){
			if(p.getInventory().first(Material.GOLD_NUGGET) >= 0){
				ItemStack is = p.getInventory().getItem(p.getInventory().first(Material.GOLD_NUGGET));
				if( (is.getAmount()+price) < 250){
					is.setAmount(is.getAmount()+price);
					return;
				}else{
					int empty = p.getInventory().firstEmpty();
					p.getInventory().setItem(empty, new ItemStack(Material.GOLD_NUGGET, price));
					return;
				}
			}
		}
		
	// give the player the bought item
		public boolean itemToPlayer(Material m){
			if(p.getInventory().firstEmpty() >= 0){
				PlayerInventory pi = (PlayerInventory) p.getInventory();
				pi.setItem(p.getInventory().firstEmpty(), new ItemStack(m));
				return true;
			}else{
				p.sendRawMessage("NuggetPayment: You are full.");
				return false;
			}
		}
		
	// delete the item he selled
		public void deleteItem(Material m){
			int index = p.getInventory().first(m);
			ItemStack is = p.getInventory().getItem(index);
			if(is.getAmount() > 1){
				is.setAmount(is.getAmount() - 1);
			}else{
				p.getInventory().clear(index);
			}
		}
		
	// get the nuggets
		public boolean giveNuggetsToPlayer(int price){
			int index = p.getInventory().firstEmpty();
			if(index >= 0){
				p.getInventory().setItem(index, new ItemStack(Material.GOLD_NUGGET, price));
				return true;
			}else if(p.getInventory().first(Material.GOLD_NUGGET) >= 0){
				index = p.getInventory().first(Material.GOLD_NUGGET);
				ItemStack is = p.getInventory().getItem(index);
				is.setAmount(is.getAmount() + price);
				return true;
			}else {
				p.sendRawMessage("NuggetPayment: Unable to give nuggets to you.");
				return false;
			}
		}
		
	// regive item
		public void regiveItem(Material m){
			if(p.getInventory().first(m) >= 0){
				ItemStack is = p.getInventory().getItem(p.getInventory().first(m));
				is.setAmount(is.getAmount() + 1);
				return;
			}else if(p.getInventory().firstEmpty() >= 0){
				int index = p.getInventory().firstEmpty();
				p.getInventory().setItem(index, new ItemStack(m));
				return;
			}
		}
		
	// force inv update
		@SuppressWarnings("deprecation")
		public void updateInventory(){
			p.updateInventory();
		}
		
}
