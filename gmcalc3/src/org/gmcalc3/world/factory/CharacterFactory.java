package org.gmcalc3.world.factory;

import org.gmcalc3.world.Character;
import org.gmcalc3.world.World;
import org.json.JSONException;
import org.json.JSONObject;

public class CharacterFactory extends JSONFactory<Character> {
	
	private World world;
	
	public void setWorld(World w) {
		world = w;
	}

	@Override
	protected Character makeInstance(String path, JSONObject jsonObject) throws JSONException {
		return new Character(path, world, jsonObject);
	}

}
