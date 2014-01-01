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
package com.evilco.bukkit.map.utility.event;

import com.evilco.bukkit.map.utility.MapUtilityPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * A basic world listener.
 * @author Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2013 Evil-Co <http://www.evil-co.com>
 */
public class WorldListener implements Listener {

	/**
	 * Stores the parent plugin.
	 */
	protected final MapUtilityPlugin plugin;

	/**
	 * Constructs a new world listener.
	 * @param plugin
	 */
	public WorldListener (MapUtilityPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Handles block breaks.
	 * @param event
	 */
	@EventHandler (priority = EventPriority.HIGH)
	public void onBlockBreak (BlockBreakEvent event) {
		// disallow spectators to interact with the world
		if (this.plugin.getSpectators ().contains (event.getPlayer ())) event.setCancelled (true);
	}

	/**
	 * Handles block placements.
	 * @param event
	 */
	@EventHandler (priority = EventPriority.HIGH)
	public void onBlockPlace (BlockPlaceEvent event) {
		// disallow spectators to interact with the world
		if (this.plugin.getSpectators ().contains (event.getPlayer ())) event.setCancelled (true);
	}
}
