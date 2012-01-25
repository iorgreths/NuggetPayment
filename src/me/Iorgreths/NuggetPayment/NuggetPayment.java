package me.Iorgreths.NuggetPayment;

import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class NuggetPayment extends JavaPlugin {
	
	public static NuggetPayment plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	private final SignListener signlistener = new SignListener(this);
	private final SignCreateListener signcreate = new SignCreateListener(this);

	@Override
	public void onDisable() {
		PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " version " + pdffile.getVersion() + " is now disable.");

	}

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_INTERACT, this.signlistener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.SIGN_CHANGE, this.signcreate, Event.Priority.Normal, this);
		PluginDescriptionFile pdffile = this.getDescription();
		this.logger.info(pdffile.getName() + " version " + pdffile.getVersion() + " is now enabled.");

	}

}
