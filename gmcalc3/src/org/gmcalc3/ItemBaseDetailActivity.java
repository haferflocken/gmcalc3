package org.gmcalc3;

import org.gmcalc3.world.ItemBase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class ItemBaseDetailActivity extends Activity {
	
	public static final String BUNDLE_ITEMBASE = "itemBase";

	private ItemBase itemBase;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_component_detail);
		
		// Enable the up button.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Get the item base from the bundle.
		if (savedInstanceState != null)
			itemBase = savedInstanceState.getParcelable(BUNDLE_ITEMBASE);
		else
			itemBase = getIntent().getParcelableExtra(BUNDLE_ITEMBASE);
		
		// Abort if there's no item base to display.
		if (itemBase == null) {
			Log.e("gmcalc3", "No item base for ItemBaseDetailActivity to show.");
			finish();
			return;
		}
		
		// Set the title to the item base name.
		setTitle(itemBase.getName());
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		// Save the item base to the bundle.
		outState.putParcelable(BUNDLE_ITEMBASE, itemBase);
	}
	
	@Override
	public Intent getParentActivityIntent() {
		// Make up navigation work properly.
		Intent out = new Intent(this, WorldDetailActivity.class);
		out.putExtra(WorldDetailActivity.BUNDLE_WORLD, itemBase.getWorld());
		return out;
	}
}
