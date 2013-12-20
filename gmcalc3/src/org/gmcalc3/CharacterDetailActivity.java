package org.gmcalc3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ExpandableListAdapter;
import android.widget.SimpleExpandableListAdapter;

import org.gmcalc3.world.Character;

import com.astuetz.PagerSlidingTabStrip;

public class CharacterDetailActivity extends Activity {
	
	private ExpandableListFragment statsFragment;
	private ExpandableListFragment equippedFragment;
	private ExpandableListFragment inventoryFragment;
	private ViewPager tabPager;
	private Character character;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_character_detail);
		
		// TODO: Get the character from the bundle.
		
		// Calc the current screen width in dp.
		int width = (int)(getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().density);
		
		// Set up the fragments appropriately.
		if (width < 720)
			setupTabbedLayout();
		else
			setupColumnedLayout();
		
		// Populate the fragments.
		android.util.Log.i("gmcalc3", "populating");
		populateStatsFragment();
		populateEquippedFragment();
		populateInventoryFragment();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		// TODO: Save the character to the bundle.
	}
	
	// Set up the tabbed layout when it's being used.
	private void setupTabbedLayout() {
		// Create the fragments.
		statsFragment = new ExpandableListFragment();
		equippedFragment = new ExpandableListFragment();
		inventoryFragment = new ExpandableListFragment();
						
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
		statsFragment = (ExpandableListFragment)getFragmentManager().findFragmentById(R.id.statsFragment);
		equippedFragment = (ExpandableListFragment)getFragmentManager().findFragmentById(R.id.equippedFragment);
		inventoryFragment = (ExpandableListFragment)getFragmentManager().findFragmentById(R.id.inventoryFragment);
	}
	
	private List<Map<String, ?>> createMaps(String[] strings) {
		List<Map<String, ?>> out = new ArrayList<Map<String, ?>>();
		for (String s : strings) {
			Map<String, String> row = new HashMap<String, String>();
			row.put("text1", s);
			out.add(row);
		}
		return out;
	}
	
	private List<List<Map<String, ?>>> createChildMaps(String[][] strings) {
		List<List<Map<String, ?>>> out = new ArrayList<List<Map<String, ?>>>();
		for (String[] s : strings) {
			out.add(createMaps(s));
		}
		return out;
	}
	
	private ExpandableListAdapter createExpandableListAdapter(List<? extends Map<String, ?>> groupData,
			List<? extends List<? extends Map<String, ?>>> childData) {
		/* SimpleExpandableListAdapter(Context context, 
			List<? extends Map<String, ?>> groupData, int groupLayout, String[] groupFrom, int[] groupTo,
			List<? extends Map<String, ?>> childData, int childLayout, String[] childFrom, int[] childTo)
		*/
		int groupLayout = android.R.layout.simple_expandable_list_item_1;
		String[] groupFrom = new String[] { "text1" };
		int[] groupTo = new int[] { android.R.id.text1 };
		int childLayout = android.R.layout.simple_list_item_1;
		String[] childFrom = new String[] { "text1" };
		int[] childTo = new int[] { android.R.id.text1 };
		return new SimpleExpandableListAdapter(this,
				groupData, groupLayout, groupFrom, groupTo,
				childData, childLayout, childFrom, childTo);
	}
	
	// Populate the stats fragment using the character.
	protected void populateStatsFragment() {
		// Populate the fragment with bullshit.
		statsFragment.setExpandableListAdapter(createExpandableListAdapter(
				createMaps(new String[] { "Mumbo", "Jumbo", "Wumbo", "Stats are cool" }),
				createChildMaps(new String[][] {
						{ "Cumbo", "Fook" }, { "Bumbo" }, { "Sumbo" }, { "I disagree." },
				})));
	}

	// Populate the equipped fragment using the character.
	protected void populateEquippedFragment() {
		// Populate the fragment with bullshit.
		equippedFragment.setExpandableListAdapter(createExpandableListAdapter(
				createMaps(new String[] { "King Dick Alpha Cleaver", "Adamantite Chainsword", "Wizard Robes" }),
				createChildMaps(new String[][] {
						{ "Damn this thing is fine" }, { "WHOOAAAA" }, { "YEAH BWOY MAGICKS" },
				})));
	}
	
	// Populate the inventory fragment using the character.
	protected void populateInventoryFragment() {
		// Populate the fragment with bullshit.
		inventoryFragment.setExpandableListAdapter(createExpandableListAdapter(
				createMaps(new String[] { "Gold x100" }),
				createChildMaps(new String[][] {
						{ "Swag in solid form." },
				})));
	}
}
