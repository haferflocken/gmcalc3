package org.gmcalc3;

import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import org.gmcalc3.widget.CharacterStatAdapter;
import org.gmcalc3.widget.ItemBagAdapter;
import org.gmcalc3.world.Character;

import com.astuetz.PagerSlidingTabStrip;

public class CharacterDetailActivity extends Activity {
	
	public static final String BUNDLE_CHARACTER = "character";
	
	// Detects touch on item views.
	public static final class ItemLongClickListener implements OnLongClickListener {

		@Override
		public boolean onLongClick(View v) {
			ClipData data = ClipData.newPlainText("", "");
			DragShadowBuilder shadowBuilder = new DragShadowBuilder(v);
			v.startDrag(data, shadowBuilder, v, 0);
			return false;
		}
	}
	
	// Allows an ExpandableListView to receive item views.
	public static final class ItemDragListener implements OnDragListener {
		
		@Override
		public boolean onDrag(View v, DragEvent event) {
			switch(event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				break;
			case DragEvent.ACTION_DROP:
				View droppedView = (View)event.getLocalState();
				ViewGroup fromGroup = (ViewGroup)droppedView.getParent();
				fromGroup.removeView(droppedView);
				ExpandableListView toGroup = (ExpandableListView)v;
				toGroup.addView(droppedView);
				droppedView.setVisibility(View.VISIBLE);
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				break;
			}
			return true;
		}
	}
	
	private ExpandableListFragment statsFragment;
	private ExpandableListFragment equippedFragment;
	private ExpandableListFragment inventoryFragment;
	private ViewPager tabPager;
	private Character character;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_character_detail);
		
		// Enable the up button.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Get the character from the bundle.
		if (savedInstanceState != null)
			character = savedInstanceState.getParcelable(BUNDLE_CHARACTER);
		else
			character = getIntent().getParcelableExtra(BUNDLE_CHARACTER);
		
		// Abort if there's no character to display.
		if (character == null) {
			Log.e("gmcalc3", "No character for CharacterDetailActivity to show.");
			finish();
			return;
		}
		
		// Set the activity title to the character's name.
		character.recalculateStats();
		setTitle(character.getName());
		
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
	protected void onStart() {
		super.onStart();
		
		// Set up drag and drop.
		//setupDragAndDrop((ViewGroup)equippedFragment.getView());
		//setupDragAndDrop((ViewGroup)inventoryFragment.getView());
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		// Save the character to the bundle.
		outState.putParcelable(BUNDLE_CHARACTER, character);
	}
	
	@Override
	public Intent getParentActivityIntent() {
		// Make up navigation work properly.
		Intent out = new Intent(this, WorldDetailActivity.class);
		out.putExtra(WorldDetailActivity.BUNDLE_WORLD, character.getWorld());
		return out;
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
	
	// Populate the stats fragment using the character.
	protected void populateStatsFragment() {
		statsFragment.setExpandableListAdapter(new CharacterStatAdapter(this, character));
	}

	// Populate the equipped fragment using the character.
	protected void populateEquippedFragment() {
		equippedFragment.setExpandableListAdapter(new ItemBagAdapter(this, character.getEquipped()));
	}
	
	// Populate the inventory fragment using the character.
	protected void populateInventoryFragment() {
		inventoryFragment.setExpandableListAdapter(new ItemBagAdapter(this, character.getInventory()));
	}
	
	// Set up the drag and drop between the equipped and inventory columns.
	private void setupDragAndDrop(ViewGroup group) {
		group.setOnDragListener(new ItemDragListener());
		for (int i = 0; i < group.getChildCount(); i++) {
			View v = group.getChildAt(i);
			v.setLongClickable(true);
			v.setOnLongClickListener(new ItemLongClickListener());
		}
	}
}
