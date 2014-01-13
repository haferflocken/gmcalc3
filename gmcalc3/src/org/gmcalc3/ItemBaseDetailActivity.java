package org.gmcalc3;

import org.gmcalc3.util.Handies;
import org.gmcalc3.world.ItemBase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ItemBaseDetailActivity extends Activity {
	
	public static final String BUNDLE_ITEMBASE = "itemBase";

	private ItemBase itemBase;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itembase_detail);
		
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
		
		// Display the item base.
		displayItemBase();
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
	
	private void displayItemBase() {
		// Show the item base's name in the action bar.
		setTitle(itemBase.getName());
		
		// Display the item base's prefix requirements.
		TextView prefixReqsDisplay = (TextView)findViewById(R.id.prefixReqsContent);
		Handies.displayInTextView(prefixReqsDisplay, itemBase.getPrefixReqs());
		
		// Display the item base's material requirements.
		TextView materialReqsDisplay = (TextView)findViewById(R.id.materialReqsContent);
		Handies.displayInTextView(materialReqsDisplay, itemBase.getMaterialReqs());
		
		// Display the item base's default materials.
		TextView defaultMaterialsDisplay = (TextView)findViewById(R.id.defaultMaterialsContent);
		Handies.displayInTextView(defaultMaterialsDisplay, itemBase.getDefaultMaterials());
		
		// Display the item base's stats.
		TextView statDisplay = (TextView)findViewById(R.id.statsContent);
		statDisplay.setText(itemBase.getStatMap().toDisplayString());
				
		// Display the item base's tags.
		TextView tagDisplay = (TextView)findViewById(R.id.tagsContent);
		Handies.displayInTextView(tagDisplay, itemBase.getTags());
				
		// Display the item base's other info.
		TextView otherDisplay = (TextView)findViewById(R.id.otherContent);
		otherDisplay.setText("Rarity: " + itemBase.getRarity());
	}
}
