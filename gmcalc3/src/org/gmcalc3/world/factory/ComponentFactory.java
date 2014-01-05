package org.gmcalc3.world.factory;

import org.gmcalc3.world.Component;
import org.gmcalc3.world.World;
import org.hafermath.expression.ExpressionBuilder;
import org.json.JSONException;
import org.json.JSONObject;

public class ComponentFactory extends JSONFactory<Component> {
	
	private ExpressionBuilder expBuilder;
	private World world;
	
	public ComponentFactory() {
		expBuilder = new ExpressionBuilder();
	}
	
	public void setWorld(World w) {
		world = w;
	}

	@Override
	protected Component makeInstance(String path, JSONObject jsonObject) throws JSONException {
		return new Component(path, world, jsonObject, expBuilder);
	}

}
