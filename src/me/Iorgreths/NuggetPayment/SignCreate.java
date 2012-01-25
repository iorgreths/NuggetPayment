package me.Iorgreths.NuggetPayment;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

public class SignCreate {

	public NuggetPayment ng;
	private Player p;
	private String[] lines;
	SignChangeEvent event;
	
	public SignCreate(NuggetPayment ng, Player p) {
		super();
		this.ng = ng;
		this.p = p;
	}
	
	// set the sign that gets created
	public void setSign(SignChangeEvent event){
		this.event = event;
		lines = event.getLines();
	}
	
	//check headline
	public int checkHeadline(){
		if(lines[0].toUpperCase().equals("[NP_BUY]")){
			return 1;
		}else if(lines[0].toUpperCase().equals("[NP_SELL]")){
			return 2;
		}else{
			return 0;
		}
	}
	
	//check second line
	public boolean correctSecondLine(){
		if(lines[1].toUpperCase().contains("PRICE:")){
			String[] sub = lines[1].split(":");
			try{
				int a = Integer.parseInt(sub[1]);
				if(a > 0){
					return true;
				}else{
					p.sendRawMessage("Invalid second line. Try something like: price:<amount>");
					return false;
				}
			}catch(Exception e){
				p.sendRawMessage("Invalid second line. Try something like: price:<amount>");
				return false;
			}
		}else{
			p.sendRawMessage("Invalid second line. Try something like: price:<amount>");
			return false;
		}
	}
	
	// check third line
	public boolean correctMaterialOnLineThree(){
		if(lines[2].toUpperCase().equals("REDSTONE_TORCH_")){
			return true;
		}else{
			try{
				Material.valueOf(lines[2].toUpperCase());
				return true;
			}catch(Exception e){
				return false;
			}
		}
	}
	
	// set third line - material
	public void setMaterialOnLineThree(Material m){
		event.setLine(2, m.name());
	}
	
	// set fourth line - player
	public void setPlayerOnLineFour(){
		event.setLine(3, "Owner:"+p.getName());
	}
	
	// getter methods
	public Location getLocationOfSign(){
		return event.getBlock().getLocation();
	}
}
