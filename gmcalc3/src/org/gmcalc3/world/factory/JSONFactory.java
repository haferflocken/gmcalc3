package org.gmcalc3.world.factory;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gmcalc3.util.Handies;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

public abstract class JSONFactory<E> implements Factory<E> {
	
	public static final String EXTENSION = ".json";
	
	protected final FileFilter extensionFilter = new FileFilter() {
		public boolean accept(File f) {
			if (f.isDirectory())
				return true;
			return f.getName().endsWith(EXTENSION);
		}
	};
	
	protected File directory;
	protected List<File> filesToLoad;
	protected int loadIndex;
	protected Map<String, E> loadedValues;
	
	@Override
	public void setDirectory(File dir) {
		// Throw an exception if dir isn't a directory.
		if (!dir.isDirectory())
			throw new IllegalArgumentException("dir must be a directory.");
		
		// Set the directory and get rid of old loaded values.
		directory = dir;
		filesToLoad = new ArrayList<File>();
		loadIndex = 0;
		loadedValues = new HashMap<String, E>();
		
		// Browse the directory and build a list of files to attempt to load.
		Deque<File> fileStack = new ArrayDeque<File>();
		fileStack.addLast(directory);
		while (fileStack.size() > 0) {
			File f = fileStack.removeLast();
			// If the file is a directory, add its subfiles to the stack.
			if (f.isDirectory()) {
				File[] subFiles = f.listFiles(extensionFilter);
				for (File s : subFiles)
					fileStack.addLast(s);
			}
			// Otherwise, add it to filesToLoad, as the extension has already been checked.
			else {
				filesToLoad.add(f);
			}
		}
	}

	@Override
	public int getDirectorySize() {
		return filesToLoad.size();
	}

	@Override
	public int getNumLoadAttempts() {
		return loadIndex;
	}

	@Override
	public void loadNext() {
		// Get the file and read it in.
		File f = filesToLoad.get(loadIndex);
		String contents = Handies.readFile(f);
			
		if (contents != null) {
			// Once we have read in the file, tokenize it.
			JSONTokener tokener = new JSONTokener(contents);
			try {
				// Make the instance.
				JSONObject jsonObject = (JSONObject)tokener.nextValue();
				String path = f.getAbsolutePath().substring(directory.getAbsolutePath().length());
				if (path.charAt(0) == '/')
					path = path.substring(1);
				E instance = makeInstance(path, jsonObject);
				if (instance != null)
					loadedValues.put(path, instance);
			}
			catch (JSONException e) {
				Log.d("gmcalc3-json", "Failed to load " + f.getName() + " as JSON: " + e.getMessage());
			}
		}
		
		loadIndex++;
	}

	@Override
	public boolean isFinished() {
		return loadIndex == filesToLoad.size();
	}

	@Override
	public Map<String, E> getLoadedValues() {
		return loadedValues;
	}
	
	/**
	 * Make an instance of E from the tokens read from the file at path relative to the directory
	 * this factory works with.
	 * @param path
	 *			The path relative to the directory that the tokens were read from.
	 * @param tokener
	 *			The tokener that tokenized the file's contents.
	 * @return
	 *			An instance of E made from path and tokener, or null if one could not be made.
	 */
	protected abstract E makeInstance(String path, JSONObject jsonObject) throws JSONException;

}
