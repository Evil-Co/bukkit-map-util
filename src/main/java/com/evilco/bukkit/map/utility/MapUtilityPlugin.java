/*
 * This file is part of the Bukkit Map Utility.
 *
 * The Bukkit Map Utility is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * The Bukkit Map Utility is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License
 * along with the Bukkit Map Utility. If not, see <http://www.gnu.org/licenses/>.
 */
package com.evilco.bukkit.map.utility;

import com.evilco.bukkit.map.utility.event.PlayerListener;
import com.evilco.bukkit.map.utility.event.WorldListener;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the map utility plugin.
 * @author Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2013 Evil-Co <http://www.evil-co.com>
 */
public class MapUtilityPlugin extends JavaPlugin {

	/**
	 * Stores a list of spectators
	 */
	protected List<Player> spectators = null;

	/**
	 * @return A list of spectators.
	 */
	public List<Player> getSpectators () {
		return this.spectators;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void onEnable () {
		super.onEnable ();

		// setup spectator list
		this.spectators = new ArrayList<Player> ();

		// setup listeners
		this.getServer ().getPluginManager ().registerEvents ((new WorldListener (this)), this);
		this.getServer ().getPluginManager ().registerEvents ((new PlayerListener (this)), this);
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void onDisable () {
		super.onDisable ();

		// disable flight for spectators
		for (Player player : this.spectators) {
			player.setAllowFlight (false);
		}

		// delete list
		this.spectators = null;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public boolean onCommand (CommandSender sender, Command command, String label, String[] args) {
		// select senders
		if (!(sender instanceof BlockCommandSender) && !(sender instanceof Player)) {
			sender.sendMessage ("This command is restricted to players and command blocks.");
			return false;
		}

		// /spectator
		if (command.getName ().equalsIgnoreCase ("spectator")) {
			// check permissions
			if (!sender.isOp () && !(sender instanceof BlockCommandSender)) {
				sender.sendMessage ("Insufficient permissions.");
				return false;
			}

			// check parameters
			if (args.length < 2 || args.length > 2) {
				sender.sendMessage ("Usage: /spectator <add|remove> <player>");
				return false;
			}

			// get player
			Player player = this.getServer ().getPlayerExact (args[1]);

			// verify
			if (!args[0].equalsIgnoreCase ("add") && !args[0].equalsIgnoreCase ("remove")) {
				sender.sendMessage ("Usage: /spectator <add|remove> <player>");
				return false;
			}

			if (player == null) {
				sender.sendMessage ("Cannot find player \"" + args[0] + "\"");
				return false;
			}

			if (args[0].equalsIgnoreCase ("add")) {
				// verify player
				if (this.spectators.contains (player)) {
					sender.sendMessage (player.getName () + " is already a spectator.");
					return false;
				}

				// store in spectator list
				this.spectators.add (player);

				// allow to fly
				player.setAllowFlight (true);

				// notify user
				sender.sendMessage (player.getName () + " is now a spectator.");
			} else if (args[0].equalsIgnoreCase ("remove")) {
				if (!this.spectators.contains (player)) {
					sender.sendMessage (player.getName () + " is not a spectator.");
					return false;
				}

				// remove from spectator list
				this.spectators.remove (player);

				// disallow to fly
				if (player.getGameMode () != GameMode.CREATIVE) player.setAllowFlight (false);

				// notify user
				sender.sendMessage (player.getName () + " is no longer a spectator.");
			}

			// notify user
			return true;
		}

		// /pvp [on/off]
		if (command.getName ().equalsIgnoreCase ("pvp")) {
			// check permissions
			if (!sender.isOp () && !(sender instanceof BlockCommandSender)) {
				sender.sendMessage ("Insufficient permissions.");
				return false;
			}

			// check parameters
			if (args.length > 1) {
				sender.sendMessage ("Usage: /pvp [on|off]");
				return false;
			}

			// get world
			World world = (sender instanceof BlockCommandSender ? ((BlockCommandSender) sender).getBlock ().getWorld () : ((Player) sender).getWorld ());

			// change
			world.setPVP ((args.length > 0 ? args[0].equalsIgnoreCase ("on") : !world.getPVP ()));

			// notify user
			sender.sendMessage ("PVP has been " + (world.getPVP () ? "enabled" : "disabled") + ".");
			return true;
		}

		// call parent
		return super.onCommand (sender, command, label, args);
	}
}
