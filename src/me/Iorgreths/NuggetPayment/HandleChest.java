package me.Iorgreths.NuggetPayment;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class HandleChest {

	private Chest chest;
	private NuggetPayment ng;
	private Player p;
	
	public HandleChest(NuggetPayment inst, Player p){
		super();
		this.ng = inst;
		this.p = p;
	}
	
	// get the location of the chest
	public boolean findChest(Location loc){
		BlockFace[] bf = {
				BlockFace.EAST,
				BlockFace.WEST,
				BlockFace.SOUTH,
				BlockFace.NORTH
							};
		for(BlockFace f : bf){
			for(BlockFace face : BlockFace.values()){
				if(loc.getBlock().getRelative(BlockFace.DOWN).getRelative(f).getRelative(face).getType() == Material.CHEST){
					chest = (Chest) loc.getBlock().getRelative(BlockFace.DOWN).getRelative(f).getRelative(face).getState();
					return true;
				}
			}
		}
		p.sendRawMessage("no valid chest nearby.");
		ng.logger.log(Level.INFO, "Trading chest for merchant sign vanished.");
		return false;
	}
	
	// check if in the chest are still enough nuggets left
	public boolean enoughNuggetsLeft(int price){
		HashMap<Integer,? extends ItemStack> hm = chest.getInventory().all(Material.GOLD_NUGGET);
		int count = 0;
		for(Entry<Integer, ? extends ItemStack> entry : hm.entrySet()){
			count += entry.getValue().getAmount();
		}
		if( (count - price) >=0 ){
			return true;
		}else{
			p.sendRawMessage("NuggetPayment: The chest hasn't enough nuggets left.");
			return false;
		}
	}
	
	// check if their are still items left
	public boolean itemsLeft(Material m){
		if(chest.getInventory().first(m) >= 0){
			return true;
		}else{
			p.sendRawMessage("NuggetPayment: We're sorry but the chest is out of items.");
			return false;
		}
	}
	
	// delete nuggets from the chest
	public void deleteNuggets(int price){
		HashMap<Integer,? extends ItemStack> hm = chest.getInventory().all(Material.GOLD_NUGGET);
		for(Entry<Integer, ? extends ItemStack> entry : hm.entrySet()){
			ItemStack is = entry.getValue();
			if( price < is.getAmount()){
				is.setAmount(is.getAmount() - price);
				return;
			}else{
				price = price - is.getAmount();
				chest.getInventory().remove(is);
			}
			if( price == 0){
				return;
			}
		}
	}
	
	// refill the nuggets to the chest
	public void refillNuggets(int price){
		if(chest.getInventory().first(Material.GOLD_NUGGET) >= 0){
			ItemStack is = chest.getInventory().getItem(chest.getInventory().first(Material.GOLD_NUGGET));
			if( (is.getAmount()+price) < 250){
				is.setAmount(is.getAmount()+price);
				return;
			}else{
				int empty = chest.getInventory().firstEmpty();
				chest.getInventory().setItem(empty, new ItemStack(Material.GOLD_NUGGET, price));
				return;
			}
		}
	}
	
	// set buyed item
	public boolean itemToChest(Material m){
		if(chest.getInventory().firstEmpty() >= 0){
			chest.getInventory().setItem(chest.getInventory().firstEmpty(), new ItemStack(m));
			return true;
		}else{
			p.sendRawMessage("NuggetPayment: Chest is full.");
			return false;
		}
	}
	
	// delete the sold item
	public void deleteItem(Material m){
		int index = chest.getInventory().first(m);
		ItemStack is = chest.getInventory().getItem(index);
		if(is.getAmount() > 1){
			is.setAmount(is.getAmount() - 1);
		}else{
			chest.getInventory().clear(index);
		}
	}
	
	// give the nuggets to the chest
	public boolean giveNuggetsToChest(int price){
		int index = chest.getInventory().firstEmpty();
		if(index >= 0){
			chest.getInventory().setItem(index, new ItemStack(Material.GOLD_NUGGET, price));
			return true;
		}else if(chest.getInventory().first(Material.GOLD_NUGGET) >= 0){
			index = chest.getInventory().first(Material.GOLD_NUGGET);
			ItemStack is = chest.getInventory().getItem(index);
			is.setAmount(is.getAmount() + price);
			return true;
		}else {
			p.sendRawMessage("NuggetPayment: Unable to give nuggets to you.");
			return false;
		}
	}
	
	// regive the sold item
	public void regiveItem(Material m){
		if(chest.getInventory().first(m) >= 0){
			ItemStack is = chest.getInventory().getItem(chest.getInventory().first(m));
			is.setAmount(is.getAmount() + 1);
			return;
		}else if(chest.getInventory().firstEmpty() >= 0){
			int index = chest.getInventory().firstEmpty();
			chest.getInventory().setItem(index, new ItemStack(m));
			return;
		}
	}
	
	// get the first item
	public Material getFirstMaterial(){
		ItemStack[] content = chest.getInventory().getContents();
		for(ItemStack is : content){
			if(is.getType() == Material.GOLD_NUGGET){
				
			}else{
				return is.getType();
			}
		}
		return null;
	}
}
