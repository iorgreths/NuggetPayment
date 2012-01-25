package me.Iorgreths.NuggetPayment;


import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;

public class SignListener extends PlayerListener {

	private NuggetPayment ng;
	private HandleSign hs;
	private HandleChest hc;
	private HandlePlayer hp;
	
	public SignListener (NuggetPayment ng){
		this.ng = ng;
	}
	
	
	public void onPlayerInteract(PlayerInteractEvent event){
		if(event.getClickedBlock().getType() == Material.WALL_SIGN){
			hs = new HandleSign(ng, event);
			hc = new HandleChest(ng, event.getPlayer());
			hp = new HandlePlayer(ng, event.getPlayer());
			switch(hs.checkHeadline()){
			case 1:
				signBuy();
				break;
			case 2:
				signSell();
				break;
			default:
				break;
			}
			hp.updateInventory();
		}
	}
	
	private void signBuy(){
		if(handle()){
			if(hc.enoughNuggetsLeft(hs.getPriceFromSign())){
				if(hp.owesItem(hs.getMaterialFromSign())){
					hc.deleteNuggets(hs.getPriceFromSign());
					if(hc.itemToChest(hs.getMaterialFromSign())){
						hp.deleteItem(hs.getMaterialFromSign());
						if(hp.giveNuggetsToPlayer(hs.getPriceFromSign())){
							hs.getPlayer().sendRawMessage("Trade Successful");
						}else{
							hp.regiveItem(hs.getMaterialFromSign());
						}
					}else{
						hc.refillNuggets(hs.getPriceFromSign());
					}
				}
			}
		}
	}
	
	private void signSell(){
		if(handle()){
			if(hp.owesEnoughNuggets(hs.getPriceFromSign())){
				if(hc.itemsLeft(hs.getMaterialFromSign())){
					hp.deleteNuggets(hs.getPriceFromSign());
					if(hp.itemToPlayer(hs.getMaterialFromSign())){
						hc.deleteItem(hs.getMaterialFromSign());
						if(hc.giveNuggetsToChest(hs.getPriceFromSign())){
							hs.getPlayer().sendRawMessage("Trade Successful");
						}else{
							hc.regiveItem(hs.getMaterialFromSign());
						}
					}else{
						hp.refillNuggets(hs.getPriceFromSign());
					}
				}
			}
		}
	}
	
	private boolean handle(){
		if(hs.readPrice()){
			if(hs.readMaterial()){
				if(hc.findChest(hs.getSignLocation())){
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
}
