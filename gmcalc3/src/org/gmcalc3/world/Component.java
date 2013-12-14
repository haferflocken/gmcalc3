// A component of an item.

package org.gmcalc3.world;

import java.util.Set;
import java.util.TreeSet;

import org.hafermath.expression.ExpressionBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Component {
	
	public static final String NAME_KEY = "name";
	public static final String STATMAP_KEY = "stats";
	public static final String RARITY_KEY = "rarity";
	public static final String TAGS_KEY = "tags";

	private String filePath;
	private String name;
	private StatMap statMap;
	private int rarity;
	private Set<String> tags;
	
	// Constructors.
	public Component(String filePath, String name, StatMap statMap, int rarity, TreeSet<String> tags) {
		this.filePath = filePath;
		this.name = name;
		this.statMap = statMap;
		this.rarity = rarity;
		this.tags = tags;
	}
	
	public Component(String filePath) {
		this(filePath, "Untitled Item", new StatMap(), 0, new TreeSet<String>());
	}
	
	public Component(String filePath, JSONObject values, ExpressionBuilder expBuilder) throws JSONException {
		this(filePath);
		
		// Get the name.
		name = values.getString(NAME_KEY);
		
		// Get the stats.
		JSONObject rawStatMap = values.getJSONObject(STATMAP_KEY); 
		statMap = new StatMap(rawStatMap, expBuilder);
		
		// Get the rarity.
		rarity = values.getInt(RARITY_KEY);
		
		// Get the tags.
		JSONArray rawTags = values.getJSONArray(TAGS_KEY);
		for (int i = 0; i < rawTags.length(); i++) {
			tags.add(rawTags.getString(i));
		}
	}
	
	// Accessors.
	public String getFilePath() {
		return filePath;
	}
	
	public String getName() {
		return name;
	}
	
	public StatMap getStatMap() {
		return statMap;
	}
	
	public int getRarity() {
		return rarity;
	}
	
	public Set<String> getTags() {
		return tags;
	}

}
