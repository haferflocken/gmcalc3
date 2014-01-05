//A player is basically a name, a StatMap, some items that go into the stat map, and some items that don't.

package org.gmcalc3.world;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Character implements Parcelable {

	// Keys for loading.
	public static final String DEFAULT_NAME = "Unnamed";
	public static final String NAME_KEY = "name";
	public static final String EQUIPPED_KEY = "equipped";
	public static final String INVENTORY_KEY = "inventory";
	public static final String QUANTITY_KEY = "Quantity";
	public static final String PREFIXES_KEY = "Prefixes";
	public static final String MATERIALS_KEY = "Materials";
	public static final String ITEMBASE_KEY = "ItemBase";

	// Instance fields.
	private String filePath;
	private World world;
	private String name;
	private StatMap statMap;
	private ListBag<Item> equipped, inventory;

	// Constructors.
	public Character(String filePath, World world) {
		this.filePath = filePath;
		this.world = world;
		name = DEFAULT_NAME;
		statMap = new StatMap();
		equipped = new ListBag<Item>();
		inventory = new ListBag<Item>();
	}

	public Character(String filePath, World world, JSONObject values) throws JSONException {
		this(filePath, world);
		
		// Get the name.
		name = values.getString(NAME_KEY);

		// Get the equipped items.
		JSONArray rawItems = values.getJSONArray(EQUIPPED_KEY);
		for (int i = 0; i < rawItems.length(); i++) {
			JSONObject data = rawItems.getJSONObject(i);
			makeItemFromData(data, equipped);
		}

		// Get the inventory items.
		rawItems = values.getJSONArray(INVENTORY_KEY);
		for (int i = 0; i < rawItems.length(); i++) {
			JSONObject data = rawItems.getJSONObject(i);
			makeItemFromData(data, inventory);
		}

		// Recalculate the stats.
		recalculateStats();
	}

	// Accessors.
	public String getFilePath() {
		return filePath;
	}
	
	public World getWorld() {
		return world;
	}

	public String getName() {
		return name;
	}

	public StatMap getStatMap() {
		return statMap;
	}

	public ListBag<Item> getEquipped() {
		return equipped;
	}

	public ListBag<Item> getInventory() {
		return inventory;
	}

	// Recalculate the stats.
	public void recalculateStats() {
		statMap.clear();
		
		// If a player base is defined in the world, merge it into the stats.
		StatMap baseStats = world.getCharacterBaseStats();
		if (baseStats != null)
			statMap.mergeMap(baseStats);

		// Merge the equipped item stats into the stats.
		for (int q, i = 0; i < equipped.size(); i++) {
			Item item = equipped.get(i);
			int amount = equipped.getCount(i);
			for (q = 0; q < amount; q++) {
				statMap.mergeMap(item.getStatMap());
			}
		}

		// Evaluate the expressions in the stats.
		statMap.evaluateExpressions();
	}

	// Turn an array of objects into an item and add it to the given bag.
	public void makeItemFromData(JSONObject data, ListBag<Item> bag) throws JSONException {
		// Get the quantity.
		int quantity = data.getInt(QUANTITY_KEY);
		if (quantity < 1)
			return;
		
		// Get the item base.
		String itemBasePath = data.getString(ITEMBASE_KEY);
		ItemBase itemBase = world.getItemBase(itemBasePath);
		// If we fail to find the item base, give up.
		if (itemBase == null) {
			Log.d("gmcalc3-json", "Item creation error: failed to find item base " + itemBasePath);
			return;
		}
		
		// Get the prefixes.
		JSONArray rawPrefixes = data.getJSONArray(PREFIXES_KEY);
		Component[] prefixes = new Component[rawPrefixes.length()];
		for (int i = 0; i < prefixes.length; i++) {
			String prefixPath = rawPrefixes.getString(i);
			prefixes[i] = world.getPrefix(prefixPath);
			// If we fail to find a prefix, give up.
			if (prefixes[i] == null) {
				Log.d("gmcalc3-json", "Item creation error: failed to find prefix " + prefixPath);
				return;
			}
		}
		
		// Get the materials.
		JSONArray rawMaterials = data.getJSONArray(MATERIALS_KEY);
		Component[] materials = new Component[rawMaterials.length()];
		for (int i = 0; i < materials.length; i++) {
			String materialPath = rawMaterials.getString(i);
			materials[i] = world.getMaterial(materialPath);
			// If we fail to find a material, give up.
			if (materials[i] == null) {
				Log.d("gmcalc3-json", "Item creation error: failed to find material " + materialPath);
				return;
			}
		}

		// Add the item to the bag.
		Item item = new Item(world, prefixes, materials, itemBase);
		bag.add(item, quantity);
	}

	@Override
    public int describeContents() {
        return 0;
    }

	@Override
    public void writeToParcel(Parcel out, int flags) {
		out.writeParcelable(world, flags);
		out.writeString(filePath);
    }

    public static final Parcelable.Creator<Character> CREATOR
            = new Parcelable.Creator<Character>() {
        public Character createFromParcel(Parcel in) {
            World world = in.readParcelable(World.class.getClassLoader());
            String filePath = in.readString();
            return world.getCharacter(filePath);
        }

        public Character[] newArray(int size) {
            return new Character[size];
        }
    };
}
