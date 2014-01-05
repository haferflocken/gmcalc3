package org.gmcalc3;

import java.util.Map;

import org.gmcalc3.widget.MapListAdapter;
import org.gmcalc3.world.World;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class WorldListFragment extends ListFragment {
	
	public static final String BUNDLE_WORLDS_LOC = "worldsLoc";
	
	private byte worldsLoc;				// == World.DEVICE_WORLD_VAL if device worlds, table worlds otherwise.
	private Map<String, World> worlds;	// The worlds to display.

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Get the worlds from the bundle.
		if (savedInstanceState != null)
			setWorlds(savedInstanceState.getByte(BUNDLE_WORLDS_LOC));
		else if (getArguments() != null)
			setWorlds(getArguments().getByte(BUNDLE_WORLDS_LOC));
		else 
			setWorlds(World.DEVICE_WORLD_VAL);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		// Save the worlds to the bundle.
		outState.putByte(BUNDLE_WORLDS_LOC, worldsLoc);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Create a world detail activity.
		World w = (World)l.getItemAtPosition(position);
		Intent intent = new Intent(getActivity(), WorldDetailActivity.class);
		intent.putExtra(WorldDetailActivity.BUNDLE_WORLD, w);
		startActivity(intent);
	}
	
	// Set the worlds.
	public void setWorlds(byte worldsLoc) {
		this.worldsLoc = worldsLoc;
		if (worldsLoc == World.DEVICE_WORLD_VAL)
			worlds = DeviceWorldsActivity.deviceWorlds;
		else
			worlds = TableActivity.tableWorlds;
		if (worlds != null)
			setListAdapter(new MapListAdapter<World>(getActivity(), worlds));
	}
	
}
