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
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.*;

/**
 * A basic player listener.
 * @author Johannes Donath <johannesd@evil-co.com>
 * @copyright Copyright (C) 2013 Evil-Co <http://www.evil-co.com>
 */
public class PlayerListener implements Listener {

	/**
	 * Stores the parent plugin.
	 */
	protected final MapUtilityPlugin plugin;

	/**
	 * Constructs a new player listener.
	 * @param plugin
	 */
	public PlayerListener (MapUtilityPlugin plugin) {
		this.plugin = plugin;
	}

	/**
	 * Handles bucket events.
	 * @param event
	 */
	public void onBucket (PlayerBucketEvent event) {
		// disallow spectators
		if (this.plugin.getSpectators ().contains (event.getPlayer ())) event.setCancelled (true);
	}

	/**
	 * Handles bucket empty events.
	 * @param event
	 */
	@EventHandler (priority = EventPriority.HIGH)
	public void onBucketEmpty (PlayerBucketEmptyEvent event) {
		this.onBucket (event);
	}

	/**
	 * Handles bucket fill events.
	 * @param event
	 */
	@EventHandler (priority = EventPriority.HIGH)
	public void onBucketFill (PlayerBucketFillEvent event) {
		this.onBucket (event);
	}

	/**
	 * Handles drop item events.
	 * @param event
	 */
	@EventHandler (priority = EventPriority.HIGH)
	public void onDropItem (PlayerDropItemEvent event) {
		// disallow spectators
		if (this.plugin.getSpectators ().contains (event.getPlayer ())) event.setCancelled (true);
	}

	/**
	 * Handles bed events.
	 * @param event
	 */
	@EventHandler (priority = EventPriority.HIGH)
	public void onEnterBed (PlayerBedEnterEvent event) {
		// disallow spectators
		if (this.plugin.getSpectators ().contains (event.getPlayer ())) event.setCancelled (true);
	}

	/**
	 * Handles entity damage by entity events.
	 * @param event
	 * @todo Arrows and other projectiles will still hit spectators but won't cause any damage.
	 */
	@EventHandler (priority = EventPriority.HIGH)
	public void onEntityDamageByEntityEvent (EntityDamageByEntityEvent event) {
		Player player = null;

		// get player
		if (event.getDamager () instanceof Player)
			player = ((Player) event.getDamager ());
		else if (event.getEntity () instanceof Player)
			player = ((Player) event.getEntity ());
		else
			return;

		// disallow spectators to damage anybody or take damage
		if (this.plugin.getSpectators ().contains (player)) event.setCancelled (true);
	}

	/**
	 * Handles entity target events.
	 * @param event
	 */
	@EventHandler (priority = EventPriority.HIGH)
	public void onEntityTargetEvent (EntityTargetEvent event) {
		// only check for players
		if (!(event.getTarget () instanceof Player)) return;

		// get player
		Player player = ((Player) event.getTarget ());

		// disallow mobs to target spectators
		if (this.plugin.getSpectators ().contains (player)) event.setCancelled (true);
	}

	/**
	 * Handles game mode changes.
	 * @param event
	 */
	@EventHandler
	public void onGameModeChange (PlayerGameModeChangeEvent event) {
		// update flight permission
		if (this.plugin.getSpectators ().contains (event.getPlayer ()) && !event.getNewGameMode ().equals (GameMode.CREATIVE)) event.getPlayer ().setAllowFlight (false);
	}

	/**
	 * Handles player interactions.
	 */
	@EventHandler (priority = EventPriority.HIGH)
	public void onInteract (PlayerInteractEvent event) {
		// disallow spectators
		if (event.hasBlock () && this.plugin.getSpectators ().contains (event.getPlayer ())) event.setCancelled (true);
	}

	/**
	 * Handles item pickups.
	 * @param event
	 */
	@EventHandler (priority = EventPriority.HIGH)
	public void onPickupItemEvent (PlayerPickupItemEvent event) {
		// disallow spectators
		if (this.plugin.getSpectators ().contains (event.getPlayer ())) event.setCancelled (true);
	}

	/**
	 * Handles projectile launches.
	 * @param event
	 */
	@EventHandler (priority = EventPriority.HIGH)
	public void onProjectileLaunch (ProjectileLaunchEvent event) {
		// check shooter
		if (!(event.getEntity ().getShooter () instanceof Player)) return;

		// get player
		Player player = ((Player) event.getEntity ().getShooter ());

		// disallow spectators to shoot projectiles
		if (this.plugin.getSpectators ().contains (player)) event.setCancelled (true);
	}

	/**
	 * Handles player quits.
	 * @param event
	 */
	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void onQuit (PlayerQuitEvent event) {
		// remove spectator
		if (this.plugin.getSpectators ().contains (event.getPlayer ())) {
			// disallow flight (to be sure about that)
			if (event.getPlayer ().getGameMode () != GameMode.CREATIVE) event.getPlayer ().setAllowFlight (false);

			// remove player from list
			this.plugin.getSpectators ().remove (event.getPlayer ());
		}
	}

	/**
	 * Handles unleash events.
	 * @param event
	 */
	@EventHandler (priority = EventPriority.HIGH)
	public void onUnleash (PlayerUnleashEntityEvent event) {
		// disallow spectators
		if (this.plugin.getSpectators ().contains (event.getPlayer ())) event.setCancelled (true);
	}
}
