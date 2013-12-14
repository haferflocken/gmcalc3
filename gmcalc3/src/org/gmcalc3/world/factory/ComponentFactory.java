package org.gmcalc3.world.factory;

import org.gmcalc3.world.Component;
import org.hafermath.expression.ExpressionBuilder;
import org.json.JSONException;
import org.json.JSONObject;

public class ComponentFactory extends JSONFactory<Component> {
	
	private ExpressionBuilder expBuilder;
	
	public ComponentFactory() {
		expBuilder = new ExpressionBuilder();
	}

	@Override
	protected Component makeInstance(String path, JSONObject jsonObject) throws JSONException {
		return new Component(path, jsonObject, expBuilder);
	}

}
