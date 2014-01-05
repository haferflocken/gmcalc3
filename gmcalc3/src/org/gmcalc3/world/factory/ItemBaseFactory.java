package org.gmcalc3.world.factory;

import org.gmcalc3.world.ItemBase;
import org.gmcalc3.world.World;
import org.hafermath.expression.ExpressionBuilder;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemBaseFactory extends JSONFactory<ItemBase> {
	
	private ExpressionBuilder expBuilder;
	private World world;
	
	public ItemBaseFactory() {
		expBuilder = new ExpressionBuilder();
	}
	
	public void setWorld(World w) {
		world = w;
	}

	@Override
	protected ItemBase makeInstance(String path, JSONObject jsonObject) throws JSONException {
		return new ItemBase(path, world, jsonObject, expBuilder);
	}

}
