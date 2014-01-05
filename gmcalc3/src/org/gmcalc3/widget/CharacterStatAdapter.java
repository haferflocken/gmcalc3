package org.gmcalc3.widget;

import java.util.ArrayList;
import java.util.Map;

import org.gmcalc3.world.Character;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/**
 * Adapts a character's stat map to an expandable list view, with the stats grouped as
 * specified by the character's world.
 * 
 * @author John Werner
 */

public final class CharacterStatAdapter extends BaseExpandableListAdapter {
	
	private Character character;
	private String[] groupStrings;
	private String[][] childStrings;
	private LayoutInflater inflater;
	
	/**
	 * Construct an adapter.
	 * 
	 * @param context
	 *			Any non-null context will do.
	 * @param character
	 *			The character to adapt.
	 */
	public CharacterStatAdapter(Context context, Character character) {
		this.character = character;
		buildContents();
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public void notifyDataSetInvalidated() {
		buildContents();
		super.notifyDataSetInvalidated();
    }
    
    @Override
    public void notifyDataSetChanged() {
        buildContents();
    	super.notifyDataSetChanged();
    }

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childStrings[groupPosition][childPosition];
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View v;
        if (convertView == null) {
            v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        else {
            v = convertView;
        }
        TextView tV = (TextView)v.findViewById(android.R.id.text1);
        tV.setText(childStrings[groupPosition][childPosition]);
        return v;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childStrings[groupPosition].length;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupStrings[groupPosition];
	}

	@Override
	public int getGroupCount() {
		return groupStrings.length;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View v;
        if (convertView == null) {
            v = inflater.inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
        }
        else {
            v = convertView;
        }
        TextView tV = (TextView)v.findViewById(android.R.id.text1);
        tV.setText(groupStrings[groupPosition]);
        return v;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
	
	private void buildContents() {
		Map<String, String[]> statCategories = character.getWorld().getCharacterStatCategories();
		String[] statStrings = character.getStatMap().toDisplayStrings();
		int numStatStrings = statStrings.length;
		
		ArrayList<String> groupNames = new ArrayList<String>();
		ArrayList<String[]> groupContents = new ArrayList<String[]>();
		ArrayList<String> groupContentsBuilder = new ArrayList<String>();
		
		// Look through the stat categories.
		for (Map.Entry<String, String[]> entry : statCategories.entrySet()) {
			// Get stats that match the category and place them in the ArrayLists.
			for (String s : entry.getValue()) {
				for (int i = 0; i < numStatStrings; i++) {
					if (statStrings[i] != null && statStrings[i].startsWith(s)) {
						groupContentsBuilder.add(statStrings[i]);
						statStrings[i] = null;
						break;
					}
				}
			}
			// If any stat strings were acquired, place them in a group.
			int groupContentsBuilderSize = groupContentsBuilder.size();
			if (groupContentsBuilderSize > 0) {
				groupNames.add(entry.getKey());
				groupContents.add(groupContentsBuilder.toArray(new String[groupContentsBuilderSize]));
				groupContentsBuilder.clear();
			}
		}
		
		// Place the leftover stat strings in an "Other" group.
		for (int i = 0; i < numStatStrings; i++) {
			if (statStrings[i] != null) {
				groupContentsBuilder.add(statStrings[i]);
			}
		}
		int groupContentsBuilderSize = groupContentsBuilder.size();
		if (groupContentsBuilderSize > 0) {
			groupNames.add("Other");
			groupContents.add(groupContentsBuilder.toArray(new String[groupContentsBuilderSize]));
			groupContentsBuilder.clear();
		}
		
		// Place the ArrayLists in the instance arrays.
		groupStrings = groupNames.toArray(new String[groupNames.size()]);
		childStrings = groupContents.toArray(new String[groupContents.size()][]);
	}
	
}