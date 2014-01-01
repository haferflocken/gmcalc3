package org.gmcalc3.widget;

import org.gmcalc3.world.Item;
import org.gmcalc3.world.ListBag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public final class ItemBagAdapter extends BaseExpandableListAdapter {
	
	private ListBag<Item> bag;
	private String[][] childContents;
	private LayoutInflater inflater;
	
	public ItemBagAdapter(Context context, ListBag<Item> bag) {
		this.bag = bag;
		buildChildContents();
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public void notifyDataSetInvalidated() {
		buildChildContents();
		super.notifyDataSetInvalidated();
    }
    
    @Override
    public void notifyDataSetChanged() {
        buildChildContents();
    	super.notifyDataSetChanged();
    }

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return bag.get(groupPosition);
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
        tV.setText(childContents[groupPosition][childPosition]);
        return v;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return bag.get(groupPosition).getStatMap().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return bag.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return bag.size();
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
        int itemCount = bag.getCount(groupPosition);
        if (itemCount > 1) {
        	tV.setText(bag.get(groupPosition).getName() + " x" + itemCount);
        }
        else {
        	tV.setText(bag.get(groupPosition).getName());
        }
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
	
	private void buildChildContents() {
		childContents = new String[bag.size()][];
		int length = childContents.length;
		for (int i = 0; i < length; i++) {
			childContents[i] = bag.get(i).getStatMap().toDisplayStrings();
		}
	}
	
}