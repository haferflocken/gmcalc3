package org.gmcalc3.adapters;

import java.util.Map;

import org.gmcalc3.util.Handies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MapListAdapter<E> extends BaseAdapter {
	
	private final Map<String, E> map;
	private String[] strings;
	private Object[] values;
	private LayoutInflater inflater;
	
	public MapListAdapter(Context context, Map<String, E> map) {
		this.map = map;
		buildContent();
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public void notifyDataSetChanged() {
		buildContent();
		super.notifyDataSetChanged();
	}
	
	@Override
	public void notifyDataSetInvalidated() {
		buildContent();
		super.notifyDataSetInvalidated();
	}

	@Override
	public int getCount() {
		return strings.length;
	}

	@Override
	public Object getItem(int position) {
		return values[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v;
        if (convertView == null) {
            v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }
        else {
            v = convertView;
        }
        TextView tV = (TextView)v.findViewById(android.R.id.text1);
        tV.setText(strings[position]);
        return v;
	}
	
	private void buildContent() {
		int length = map.size();
		strings = new String[length];
		values = new Object[length];
		
		int i = 0;
		for (Map.Entry<String, E> entry : map.entrySet()) {
			strings[i] = entry.getKey();
			values[i] = entry.getValue();
			i++;
		}
		
		Handies.sortAndKeepPairs(strings, values);
	}

}
