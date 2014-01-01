package org.gmcalc3;

import java.io.File;
import java.util.Map;

import org.gmcalc3.world.World;
import org.gmcalc3.world.factory.WorldFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;

/**
 * Loads the device worlds so that the app may function properly.
 * 
 * @author John Werner
 */

public class InitializationActivity extends Activity {
	
	private AsyncTask<Void, Integer, Map<String, World>> deviceWorldsTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_initialization);
		
		// Load the device worlds.
		deviceWorldsTask = new AsyncTask<Void, Integer, Map<String, World>>() {

			@Override
			protected Map<String, World> doInBackground(Void... params) {
				File worldsDir = getExternalFilesDir(null);
				if (worldsDir == null)
					return null;
				
				WorldFactory deviceWorldsFactory = new WorldFactory();
				deviceWorldsFactory.setDirectory(worldsDir);
				int directorySize = deviceWorldsFactory.getDirectorySize();
				
				while (!deviceWorldsFactory.isFinished() && !isCancelled()) {
					deviceWorldsFactory.loadNext();
					publishProgress((deviceWorldsFactory.getNumLoadAttempts() * 100) / directorySize);
				}
				
				return deviceWorldsFactory.getLoadedValues();
			}
			
			@Override
			protected void onProgressUpdate(Integer... progress) {
				ProgressBar progressBar = (ProgressBar)findViewById(R.id.loadProgress);
				progressBar.setProgress(progress[0]);
			}
			
			@Override
			protected void onPostExecute(Map<String, World> result) {
				DeviceWorldsActivity.deviceWorlds = result;
				deviceWorldsTask = null;
				Intent intent = new Intent(InitializationActivity.this, TableActivity.class);
				startActivity(intent);
				finish();
			}
			
		}.execute((Void[])null);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (deviceWorldsTask != null) {
			deviceWorldsTask.cancel(false);
			deviceWorldsTask = null;
		}
	}
	
}
