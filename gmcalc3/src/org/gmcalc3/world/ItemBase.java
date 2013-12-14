package org.gmcalc3.world;

import org.hafermath.expression.ExpressionBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemBase extends Component {
	
	public static final String PREFIXREQS_KEY = "prefixRequirements";
	public static final String MATERIALREQS_KEY = "materialRequirements";
	public static final String DEFAULTMATERIALS_KEY = "defaultMaterials";
	
	private TagRequirement prefixReqs;		// The tag requirements for all prefixes.
	private TagRequirement[] materialReqs;	// The tag requirements for each material.
	private String[] defaultMaterials;		// The default materials that Item uses if no materials are passed to item.
	
	// Constructors.
	public ItemBase(String filePath, JSONObject values, ExpressionBuilder expBuilder) throws JSONException {
		super(filePath, values, expBuilder);
		
		// Get the prefix tag requirements.
		JSONArray rawPrefixReqs = values.getJSONArray(PREFIXREQS_KEY);
		if (rawPrefixReqs.length() > 0) {
			prefixReqs = new TagRequirement(rawPrefixReqs);
		}
		else {
			prefixReqs = new TagRequirement();
		}
		
		// Get the material tag requirements.
		JSONArray rawMaterialReqs = values.getJSONArray(MATERIALREQS_KEY);
		materialReqs = new TagRequirement[rawMaterialReqs.length()];
		for (int i = 0; i < materialReqs.length; i++) {
			materialReqs[i] = new TagRequirement(rawMaterialReqs.getJSONArray(i));
		}
		
		// Get the default materials.
		JSONArray rawDefaultMaterials = values.getJSONArray(DEFAULTMATERIALS_KEY);
		defaultMaterials = new String[rawDefaultMaterials.length()];
		for (int i = 0; i < defaultMaterials.length; i++) {
			defaultMaterials[i] = rawDefaultMaterials.getString(i);
		}
	}

	// Accessors.
	public TagRequirement getPrefixReqs() {
		return prefixReqs;
	}
	
	public TagRequirement[] getMaterialReqs() {
		return materialReqs;
	}
	
	public String[] getDefaultMaterials() {
		return defaultMaterials;
	}
}
