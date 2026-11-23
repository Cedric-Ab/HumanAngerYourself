package ch.zhaw.it.pm.humanangeryourself.api;

/**
 * Name and color of a player as entered before the game starts.
 *
 * @param name  name entered by the player
 * @param color color assigned to the player
 */
public record PlayerSetup(String name, PlayerColor color) { }
