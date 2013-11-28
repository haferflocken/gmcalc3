package org.gmcalc3;

import java.util.LinkedHashMap;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public abstract class NavDrawerActivity extends Activity {
	
	protected DrawerLayout drawerLayout;					// The drawer layout.
	protected ActionBarDrawerToggle drawerToggle;			// The action bar drawer toggle button.
	protected ListView drawerList;							// The drawer.
	protected LinkedHashMap<String, Intent> drawerItemMap;	// A map of drawer item strings to intents.
	private String[] drawerStrings;							// The strings for items in the drawer, built from drawerItemMap.
	
	protected int labelId;	// The activity label id.
	
	/**
	 * Create the navigation drawer.
	 */
	protected void createNavDrawer(Bundle savedInstanceState) {
		// Initialize the nav drawer and the action bar drawer toggle.
		drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
		drawerList = (ListView)findViewById(R.id.left_drawer);
		drawerToggle = new ActionBarDrawerToggle(
				this,					// Host actitivty.
				drawerLayout,			// DrawerLayout.
				R.drawable.ic_drawer,	// The icon to replace the up caret.
				R.string.drawer_open,	// "open drawer" description.
				R.string.drawer_close	// "close drawer" description.
				) {
			
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(labelId);
				invalidateOptionsMenu();
			}
			
			public void onDrawerOpened(View view) {
				getActionBar().setTitle(R.string.app_name);
				invalidateOptionsMenu();
			}
		};
		drawerLayout.setDrawerListener(drawerToggle);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		// Grab click events from the nav drawer.
		drawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View view, int position, long id) {
				// Navigate to another top-level view. TODO Consider using custom item layout and getting string from item view.
				String key = drawerStrings[position];
				Intent destination = drawerItemMap.get(key);
				if (destination != null) {
					drawerLayout.closeDrawer(drawerList);
					startActivity(destination);
				}
			}
		});
		
		// Create the drawer item map.
		drawerItemMap = new LinkedHashMap<String, Intent>();
		populateDrawerMap();
	}
	
	/**
	 * Add the desired items to the drawer map. Implementations should call updatedDrawerItemMap() when done.
	 * The default implementation will navigate between Device Worlds, Table, and Settings.
	 */
	protected void populateDrawerMap() {
		String key = getResources().getString(R.string.label_device_worlds);
		Intent value = new Intent("org.gmcalc3.DeviceWorldsActivity");
		value.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		drawerItemMap.put(key, value);
		
		key = getResources().getString(R.string.label_table);
		value = new Intent("org.gmcalc3.TableActivity");
		value.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		drawerItemMap.put(key, value);
		
		key = getResources().getString(R.string.label_settings);
		value = new Intent("org.gmcalc3.SettingsActivity");
		value.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		drawerItemMap.put(key, value);
		
		key = getResources().getString(R.string.label_character_detail);
		value = new Intent("org.gmcalc3.CharacterDetailActivity");
		drawerItemMap.put(key, value);
		
		updatedDrawerItemMap();
	}
	
	/**
	 * Called by implementing classes when the drawer item map is updated. Updates the contents of the drawer
	 * to reflect the map.
	 */
	protected void updatedDrawerItemMap() {
		drawerStrings = drawerItemMap.keySet().toArray(new String[drawerItemMap.size()]);
		drawerList.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, drawerStrings));
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state of the drawer toggle.
		drawerToggle.syncState();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerToggle.onConfigurationChanged(newConfig);
	}

}
