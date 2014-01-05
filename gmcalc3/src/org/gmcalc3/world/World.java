// A world holds:
//	component factories for materials, prefixes, and itemBases
//	a map of players in the world that have been loaded
//	rules for the players in the world
//	colors to color items based on rarity
//	rules for sorting the stats of players

package org.gmcalc3.world;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Arrays;

import org.gmcalc3.DeviceWorldsActivity;
import org.gmcalc3.TableActivity;
import org.hafermath.expression.ExpressionBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class World implements Parcelable {
	
	public static final byte DEVICE_WORLD_VAL = 0;
	public static final byte TABLE_WORLD_VAL = 1;
	
	// The default rarity colors.
	private static final int[] DEFAULT_COLOR_RARITIES = { Integer.MIN_VALUE };
	private static final int[] DEFAULT_COLOR_VALUES = { android.R.color.white };
	
	// Keys for loading.
	public static final String NAME_KEY = "name";
	public static final String RARITYCOLORS_KEY = "rarityColors";
	public static final String CHARACTERSTATCATEGORIES_KEY = "characterStatCategories";
	public static final String CHARACTERBASESTATS_KEY = "characterBase";
	
	// Instance fields.
	private final byte deviceWorld;										// If 0, the world is local to the device. Otherwise, the world belongs to the table.
	private String fileName;											// The name of the world in the file system.
	private String name;												// The name of the world.
	private int[] colorRarities;										// The rarity colors that are displayed in this world.
	private int[] colorValues;
	private LinkedHashMap<String, String[]> characterStatCategories;	// The categories stats are sorted into in PlayerTabs.
	private StatMap characterBaseStats;									// The player's base stats.
	private Map<String, Component> prefixes;							// The prefixes.
	private Map<String, Component> materials;							// The materials.
	private Map<String, ItemBase> itemBases;							// The itemBases.
	private Map<String, Character> characters;							// The characters.
	
	// Constructor.
	public World(byte deviceWorld, String fileName, JSONObject ruleValues, ExpressionBuilder expBuilder,
			Map<String, Component> prefixes, Map<String, Component> materials, Map<String, ItemBase> itemBases) throws JSONException {
		this.deviceWorld = deviceWorld;
		this.fileName = fileName;
		this.prefixes = prefixes;
		this.materials = materials;
		this.itemBases = itemBases;
		characters = null;
		setRulesToDefault();
		setRules(ruleValues, expBuilder);
	}
	
	// Set the rules to default values.
	public void setRulesToDefault() {
		name = fileName;
		colorRarities = DEFAULT_COLOR_RARITIES;
		colorValues = DEFAULT_COLOR_VALUES;
		characterStatCategories = new LinkedHashMap<String, String[]>();
		characterBaseStats = null;
	}
	
	// Set the rules using loaded data.
	public void setRules(JSONObject rawRules, ExpressionBuilder expBuilder) throws JSONException {
		// Get the name.
		name = rawRules.getString(NAME_KEY);
		
		// Get the rarity colors.
		JSONObject rawRarityColors = rawRules.getJSONObject(RARITYCOLORS_KEY);
		int maxNumColors = rawRarityColors.length();
		int[] parsedColorRarities = new int[maxNumColors];
		int[] parsedColorValues = new int[maxNumColors];
		int numColors = 0;
		
		JSONArray rarityColorNames = rawRarityColors.names();
		for (int i = 0; i < maxNumColors; i++) {
			String rarityColorName = rarityColorNames.getString(i);
			parsedColorRarities[numColors] = Integer.parseInt(rarityColorName);
			
			JSONArray rawColor = rawRarityColors.getJSONArray(rarityColorName);
			int red = rawColor.getInt(0);
			int green = rawColor.getInt(1);
			int blue = rawColor.getInt(2);
			parsedColorValues[numColors] = Color.rgb(red, green, blue);
			numColors++;
		}
			
		// If we parsed any, keep them.
		if (numColors == maxNumColors) {
			colorRarities = parsedColorRarities;
			colorValues = parsedColorValues;
		}
		else {
			colorRarities = new int[numColors];
			colorValues = new int[numColors];
			for (int i = 0; i < numColors; i++) {
				colorRarities[i] = parsedColorRarities[i];
				colorValues[i] = parsedColorValues[i];
			}
		}
		
		// Get the character stat categories.
		JSONObject rawStatCategories = rawRules.getJSONObject(CHARACTERSTATCATEGORIES_KEY);
		JSONArray rawStatCategoryNames = rawStatCategories.names();
		for (int i = 0; i < rawStatCategoryNames.length(); i++) {
			String statCategoryName = rawStatCategoryNames.getString(i);
			JSONArray rawContents = rawStatCategories.getJSONArray(statCategoryName);
			String[] statCategoryContents = new String[rawContents.length()];
			for (int j = 0; j < statCategoryContents.length; j++) {
				statCategoryContents[j] = rawContents.getString(j);
			}
			characterStatCategories.put(statCategoryName, statCategoryContents);
		}
		
		// Get the character base stats.
		JSONObject rawBaseStats = rawRules.getJSONObject(CHARACTERBASESTATS_KEY);
		characterBaseStats = new StatMap(rawBaseStats, expBuilder);
	}
	
	// Get the file name.
	public boolean isDeviceWorld() {
		return deviceWorld == 0;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	// Get the name.
	public String getName() {
		return name;
	}
	
	// Get the character stat categories.
	public Map<String, String[]> getCharacterStatCategories() {
		return characterStatCategories;
	}
	
	// Get the character base stats.
	public StatMap getCharacterBaseStats() {
		return characterBaseStats;
	}
	
	// Get a prefix.
	public Component getPrefix(String prefixName) {
		return prefixes.get(prefixName);
	}
	
	// Get the prefix map.
	public Map<String, Component> getPrefixMap() {
		return prefixes;
	}
	
	// Get a material.
	public Component getMaterial(String matName) {
		return materials.get(matName);
	}
	
	// Get the material map.
	public Map<String, Component> getMaterialMap() {
		return materials;
	}
	
	// Get an itemBase.
	public ItemBase getItemBase(String itemBaseName) {
		return itemBases.get(itemBaseName);
	}
	
	// Get the item base map.
	public Map<String, ItemBase> getItemBaseMap() {
		return itemBases;
	}
	
	// Get a player.
	public Character getCharacter(String playerFile) {
		return characters.get(playerFile);
	}

	// Get the player map.
	public Map<String, Character> getCharacterMap() {
		return characters;
	}
	
	// Get the rarity color of an item.
	public int getRarityColor(Item item) {
		int rarity = item.getRarity();
		for (int i = colorRarities.length - 1; i > -1; i--) {
			if (rarity >= colorRarities[i])
				return colorValues[i];
		}
		return android.R.color.black;
	}
	
	// Helper method to get components matching from a map.
	private Component[] getComponentsMatching(String[] requirement, Map<String, Component> map) {
		ArrayList<Component> out = new ArrayList<Component>();
		for (Map.Entry<String, Component> entry : map.entrySet()) {
			Component c = entry.getValue();
			if (c.hasTags(requirement))
				out.add(c);
		}
		return out.toArray(new Component[out.size()]);
	}
	
	// Get the prefixes matching a tag requirement.
	public Component[] getPrefixesMatching(String[] requirement) {
		return getComponentsMatching(requirement, prefixes);
	}
	
	// Get the materials matching a tag requirement.
	public Component[] getMaterialsMatching(String[] requirement) {
		return getComponentsMatching(requirement, materials);
	}
	
	// Get the number of prefixes.
	public int getNumPrefixes() {
		return prefixes.size();
	}
	
	// Get the number of materials.
	public int getNumMaterials() {
		return materials.size();
	}
	
	// Get the number of item bases.
	public int getNumItemBases() {
		return itemBases.size();
	}
	
	// Get the number of characters.
	public int getNumCharacters() {
		return characters.size();
	}

	// Set the character map.
	public void setCharacterMap(Map<String, Character> c) {
		characters = c;
	}
	
	// Output this world to the log.
	public void logContents() {
		Log.d("gmcalc3-json", "<" + name + ">");
		Log.d("gmcalc3-json", "<Rules>");
		Log.d("gmcalc3-json", "<Rarity Colors>");
		int numRarityColors = colorRarities.length;
		for (int i = 0; i < numRarityColors; i++) {
			Log.d("gmcalc3-json", colorRarities[i] + ": " + Integer.toHexString(colorValues[i]));
		}
		Log.d("gmcalc3-json", "</Rarity Colors>");
		
		Log.d("gmcalc3-json", "<Character Stat Categories>");
		for (Map.Entry<String, String[]> entry : characterStatCategories.entrySet()) {
			Log.d("gmcalc3-json", entry.getKey() + '=' + Arrays.toString(entry.getValue()));
		}
		Log.d("gmcalc3-json", "</Character Stat Categories>");
		
		Log.d("gmcalc3-json", "<Character Base Stats>");
		String[] charBaseStatsStrings = characterBaseStats.toDisplayStrings();
		for (String s : charBaseStatsStrings) {
			Log.d("gmcalc3-json", s);
		}
		Log.d("gmcalc3-json", "</Character Base Stats>");
		Log.d("gmcalc3-json", "</Rules>");
		
		Log.d("gmcalc3-json", "<Prefixes>");
		for (Map.Entry<String, Component> entry : prefixes.entrySet()) {
			Log.d("gmcalc3-json", entry.getKey());
		}
		Log.d("gmcalc3-json", "</Prefixes>");
		
		Log.d("gmcalc3-json", "<Materials>");
		for (Map.Entry<String, Component> entry : materials.entrySet()) {
			Log.d("gmcalc3-json", entry.getKey());
		}
		Log.d("gmcalc3-json", "</Materials>");
		
		Log.d("gmcalc3-json", "<Item Bases>");
		for (Map.Entry<String, ItemBase> entry : itemBases.entrySet()) {
			Log.d("gmcalc3-json", entry.getKey());
		}
		Log.d("gmcalc3-json", "</Item Bases>");
		
		Log.d("gmcalc3-json", "<Characters>");
		for (Map.Entry<String, Character> entry : characters.entrySet()) {
			Log.d("gmcalc3-json", entry.getKey());
		}
		Log.d("gmcalc3-json", "</Characters>");
		
		Log.d("gmcalc3-json", "</" + name + ">");
	}

	@Override
    public int describeContents() {
        return 0;
    }

	@Override
    public void writeToParcel(Parcel out, int flags) {
		out.writeByte(deviceWorld);
		out.writeString(fileName);
    }

    public static final Parcelable.Creator<World> CREATOR
            = new Parcelable.Creator<World>() {
        public World createFromParcel(Parcel in) {
            byte deviceWorld = in.readByte();
            String fileName = in.readString();
            if (deviceWorld == 0) {
            	return DeviceWorldsActivity.deviceWorlds.get(fileName);
            }
            else {
            	if (TableActivity.tableWorlds == null)
            		return null;
            	else
            		return TableActivity.tableWorlds.get(fileName);
            }
        }

        public World[] newArray(int size) {
            return new World[size];
        }
    };
}
