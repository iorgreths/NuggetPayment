package me.Iorgreths.NuggetPayment;

import org.bukkit.Material;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

public class SignCreateListener extends BlockListener {

	private NuggetPayment ng;
	private SignCreate sc;
	private HandleChest hc;
	
	public SignCreateListener (NuggetPayment ng){
		this.ng = ng;
	}
	
	public void onSignChange(SignChangeEvent event){
		hc = new HandleChest(ng, event.getPlayer());
		sc = new SignCreate(ng, event.getPlayer());
		sc.setSign(event);
		switch(sc.checkHeadline()){
		case 1:
			signBuy();
			break;
		case 2:
			signSell();
			break;
		default:
			break;
		}
	}
	
	private void signBuy(){
		if(sc.correctSecondLine()){
			if(sc.correctMaterialOnLineThree()){
				sc.setPlayerOnLineFour();
			}
		}
	}
	
	private void signSell(){
		if(sc.correctSecondLine()){
			if(hc.findChest(sc.getLocationOfSign())){
				Material mat = hc.getFirstMaterial();
				if(mat != null){
					sc.setMaterialOnLineThree(mat);
					sc.setPlayerOnLineFour();
				}
			}
		}
	}
	
}
