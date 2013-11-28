package org.gmcalc3;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {
	
	private String[] titles;
	private Fragment[] fragments;

	public TabAdapter(FragmentManager fm, String[] titles, Fragment[] fragments) {
		super(fm);
		this.titles = titles;
		this.fragments = fragments;
	}
	
	@Override
	public Fragment getItem(int position) {
		return fragments[position];
	}
	
	@Override
	public int getCount() {
		return fragments.length;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position];
	}
}
