// A component of an item.

package org.gmcalc3.world;

import java.util.Set;
import java.util.TreeSet;

import org.hafermath.expression.ExpressionBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Component implements Parcelable {
	
	public static final String NAME_KEY = "name";
	public static final String STATMAP_KEY = "stats";
	public static final String RARITY_KEY = "rarity";
	public static final String TAGS_KEY = "tags";

	private String filePath;
	private World world;
	private String name;
	private StatMap statMap;
	private int rarity;
	private Set<String> tags;
	
	// Constructors.
	public Component(String filePath, World world, String name, StatMap statMap, int rarity, TreeSet<String> tags) {
		this.filePath = filePath;
		this.world = world;
		this.name = name;
		this.statMap = statMap;
		this.rarity = rarity;
		this.tags = tags;
	}
	
	public Component(String filePath, World world) {
		this(filePath, world, "Untitled Item", new StatMap(), 0, new TreeSet<String>());
	}
	
	public Component(String filePath, World world, JSONObject values, ExpressionBuilder expBuilder) throws JSONException {
		this(filePath, world);
		
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
	
	public World getWorld() {
		return world;
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
	
	// Check if this meets some tag requirements.
	public boolean hasTags(String[] check) {
		int numChecks = check.length;
		for (int i = 0; i < numChecks; i++) {
			if (!tags.contains(check[i]))
				return false;
		}
		return true;
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

    public static final Parcelable.Creator<Component> CREATOR
            = new Parcelable.Creator<Component>() {
        public Component createFromParcel(Parcel in) {
            World world = in.readParcelable(World.class.getClassLoader());
            String filePath = in.readString();
            Component out = world.getPrefix(filePath);
            if (out == null)
            	out = world.getMaterial(filePath);
            return out;
        }

        public Component[] newArray(int size) {
            return new Component[size];
        }
    };

}
