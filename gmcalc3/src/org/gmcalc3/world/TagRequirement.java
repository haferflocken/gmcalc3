// Checks to see if a component has certain tags.

package org.gmcalc3.world;

import java.util.Arrays;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;

public class TagRequirement {
	
	private String[] requiredTags;
	
	// Constructors.
	public TagRequirement(JSONArray rawTags) throws JSONException {
		requiredTags = new String[rawTags.length()];
		for (int i = 0; i < requiredTags.length; i++) {
			requiredTags[i] = rawTags.getString(i);
		}
	}
	
	public TagRequirement(String[] requiredTags) {
		this.requiredTags = requiredTags;
	}
	
	public TagRequirement() {
		this(new String[0]);
	}
	
	// See if a component has the right tags.
	public boolean passes(Component component) {
		Set<String> tags = component.getTags();
		for (int i = 0; i < requiredTags.length; i++) {
			if (!tags.contains(requiredTags[i]))
				return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return Arrays.toString(requiredTags);
	}
}
