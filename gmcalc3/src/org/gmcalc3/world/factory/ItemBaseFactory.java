package org.gmcalc3.world.factory;

import org.gmcalc3.world.ItemBase;
import org.hafermath.expression.ExpressionBuilder;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemBaseFactory extends JSONFactory<ItemBase> {
	
	private ExpressionBuilder expBuilder;
	
	public ItemBaseFactory() {
		expBuilder = new ExpressionBuilder();
	}

	@Override
	protected ItemBase makeInstance(String path, JSONObject jsonObject) throws JSONException {
		return new ItemBase(path, jsonObject, expBuilder);
	}

}
