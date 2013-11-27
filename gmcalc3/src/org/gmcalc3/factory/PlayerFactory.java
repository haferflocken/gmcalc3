package org.gmcalc3.factory;

import java.util.Map;

import org.gmcalc2.World;
import org.gmcalc2.item.Player;
import org.haferlib.util.DataReader;

public class PlayerFactory extends AbstractFactoryDataReader<Player> {

	public static final String FILE_EXTENSION = ".txt";
	
	private World world; //The world the player is being made in.
	
	// Constructor.
	public PlayerFactory(DataReader dataReader, World world) {
		super(dataReader);
		setWorld(world);
	}
	
	// Set the world.
	public void setWorld(World w) {
		world = w;
	}
	
	// Make the component.
	public Player makeFromValues(String absolutePath, String relativePath, Map<String, Object> values) {
		return new Player(world, absolutePath, values);
	}
	
	// Get the file extension.
	public String getFileExtension() {
		return FILE_EXTENSION;
	}
}
