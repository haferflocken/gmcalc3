package org.gmcalc3.world;

import java.util.ArrayList;
import java.util.HashMap;

import org.hafermath.expression.ExpressionBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ComponentClass {
	
	private String filePath;
	private World world;
	private String className;
	private String[] variables;
	private String classContent;
	
	private HashMap<ArrayList<String>, Component> instances;
	
	public ComponentClass(String filePath, World world) {
		this.filePath = filePath;
		this.world = world;
	}
	
	public String getClassName() {
		return className;
	}

	public Component getInstance(ArrayList<String> params, ExpressionBuilder expBuilder) {
		Component c = instances.get(params);
		if (c == null)
			c = makeInstance(params);
		return c;
	}
	
	private Component makeInstance(ArrayList<String> params, ExpressionBuilder expBuilder) {
		// Build raw instance JSON.
		String instanceContent = classContent;
		for (int i = 0; i < variables.length; i++) {
			instanceContent = instanceContent.replaceAll("\\$" + variables[i], params.get(i));
		}
		
		// Make a component from the raw JSON.
		try {
			JSONTokener tokener = new JSONTokener(instanceContent);
			JSONObject object = (JSONObject)tokener.nextValue();
			Component c = new Component(filePath, world, object, expBuilder); // TODO
			
			return c;
		}
		catch (JSONException e) {
			return null;
		}
	}
	
}
