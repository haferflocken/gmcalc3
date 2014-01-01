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
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.support.v4.util.ArrayMap;

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
	
	public WorldFactory() {
		prefixFactory = new ComponentFactory();
		materialFactory = new ComponentFactory();
		itemBaseFactory = new ItemBaseFactory();
		characterFactory = new CharacterFactory();
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
				File[] subFiles = f.listFiles();
				if (subFiles != null && subFiles.length >= 5) {
					// Count the number of things that meet the requirements.
					int checkCount = 0;
					for (File c : subFiles) {
						if (c.isDirectory()) {
							if (c.getName().equals(PREFIX_DIR_NAME))
								checkCount++;
							else if (c.getName().equals(MATERIAL_DIR_NAME))
								checkCount++;
							else if (c.getName().equals(ITEMBASE_DIR_NAME))
								checkCount++;
							else if (c.getName().equals(CHARACTER_DIR_NAME))
								checkCount++;
						}
						else if (c.getName().equals(RULES_FILE_NAME))
							checkCount++;
					}
					// If there is the right number of things, keep it.
					if (checkCount == 5) {
						worldDirsToLoad.add(f);
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
		Map<String, Component> prefixes = runFactory(prefixFactory, prefixDir);
		Map<String, Component> materials = runFactory(materialFactory, materialDir);
		Map<String, ItemBase> itemBases = runFactory(itemBaseFactory, itemBaseDir);
		try {
			World world = new World(parsedRules, null, prefixes, materials, itemBases);
			
			Map<String, Character> characters = runFactory(characterFactory, characterDir);
			world.setCharacterMap(characters);
			
			loadedWorlds.put(worldDir.getName(), world);
		}
		catch (JSONException e) {
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
			while (scanner.hasNext()) {
				contentBuilder.append(scanner.next());
			}
			
			// Once we have read in the file, tokenize it.
			JSONTokener tokener = new JSONTokener(contentBuilder.toString());
			try {
				return (JSONObject)tokener.nextValue();
			}
			catch (JSONException e) {
			}
		}
		catch (FileNotFoundException e) {}
		finally {
			if (scanner != null)
				scanner.close();
		}
		return null;
	}
	
	private <E> Map<String, E> runFactory(Factory<E> factory, File dir) {
		factory.setDirectory(dir);
		while (!factory.isFinished()) {
			factory.loadNext();
		}
		return factory.getLoadedValues();
	}

}
