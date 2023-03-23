package fr.aureprod.loupgarou;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Main extends JavaPlugin 
{
	public Integer nb_joueurs = 0;
	private Integer nb_joueurs_max = 0;
	private BossBar bossBar;
	private Integer nb_joueurs_min = 0;
	public Main main = this;
	private BukkitTask task;
	public Boolean partie_start = false;
	private Boolean partie_start_bis = false;
	
	public void finPartie()
	{
		Bukkit.getServer().getScheduler().runTaskLater(this, new Runnable() {
			
			@Override
			public void run() 
			{
				for (Player p : Bukkit.getOnlinePlayers())
				{
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + p.getName());
				}
				
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "rl");
			}
		}, (long) 60);
	}
	
	@Override
	public void onEnable() 
	{
		saveDefaultConfig();
		
		System.out.println("Le plugin Firework est est demarre !!!");
		getCommand("interface").setExecutor(new Commande());
		getServer().getPluginManager().registerEvents(new Listeners(this), this);
		
		nb_joueurs_max = this.getConfig().getInt("nb_joueurs_max");
		nb_joueurs_min = this.getConfig().getInt("nb_joueurs_min");
		
		bossBar = Bukkit.createBossBar("§0§l[§4§l§k!!!!§r§0§l] §7Statut de la partie §9" + this.nb_joueurs +  "§8/§9" + this.nb_joueurs_max + " §0§l[§4§l§k!!!!§r§0§l]", BarColor.RED, BarStyle.SOLID, new BarFlag[0]);
		
		task = Bukkit.getServer().getScheduler().runTaskTimer(this, new Runnable() 
		{
			Integer timerOK = 30;
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() 
			{
				if (!partie_start_bis) 
				{
					Double res = (double) (((double) main.nb_joueurs)/((double) main.nb_joueurs_max));
					
					bossBar.setTitle("§0§l[§4§l§k!!!!§r§0§l] §7Statut de la partie §9" + main.nb_joueurs +  "§8/§9" + main.nb_joueurs_max + " §0§l[§4§l§k!!!!§r§0§l]");
					bossBar.setProgress(res);
					
					for (Player p : Bukkit.getOnlinePlayers())
					{
						bossBar.addPlayer(p);
					}
					
					if(nb_joueurs >= nb_joueurs_min)
					{
						String name = null;
						
						for (Player p : Bukkit.getOnlinePlayers())
						{
							p.setLevel(timerOK);
							p.setExp(((float)(30 - ((float) timerOK)) / 30));
							name = p.getName();
							
							if (timerOK == 10) 
							{
								p.sendTitle("10 secondes restantes", "");
							} 
							else if (timerOK > 0 && timerOK < 6) 
							{
								p.sendTitle("" + timerOK, "");
							}
							else if (timerOK == 0) 
							{
								p.sendTitle("Bonne Game", "");
								bossBar.removePlayer(p);
								p.setExp(0);
								p.setLevel(0);
							} 
						}
						
						if (timerOK == 6) 
						{
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lg joinAll");
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lg start " + name);
						}
						else if (timerOK == 0) 
						{
							partie_start_bis = true;
						}

						timerOK--;
					}
					else
					{
						timerOK = 30;
					}
				}
				else
				{
					if (timerOK == -3) 
					{
						partie_start = true;
						
						Bukkit.getScheduler().cancelTask(task.getTaskId());
					} 
					
					timerOK--;
				}		
			}
		}, 5, (long) 20); // Always multiply by twenty because that's the amount of ticks in Minecraft
	}

	@Override
	public void onDisable() 
	{
		System.out.println("Le plugin Firework est arreter !!!");
		
		this.bossBar.removeAll();
	}
}
