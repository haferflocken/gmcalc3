package org.gmcalc3;

import java.util.Map;

import org.gmcalc3.world.World;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class DeviceWorldsActivity extends NavDrawerActivity {
	
	public static Map<String, World> deviceWorlds;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_worlds);
		labelId = R.string.label_device_worlds;
		createNavDrawer(savedInstanceState);
	}

	@Override
	protected void populateDrawerMap() {
		super.populateDrawerMap();
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide items related to the content view.
		boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
		// TODO Disable items here: menu.findItem(R.id.whatever).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Let the drawer toggle handle it if it can.
		if (drawerToggle.onOptionsItemSelected(item))
			return true;
		
		// Otherwise, see about the other items.
		// TODO
		
		return super.onOptionsItemSelected(item);
	}

}
