package org.gmcalc3;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.gmcalc3.adapters.MapListAdapter;
import org.gmcalc3.adapters.TabAdapter;
import org.gmcalc3.world.Component;
import org.gmcalc3.world.ItemBase;
import org.gmcalc3.world.World;
import org.gmcalc3.world.Character;

import com.astuetz.PagerSlidingTabStrip;

public class WorldDetailActivity extends Activity {
	
	public static final String BUNDLE_WORLD = "world";
	
	public static final class CharacterListFragment extends ListFragment {
		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			// Create a character detail activity.
			Character c = (Character)getListAdapter().getItem(position);
			Intent intent = new Intent(getActivity(), CharacterDetailActivity.class);
			intent.putExtra(CharacterDetailActivity.BUNDLE_CHARACTER, c);
			startActivity(intent);
		}
	}
	
	public static final class ComponentListFragment extends ListFragment {
		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			// Create a component detail activity.
			Component c = (Component)l.getItemAtPosition(position);
			Intent intent = new Intent(getActivity(), ComponentDetailActivity.class);
			intent.putExtra(ComponentDetailActivity.BUNDLE_COMPONENT, c);
			startActivity(intent);
		}
	}
	
	public static final class ItemBaseListFragment extends ListFragment {
		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			// Create an item base detail activity.
			ItemBase i = (ItemBase)l.getItemAtPosition(position);
			Intent intent = new Intent(getActivity(), ItemBaseDetailActivity.class);
			intent.putExtra(ItemBaseDetailActivity.BUNDLE_ITEMBASE, i);
			startActivity(intent);
		}
	}
	
	private CharacterListFragment characterFragment;
	private ComponentListFragment prefixFragment;
	private ComponentListFragment materialFragment;
	private ItemBaseListFragment itemBaseFragment;
	private ViewPager tabPager;
	private World world;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_world_detail);
		setupLayout();
		
		// Enable the up button.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Get the world from the bundle.
		if (savedInstanceState != null)
			world = savedInstanceState.getParcelable(BUNDLE_WORLD);
		else
			world = getIntent().getParcelableExtra(BUNDLE_WORLD);
		
		// Abort if there's no world to display.
		if (world == null) {
			Log.e("gmcalc3", "No world for WorldDetailActivity to show.");
			finish();
			return;
		}
		
		// Set the title to the world name.
		setTitle(world.getName());
		
		// Populate the fragments.
		populateCharacterFragment();
		populatePrefixFragment();
		populateMaterialFragment();
		populateItemBaseFragment();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		// Save the world to the bundle.
		outState.putParcelable(BUNDLE_WORLD, world);
	}
	
	@Override
	public Intent getParentActivityIntent() {
		// Make up navigation work properly.
		if (world.isDeviceWorld())
			return new Intent(this, DeviceWorldsActivity.class);
		return new Intent(this, TableActivity.class);
	}
	
	// Set up the layout.
	private void setupLayout() {
		// Create the fragments.
		characterFragment = new CharacterListFragment();
		prefixFragment = new ComponentListFragment();
		materialFragment = new ComponentListFragment();
		itemBaseFragment = new ItemBaseListFragment();
						
		// Set up the tab pager.
		tabPager = (ViewPager)findViewById(R.id.tabPager);
		String[] tabTitles = new String[] {
				getResources().getString(R.string.world_detail_characters),
				getResources().getString(R.string.world_detail_prefixes),
				getResources().getString(R.string.world_detail_materials),
				getResources().getString(R.string.world_detail_itembases),
			};
		Fragment[] tabFragments = new Fragment[] { characterFragment, prefixFragment, materialFragment, itemBaseFragment };
		TabAdapter tabAdapter = new TabAdapter(getFragmentManager(), tabTitles, tabFragments);
		tabPager.setAdapter(tabAdapter);
						
		// Bind the tab strip to the pager.
		PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip)findViewById(R.id.tabStrip);
		tabStrip.setViewPager(tabPager);
	}
	
	// Populate the character fragment using the world.
	private void populateCharacterFragment() {
		characterFragment.setListAdapter(new MapListAdapter<Character>(this, world.getCharacterMap()));
	}
	
	// Populate the prefix fragment using the world.
	private void populatePrefixFragment() {
		prefixFragment.setListAdapter(new MapListAdapter<Component>(this, world.getPrefixMap()));
	}
	
	// Populate the material fragment using the world.
	private void populateMaterialFragment() {
		materialFragment.setListAdapter(new MapListAdapter<Component>(this, world.getMaterialMap()));
	}
	
	// Populate the item base fragment using the world.
	private void populateItemBaseFragment() {
		itemBaseFragment.setListAdapter(new MapListAdapter<ItemBase>(this, world.getItemBaseMap()));
	}
}
