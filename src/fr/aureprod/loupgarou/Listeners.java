package fr.aureprod.loupgarou;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listeners implements Listener 
{
	private boolean finOK = false;
	private Main main;
	
	public Listeners(Main mainbis)
	{
		this.main = mainbis;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event)
	{
		main.nb_joueurs++;
	}
	
	@EventHandler
	public void onUnJoin(PlayerQuitEvent event)
	{
		main.nb_joueurs--;
	}
	
	@EventHandler
	public void onUnJoin(PlayerMoveEvent event)
	{
		if(main.partie_start && !finOK)
		{
			if (event.getFrom().getX() != event.getTo().getX()) 
			{
				if (event.getFrom().getZ() != event.getTo().getZ()) 
				{
					Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "PARTIE TERMINEE !!!");
					finOK = true;
					
					main.finPartie();
				}
			}
		}
	}
}

