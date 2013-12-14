package org.gmcalc3;

import org.gmcalc3.world.World;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

public class WorldExplorerFragment extends Fragment {
	
	private World[] worlds; // The worlds to display.

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_world_explorer, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setupWorldGrid();
	}
	
	private void setupWorldGrid() {
		GridLayout worldGrid = (GridLayout)getView();
		// Clear any child views if there are no worlds to show.
		if (worlds == null) {
			worldGrid.removeAllViews();
		}
		// If there are worlds to show, show them.
		else {
			for (World w : worlds) {
				Button button = new Button(worldGrid.getContext());
				//button.setLayoutParams(new GridLayout.LayoutParams());
				button.setText(w.getName());
				worldGrid.addView(button);
			}
		}
	}
	
	public void setWorlds(World[] w) {
		worlds = w;
		setupWorldGrid();
	}
	
}
