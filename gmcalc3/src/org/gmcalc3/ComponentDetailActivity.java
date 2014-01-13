package org.gmcalc3;

import org.gmcalc3.util.Handies;
import org.gmcalc3.world.Component;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ComponentDetailActivity extends Activity {
	
	public static final String BUNDLE_COMPONENT = "component";

	private Component component;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_component_detail);
		
		// Enable the up button.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Get the component from the bundle.
		if (savedInstanceState != null)
			component = savedInstanceState.getParcelable(BUNDLE_COMPONENT);
		else
			component = getIntent().getParcelableExtra(BUNDLE_COMPONENT);
		
		// Abort if there's no component to display.
		if (component == null) {
			Log.e("gmcalc3", "No component for ComponentDetailActivity to show.");
			finish();
			return;
		}
		
		// Display the component.
		displayComponent();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		// Save the component to the bundle.
		outState.putParcelable(BUNDLE_COMPONENT, component);
	}
	
	@Override
	public Intent getParentActivityIntent() {
		// Make up navigation work properly.
		Intent out = new Intent(this, WorldDetailActivity.class);
		out.putExtra(WorldDetailActivity.BUNDLE_WORLD, component.getWorld());
		return out;
	}
	
	private void displayComponent() {
		// Show the component's name in the action bar.
		setTitle(component.getName());
		
		// Display the component's stats.
		TextView statDisplay = (TextView)findViewById(R.id.statsContent);
		statDisplay.setText(component.getStatMap().toDisplayString());
		
		// Display the component's tags.
		TextView tagDisplay = (TextView)findViewById(R.id.tagsContent);
		Handies.displayInTextView(tagDisplay, component.getTags());
		
		// Display the component's other info.
		TextView otherDisplay = (TextView)findViewById(R.id.otherContent);
		otherDisplay.setText("Rarity: " + component.getRarity());
	}
}
