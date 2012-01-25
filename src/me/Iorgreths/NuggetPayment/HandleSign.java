package me.Iorgreths.NuggetPayment;

import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class HandleSign {

	private NuggetPayment ng;
	private Sign sign;
	private String[] lines;
	private Material target;
	private Player p;
	private int price;
	
	public HandleSign(NuggetPayment ng, PlayerInteractEvent event){
		this.ng = ng;
		this.sign = (Sign) event.getClickedBlock().getState();
		this.lines = sign.getLines();
		this.p = event.getPlayer();
	}
	
	/* check headline of sign
	 * return 1 - (Sign) buy
	 * return 2 - (Sign) sell
	 * return 0 - NOTHING
	 */
	public int checkHeadline(){
		if(lines[0].toUpperCase().equals("[NP_BUY]")){
			return 1;
		}else if(lines[0].toUpperCase().equals("[NP_SELL]")){
			return 2;
		}else{
			return 0;
		}
	}
	
	// gets the price on the sign
	public boolean readPrice(){
		String[] sub = lines[1].split(":");
		try{
			price = Integer.parseInt(sub[1]);
			return true;
		}catch(Exception e){
			p.sendRawMessage("No valid number at line price at merchant sign.");
			return false;
		}
	}
	
	// get the material on the sign
	public boolean readMaterial(){
		try{
			if(lines[2].toUpperCase().equals("REDSTONE_TORCH_")){
				target = Material.REDSTONE_TORCH_ON;
				return true;
			}else{
				target = Material.valueOf(lines[2].toUpperCase());
				return true;
			}
		}catch(Exception e){
			p.sendRawMessage("No valid material at merchant sign.");
			ng.logger.log(Level.INFO, "NuggetPayment: Invalid Material");
			return false;
		}
	}
	
	// getter methods
	public Material getMaterialFromSign(){
		return target;
	}
	
	public int getPriceFromSign(){
		return price;
	}
	
	public Location getSignLocation(){
		return sign.getBlock().getLocation();
	}
	
	public Player getPlayer(){
		return p;
	}
}
