package org.gmcalc3;

import java.util.Map;

import org.gmcalc3.world.World;

import com.astuetz.PagerSlidingTabStrip;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class TableActivity extends NavDrawerActivity {
	
	public static Map<String, World> tableWorlds;
	
	private ListFragment partyFragment;
	private WorldListFragment worldList;
	private ListFragment connectionsFragment;
	private ViewPager tabPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_table);
		labelId = R.string.label_table;
		createNavDrawer(savedInstanceState);
		
		// Create the party view.
		partyFragment = new ListFragment();
		Bundle pFArgs = new Bundle();
		partyFragment.setArguments(pFArgs);
		
		// Create the world list.
		worldList = new WorldListFragment();
		Bundle wLArgs = new Bundle();
		wLArgs.putByte(WorldListFragment.BUNDLE_WORLDS_LOC, World.TABLE_WORLD_VAL);
		worldList.setArguments(wLArgs);
		
		// Create the connections view.
		connectionsFragment = new ListFragment();
		Bundle cFArgs = new Bundle();
		connectionsFragment.setArguments(cFArgs);
		
		// Set up the tab pager.
		tabPager = (ViewPager)findViewById(R.id.tabPager);
		String[] tabTitles = new String[] { "Party", "Worlds", "Connections" };
		Fragment[] tabFragments = new Fragment[] { partyFragment, worldList, connectionsFragment };
		TabAdapter tabAdapter = new TabAdapter(getFragmentManager(), tabTitles, tabFragments);
		tabPager.setAdapter(tabAdapter);
		
		// Bind the tab strip to the pager.
		PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip)findViewById(R.id.tabStrip);
		tabStrip.setViewPager(tabPager);
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
