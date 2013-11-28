package org.gmcalc3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.gmcalc3.world.Character;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;

public class CharacterDetailActivity extends Activity {
	
	private ListFragment statsFragment;
	private ListFragment equippedFragment;
	private ListFragment inventoryFragment;
	private ViewPager tabPager;
	private Character character;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_character_detail);
		
		// Calc the current screen width in dp.
		int width = (int)(getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().density);
		
		// Set up the fragments appropriately.
		if (width < 720)
			setupTabbedLayout();
		else
			setupColumnedLayout();
		
		// TODO: Get the character from the bundle.
		
		// Populate the fragments.
		populateStatsFragment();
		populateEquippedFragment();
		populateInventoryFragment();
	}
	
	// Set up the tabbed layout when it's being used.
	private void setupTabbedLayout() {
		// Create the stats fragment.
		statsFragment = new ListFragment();
		Bundle sArgs = new Bundle();
		statsFragment.setArguments(sArgs);
						
		// Create the equipped fragment.
		equippedFragment = new ListFragment();
		Bundle eArgs = new Bundle();
		equippedFragment.setArguments(eArgs);
						
		// Create the inventory fragment.
		inventoryFragment = new ListFragment();
		Bundle iArgs = new Bundle();
		inventoryFragment.setArguments(iArgs);
						
		// Set up the tab pager.
		tabPager = (ViewPager)findViewById(R.id.tabPager);
		String[] tabTitles = new String[] {
				getResources().getString(R.string.character_detail_stats),
				getResources().getString(R.string.character_detail_equipped),
				getResources().getString(R.string.character_detail_inventory),
			};
		Fragment[] tabFragments = new Fragment[] { statsFragment, equippedFragment, inventoryFragment };
		TabAdapter tabAdapter = new TabAdapter(getFragmentManager(), tabTitles, tabFragments);
		tabPager.setAdapter(tabAdapter);
						
		// Bind the tab strip to the pager.
		PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip)findViewById(R.id.tabStrip);
		tabStrip.setViewPager(tabPager);
	}
	
	// Set up the column layout when it's being used.
	private void setupColumnedLayout() {
		// Get rid of references to things we don't like.
		tabPager = null;
		
		// Get the fragments as they were defined in XML.
		statsFragment = (ListFragment)getFragmentManager().findFragmentById(R.id.statsFragment);
		equippedFragment = (ListFragment)getFragmentManager().findFragmentById(R.id.equippedFragment);
		inventoryFragment = (ListFragment)getFragmentManager().findFragmentById(R.id.inventoryFragment);
	}
	
	private List<Map<String, ?>> createMap(String[] titles) {
		List<Map<String, ?>> out = new ArrayList<Map<String, ?>>();
		for (String title : titles) {
			Map<String, String> row = new HashMap<String, String>();
			row.put("text1", title);
			out.add(row);
		}
		return out;
	}
	
	private ListAdapter createListAdapter(List<? extends Map<String, ?>> map) {
		int resource = android.R.layout.simple_list_item_1;
		String[] from = new String[] { "text1" };
		int[] to = new int[] { android.R.id.text1 };
		return new SimpleAdapter(this, map, resource, from, to);
	}
	
	// Populate the stats fragment using the character.
	protected void populateStatsFragment() {
		// Populate the fragment with bullshit.
		statsFragment.setListAdapter(createListAdapter(createMap( new String[] {
				"Mumbo", "Jumbo", "Wumbo", "Stats are cool"
		})));
	}

	// Populate the equipped fragment using the character.
	protected void populateEquippedFragment() {
		// Populate the fragment with bullshit.
		equippedFragment.setListAdapter(createListAdapter(createMap( new String[] {
				"King Dick Alpha Cleaver", "Adamantite Chainsword", "Wizard Robes"
		})));
	}
	
	// Populate the inventory fragment using the character.
	protected void populateInventoryFragment() {
		// Populate the fragment with bullshit.
		inventoryFragment.setListAdapter(createListAdapter(createMap( new String[] {
				"Gold x100", "Chainmail Armor", "Bucket of Fish", "Miniature Jesus Bobblehead",
				"Kanye West", "C++ for Java Programmers", "Google Chrome", "Miley Cyrus", "Nexus 5",
				"Antonio Esteban Banderez"
		})));
	}
}
