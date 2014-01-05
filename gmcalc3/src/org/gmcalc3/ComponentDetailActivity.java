package org.gmcalc3;

import org.gmcalc3.world.Component;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

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
		
		// Set the title to the component name.
		setTitle(component.getName());
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
}
