package org.gmcalc3;

import java.util.ArrayList;
import java.util.Map;

import org.gmcalc3.world.World;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class WorldExplorerFragment extends ListFragment {
	
	private static final byte VIEWCAT_WORLDLIST = 0;
	private static final byte VIEWCAT_WORLD = 1;
	private static final byte VIEWCAT_PREFIXES = 2;
	private static final byte VIEWCAT_MATERIALS = 3;
	private static final byte VIEWCAT_ITEMBASES = 4;
	private static final byte VIEWCAT_CHARACTERS = 5;
	
	private Map<String, World> worlds; // The worlds to display.
	private byte currentViewCategory; 
	private World currentWorld;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// TODO Get the worlds from the bundle.
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		// TODO: Save the worlds to the bundle.
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_list_headered, container, false);
	}
	
	// Set the worlds.
	public void setWorlds(Map<String, World> w) {
		worlds = w;
		if (worlds == null) {
			setListAdapter(null);
		}
		else {
			viewMap(worlds);
			currentViewCategory = VIEWCAT_WORLDLIST;
		}
	}
	
	// Make an ArrayMap with a single entry.
	private ArrayMap<String, Object> makeSingleEntryMap(String key, Object val) {
		ArrayMap<String, Object> map = new ArrayMap<String, Object>();
		map.put(key, val);
		return map;
	}
	
	// A convenience method for setting the list adapter with a particular SimpleAdapter.
	private void setListAdapter(ArrayList<ArrayMap<String, Object>> data, String[] from) {
		setListAdapter(new SimpleAdapter(
				getActivity(), data, android.R.layout.simple_list_item_1,
				from, new int[] { android.R.id.text1 }
			));
	}
	
	// Make an adapter for a map and display it.
	private void viewMap(Map<String, ?> map) {
		ArrayList<ArrayMap<String, Object>> adapterData = new ArrayList<ArrayMap<String, Object>>();
		for (Map.Entry<String, ?> entry : map.entrySet()) {
			adapterData.add(makeSingleEntryMap("text1", entry.getValue().toString()));
		}
		setListAdapter(adapterData, new String[] { "text1" });
	}
	
	// View the world with the given name.
	private void viewWorld(String worldName) {
		// Find the world.
		for (Map.Entry<String, World> entry : worlds.entrySet()) {
			if (entry.getValue().getName().equals(worldName)) {
				// Once we have found the world, display its contents.
				viewWorld(entry.getValue());
				break;
			}
		}
	}
	
	// View the given world object.
	private void viewWorld(World world) {
		ArrayList<ArrayMap<String, Object>> adapterData = new ArrayList<ArrayMap<String, Object>>();
		adapterData.add(makeSingleEntryMap("text1", "Prefixes"));
		adapterData.add(makeSingleEntryMap("text1", "Materials"));
		adapterData.add(makeSingleEntryMap("text1", "Item Bases"));
		adapterData.add(makeSingleEntryMap("text1", "Characters"));
		adapterData.add(makeSingleEntryMap("text1", "rules.json"));
		setListAdapter(adapterData, new String[] { "text1" });
		currentViewCategory = VIEWCAT_WORLD;
		currentWorld = world;
		refreshHeaderText();
	}
	
	private void refreshHeaderText() {
		TextView header = (TextView)getView().findViewById(R.id.listHeader);
		if (currentViewCategory == VIEWCAT_WORLDLIST) {
			header.setText("All Worlds");
		}
		else if (currentViewCategory == VIEWCAT_WORLD) {
			header.setText(currentWorld.getName() + "/");
		}
		else if (currentViewCategory == VIEWCAT_PREFIXES) {
			header.setText(currentWorld.getName() + "/Prefixes/");
		}
		else if (currentViewCategory == VIEWCAT_MATERIALS) {
			header.setText(currentWorld.getName() + "/Materials/");
		}
		else if (currentViewCategory == VIEWCAT_ITEMBASES) {
			header.setText(currentWorld.getName() + "/Item Bases/");
		}
		else if (currentViewCategory == VIEWCAT_CHARACTERS) {
			header.setText(currentWorld.getName() + "/Characters/");
		}
	}
}
