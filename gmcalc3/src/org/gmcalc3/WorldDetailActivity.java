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

import org.gmcalc3.world.World;

import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;

public class WorldDetailActivity extends Activity {
	
	private Fragment rulesFragment;
	private ExpandableListFragment characterFragment;
	private ExpandableListFragment prefixFragment;
	private ExpandableListFragment materialFragment;
	private ExpandableListFragment itemBaseFragment;
	private ViewPager tabPager;
	private World world;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_world_detail);
		setupLayout();
		
		// TODO: Get the world from the bundle.
		
		// Populate the fragments.
		populateRulesFragment();
		populateCharacterFragment();
		populatePrefixFragment();
		populateMaterialFragment();
		populateItemBaseFragment();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		// TODO: Save the world to the bundle.
	}
	
	// Set up the layout.
	private void setupLayout() {
		// Create the fragments.
		rulesFragment = new ExpandableListFragment();
		characterFragment = new ExpandableListFragment();
		prefixFragment = new ExpandableListFragment();
		materialFragment = new ExpandableListFragment();
		itemBaseFragment = new ExpandableListFragment();
						
		// Set up the tab pager.
		tabPager = (ViewPager)findViewById(R.id.tabPager);
		String[] tabTitles = new String[] {
				getResources().getString(R.string.world_detail_rules),
				getResources().getString(R.string.world_detail_characters),
				getResources().getString(R.string.world_detail_prefixes),
				getResources().getString(R.string.world_detail_materials),
				getResources().getString(R.string.world_detail_itembases),
			};
		Fragment[] tabFragments = new Fragment[] { rulesFragment, characterFragment, prefixFragment, materialFragment, itemBaseFragment };
		TabAdapter tabAdapter = new TabAdapter(getFragmentManager(), tabTitles, tabFragments);
		tabPager.setAdapter(tabAdapter);
						
		// Bind the tab strip to the pager.
		PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip)findViewById(R.id.tabStrip);
		tabStrip.setViewPager(tabPager);
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
	
	// Populate the rules fragment using the world.
	private void populateRulesFragment() {
	}

	// Populate the character fragment using the world.
	private void populateCharacterFragment() {
		characterFragment.setExpandableListAdapter(createExpandableListAdapter(
				createMaps(new String[] { "King Dick Alpha Cleaver", "Adamantite Chainsword", "Wizard Robes" }),
				createChildMaps(new String[][] {
						{ "Damn this thing is fine" }, { "WHOOAAAA" }, { "YEAH BWOY MAGICKS" },
				})));
	}
	
	// Populate the prefix fragment using the world.
	private void populatePrefixFragment() {
		prefixFragment.setExpandableListAdapter(createExpandableListAdapter(
				createMaps(new String[] { "Gold x100" }),
				createChildMaps(new String[][] {
						{ "Swag in solid form." },
				})));
	}
	
	// Populate the material fragment using the world.
	private void populateMaterialFragment() {
		prefixFragment.setExpandableListAdapter(createExpandableListAdapter(
				createMaps(new String[] { "Gold x100" }),
				createChildMaps(new String[][] {
						{ "Swag in solid form." },
				})));
	}
	
	// Populate the item base fragment using the world.
	private void populateItemBaseFragment() {
		itemBaseFragment.setExpandableListAdapter(createExpandableListAdapter(
				createMaps(new String[] { "Gold x100" }),
				createChildMaps(new String[][] {
					{ "Swag in solid form." },
				})));
	}
}
