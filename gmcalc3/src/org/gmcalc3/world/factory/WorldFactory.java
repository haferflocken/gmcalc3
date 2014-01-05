package org.gmcalc3.world.factory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.gmcalc3.world.Component;
import org.gmcalc3.world.ItemBase;
import org.gmcalc3.world.World;
import org.gmcalc3.world.Character;
import org.hafermath.expression.ExpressionBuilder;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.support.v4.util.ArrayMap;
import android.util.Log;

public class WorldFactory implements Factory<World> {
	
	public static final String RULES_FILE_NAME = "rules.json";
	public static final String PREFIX_DIR_NAME = "prefixes";
	public static final String MATERIAL_DIR_NAME = "materials";
	public static final String ITEMBASE_DIR_NAME = "itemBases";
	public static final String CHARACTER_DIR_NAME = "characters";
	
	private File directory;
	private List<File> worldDirsToLoad;
	private int loadIndex;
	private Map<String, World> loadedWorlds;
	
	private ComponentFactory prefixFactory;
	private ComponentFactory materialFactory;
	private ItemBaseFactory itemBaseFactory;
	private CharacterFactory characterFactory;
	private ExpressionBuilder expressionBuilder;
	
	public WorldFactory() {
		prefixFactory = new ComponentFactory();
		materialFactory = new ComponentFactory();
		itemBaseFactory = new ItemBaseFactory();
		characterFactory = new CharacterFactory();
		expressionBuilder = new ExpressionBuilder();
	}

	@Override
	public void setDirectory(File dir) {
		// Throw an exception if dir isn't a directory.
		if (!dir.isDirectory())
			throw new IllegalArgumentException("dir must be a directory.");
			
		// Set the directory and get rid of old loaded values.
		directory = dir;
		worldDirsToLoad = new ArrayList<File>();
		loadIndex = 0;
		loadedWorlds = new ArrayMap<String, World>();
			
		// Browse the directory and build a list of directories to attempt to load.
		File[] potentialFiles = directory.listFiles();
		for (File f : potentialFiles) {
			// If the file is a directory, it might be a valid world directory.
			if (f.isDirectory()) {
				// Verify the directory structure.
				// A world needs rules and folders named prefixes, materials, itemBases and characters.
				File prefixDir = new File(f, PREFIX_DIR_NAME);
				if (prefixDir.exists()) {
					File materialDir = new File(f, MATERIAL_DIR_NAME);
					if (materialDir.exists()) {
						File itemBaseDir = new File(f, ITEMBASE_DIR_NAME);
						if (itemBaseDir.exists()) {
							File characterDir = new File(f, CHARACTER_DIR_NAME);
							if (characterDir.exists()) {
								File rulesFile = new File(f, RULES_FILE_NAME);
								if (rulesFile.exists()) {
									worldDirsToLoad.add(f);
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public int getDirectorySize() {
		return worldDirsToLoad.size();
	}

	@Override
	public int getNumLoadAttempts() {
		return loadIndex;
	}

	@Override
	public void loadNext() {
		// Get the files and directories that we need.
		File worldDir = worldDirsToLoad.get(loadIndex);
		File ruleFile = new File(worldDir, RULES_FILE_NAME);
		File prefixDir = new File(worldDir, PREFIX_DIR_NAME);
		File materialDir = new File(worldDir, MATERIAL_DIR_NAME);
		File itemBaseDir = new File(worldDir, ITEMBASE_DIR_NAME);
		File characterDir = new File(worldDir, CHARACTER_DIR_NAME);
		
		// Load the world.
		JSONObject parsedRules = loadAsJson(ruleFile);
		if (parsedRules == null) {
			loadIndex++;
			return;
		}
		
		// Multithread what loading we can.
		FactoryRunner<Component> prefixThread = new FactoryRunner<Component>(prefixFactory, prefixDir);
		prefixThread.start();
		FactoryRunner<Component> materialThread = new FactoryRunner<Component>(materialFactory, materialDir);
		materialThread.start();
		FactoryRunner<ItemBase> itemBaseThread = new FactoryRunner<ItemBase>(itemBaseFactory, itemBaseDir);
		itemBaseThread.start();
		
		// Wait until we're done.
		try {
			prefixThread.join();
			materialThread.join();
			itemBaseThread.join();
		}
		catch (InterruptedException e) {
			Log.d("gmcalc3-json", "Factory runner interrupted: ", e);
			prefixThread.interrupt();
			materialThread.interrupt();
			itemBaseThread.interrupt();
			loadIndex++;
			return;
		}
		
		// Get the loaded data.
		Map<String, Component> prefixes = prefixThread.getLoadedValues();
		Map<String, Component> materials = materialThread.getLoadedValues();
		Map<String, ItemBase> itemBases = itemBaseThread.getLoadedValues();
		
		// Try to make a world and load the characters.
		try {
			World world = new World(parsedRules, expressionBuilder, prefixes, materials, itemBases);
			
			characterFactory.setWorld(world);
			Map<String, Character> characters = runFactory(characterFactory, characterDir);
			world.setCharacterMap(characters);
			
			loadedWorlds.put(worldDir.getName(), world);
			
			world.logContents();
		}
		catch (JSONException e) {
			Log.d("gmcalc3-json", "Failed to load world " + worldDir.getName() + ": " + e.getMessage());
		}
		
		loadIndex++;
	}

	@Override
	public boolean isFinished() {
		return loadIndex == worldDirsToLoad.size();
	}

	@Override
	public Map<String, World> getLoadedValues() {
		return loadedWorlds;
	}
	
	private JSONObject loadAsJson(File f) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(f);
			StringBuilder contentBuilder = new StringBuilder();
			while (scanner.hasNextLine()) {
				contentBuilder.append(scanner.nextLine());
			}
			
			// Once we have read in the file, tokenize it.
			JSONTokener tokener = new JSONTokener(contentBuilder.toString());
			try {
				return (JSONObject)tokener.nextValue();
			}
			catch (JSONException e) {
				Log.d("gmcalc3-json", "Failed to load " + f.getName() + " as JSON: " + e.getMessage());
			}
		}
		catch (FileNotFoundException e) {}
		finally {
			if (scanner != null)
				scanner.close();
		}
		return null;
	}
	
	private static <E> Map<String, E> runFactory(Factory<E> factory, File dir) {
		factory.setDirectory(dir);
		while (!factory.isFinished()) {
			factory.loadNext();
		}
		return factory.getLoadedValues();
	}
	
	private static final class FactoryRunner<Q> extends Thread {
		
		private final Factory<Q> factory;
		
		private FactoryRunner(Factory<Q> factory, File dir) {
			this.factory = factory;
			factory.setDirectory(dir);
		}

		@Override
		public void run() {
			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
			while (!factory.isFinished() && !isInterrupted()) {
				factory.loadNext();
			}
		}
		
		private Map<String, Q> getLoadedValues() {
			return factory.getLoadedValues();
		}
		
	}

}
