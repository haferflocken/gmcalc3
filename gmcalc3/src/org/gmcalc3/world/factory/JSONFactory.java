package org.gmcalc3.world.factory;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public abstract class JSONFactory<E> implements Factory<E> {
	
	public static final String EXTENSION = ".json";
	
	protected final FileFilter extensionFilter = new FileFilter() {
		public boolean accept(File f) {
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
		// Get the file and read it in with a scanner.
		File f = filesToLoad.get(loadIndex);
		Scanner scanner = null;
		try {
			scanner = new Scanner(f);
			StringBuilder contentBuilder = new StringBuilder();
			while (scanner.hasNext()) {
				contentBuilder.append(scanner.next());
			}
			
			// Once we have read in the file, tokenize it.
			JSONTokener tokener = new JSONTokener(contentBuilder.toString());
			try {
				// Make the instance.
				JSONObject jsonObject = (JSONObject)tokener.nextValue();
				String path = f.getAbsolutePath().substring(directory.getAbsolutePath().length());
				E instance = makeInstance(path, jsonObject);
				if (instance != null)
					loadedValues.put(path, instance);
			}
			catch (JSONException e) {
			}
		}
		catch (FileNotFoundException e) {}
		finally {
			if (scanner != null)
				scanner.close();
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
