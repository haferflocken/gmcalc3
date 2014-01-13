package org.gmcalc3;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

/**
 * A fragment enclosing an ExpandableListView and an ExpandableListAdapter.
 * Inspired by android.app.ListFragment and some code was adapted from there.
 * 
 * @author John Werner
 */

public class ExpandableListFragment extends Fragment {
	
	final private AdapterView.OnItemClickListener myOnClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
			onListItemClick((ExpandableListView)parent, v, position, id);
		}
	};
	
	private ExpandableListAdapter myAdapter;
	private ExpandableListView myList;
	
	public ExpandableListFragment() {
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.layout_expandable_list_view, container, false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ensureList();
	}
	
	@Override
	public void onDestroyView() {
		myList = null;
		super.onDestroyView();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("Stop crashing.", "Cool.");
	}
	
	public void onListItemClick(ExpandableListView l, View v, int position, long id) {
	}
	
	public void setExpandableListAdapter(ExpandableListAdapter adapter) {
		myAdapter = adapter;
		if (myList != null) {
			myList.setAdapter(myAdapter);
		}
	}
	
	public void setSelection(int position) {
		ensureList();
		myList.setSelection(position);
	}
	
	public long getSelectedItemPosition() {
		ensureList();
		return myList.getSelectedItemPosition();
	}
	
	public long getSelectedItemId() {
		ensureList();
		return myList.getSelectedItemId();
	}
	
	public ExpandableListView getExpandableListView() {
		ensureList();
		return myList;
	}
	
	public ExpandableListAdapter getExpandableListAdapter() {
		ensureList();
		return myAdapter;
	}
	
	private void ensureList() {
		if (myList != null)
			return;
		View root = getView();
		if (root == null)
			throw new IllegalStateException("Content view not yet created");
		if (root instanceof ExpandableListView)
			myList = (ExpandableListView)root;
		else 
			return;
		myList.setOnItemClickListener(myOnClickListener);
		if (myAdapter != null) {
			myList.setAdapter(myAdapter);
		}
	}
}
