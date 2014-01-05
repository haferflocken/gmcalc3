package org.gmcalc3.world;

import org.gmcalc3.util.Handies;
import org.hafermath.expression.ExpressionBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemBase extends Component {
	
	private static final String[] EMPTY_STRING_ARRAY = {};
	
	public static final String PREFIXREQS_KEY = "prefixRequirements";
	public static final String MATERIALREQS_KEY = "materialRequirements";
	public static final String DEFAULTMATERIALS_KEY = "defaultMaterials";
	
	private String[] prefixReqs;		// The tag requirements for all prefixes.
	private String[][] materialReqs;	// The tag requirements for each material.
	private String[] defaultMaterials;	// The default materials that Item uses if no materials are passed to item.
	
	// Constructors.
	public ItemBase(String filePath, World world, JSONObject values, ExpressionBuilder expBuilder) throws JSONException {
		super(filePath, world, values, expBuilder);
		
		// Get the prefix tag requirements.
		JSONArray rawPrefixReqs = values.getJSONArray(PREFIXREQS_KEY);
		if (rawPrefixReqs.length() > 0) {
			prefixReqs = Handies.jsonArrayToStringArray(rawPrefixReqs);
		}
		else {
			prefixReqs = EMPTY_STRING_ARRAY;
		}
		
		// Get the material tag requirements.
		JSONArray rawMaterialReqs = values.getJSONArray(MATERIALREQS_KEY);
		int numMaterials = rawMaterialReqs.length();
		materialReqs = new String[numMaterials][];
		for (int i = 0; i < numMaterials; i++) {
			materialReqs[i] = Handies.jsonArrayToStringArray(rawMaterialReqs.getJSONArray(i));
		}
		
		// Get the default materials.
		JSONArray rawDefaultMaterials = values.getJSONArray(DEFAULTMATERIALS_KEY);
		defaultMaterials = new String[rawDefaultMaterials.length()];
		for (int i = 0; i < defaultMaterials.length; i++) {
			defaultMaterials[i] = rawDefaultMaterials.getString(i);
		}
	}

	// Accessors.
	public String[] getPrefixReqs() {
		return prefixReqs;
	}
	
	public String[][] getMaterialReqs() {
		return materialReqs;
	}
	
	public String[] getDefaultMaterials() {
		return defaultMaterials;
	}
	
	public static final Parcelable.Creator<ItemBase> CREATOR
    		= new Parcelable.Creator<ItemBase>() {
		public ItemBase createFromParcel(Parcel in) {
		    World world = in.readParcelable(World.class.getClassLoader());
		    String filePath = in.readString();
		    return world.getItemBase(filePath);
		}
		
		public ItemBase[] newArray(int size) {
		    return new ItemBase[size];
		}
	};
	
}
