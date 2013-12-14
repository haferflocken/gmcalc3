// An interface for factories that load things one at a time.

package org.gmcalc3.world.factory;

import java.io.File;
import java.util.Map;

/**
 * An interface for anything that can read files from a directory in order to instantiate objects.
 * 
 * @author John Werner
 *
 * @param <E> The class of objects this factory loads.
 */

public interface Factory<E> {
	
	/**
	 * Set the directory this factory loads from.
	 * 
	 * @param dir
	 *			The directory to load from.
	 */
	void setDirectory(File dir);
	
	/**
	 * @return
	 *			The number of files in the current directory that the factory will attempt to load.
	 *			It does not need to succeed at loading this many files, just needs to try to. This is used to get
	 *			an indication of progress.
	 */
	int getDirectorySize();
	
	/**
	 * @return
	 *			The number of files that the factory has tried to load- this should be the number of times loadNext()
	 *			has been called since the last call to setDirectory(), and should be in the range of [0, getDirectorySize()].
	 */
	int getNumLoadAttempts();
	
	/**
	 * Attempt to load the next file.
	 */
	void loadNext();
	
	/**
	 * @return
	 *			getNumLoadAttempts() == getDirectorySize()
	 */
	boolean isFinished();
	
	/**
	 * @return
	 *			 A map of file paths relative to the directory the factory is loading from to the objects the factory instantiated.
	 */
	Map<String, E> getLoadedValues();
	
}
