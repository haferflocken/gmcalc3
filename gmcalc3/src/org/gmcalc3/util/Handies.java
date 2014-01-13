package org.gmcalc3.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;

import android.graphics.Typeface;
import android.widget.TextView;

public final class Handies {
	
	/**
	 * Turn a JSONArray into an array of Strings.
	 * 
	 * @param jsonArray
	 *			The json array to convert.
	 * @return
	 *			An array made by taking all the elements of jsonArray as strings.
	 * @throws JSONException
	 *			if any element of jsonArray is not a string.
	 */
	public static String[] jsonArrayToStringArray(JSONArray jsonArray) throws JSONException {
		int length = jsonArray.length();
		String[] out = new String[length];
		for (int i = 0; i < length; i++) {
			out[i] = jsonArray.getString(i);
		}
		return out;
	}
	
	/**
	 * Reads in a file as a string. Returns null if the file cannot be read.
	 * 
	 * @param file
	 *			The file to read.
	 * @return
	 *			A string representing the contents of the file.
	 */
	public static String readFile(File file) {
		Scanner scan = null;
		try {
			scan = new Scanner(file);
			StringBuilder builder = new StringBuilder();
			while (scan.hasNextLine()) {
				builder.append(scan.nextLine());
			}
			return builder.toString();
		}
		catch (FileNotFoundException e) {
			return null;
		}
		finally {
			if (scan != null)
				scan.close();
		}
	}
	
	/**
	 * Sort toSort by natural ordering, and move the corresponding elements of sortWith
	 * to maintain the original pairs. The two arrays must have the same length.
	 * 
	 * @param toSort
	 * @param sortWith
	 */
	public static <A extends Comparable<A>, B> void sortAndKeepPairs(A[] toSort, B[] sortWith) {
		int length = toSort.length;
		if (length != sortWith.length)
			throw new IllegalArgumentException("The arrays must be the same length.");
		
		// If it's so small it can only be sorted, return.
		if (length < 2)
			return;
		
		// This is a selection sort. It's not fast, but oh well.
		for (int i = 0; i < length; i++) {
			int smallest = i;
			for (int j = i + 1; j < length; j++) {
				if (toSort[j].compareTo(toSort[smallest]) < 0) {
					smallest = j;
				}
			}
			if (smallest > i) {
				A swapA = toSort[smallest];
				B swapB = sortWith[smallest];
				toSort[smallest] = toSort[i];
				sortWith[smallest] = sortWith[i];
				toSort[i] = swapA;
				sortWith[i] = swapB;
			}
		}
	}
	
	public static void displayInTextView(TextView view, Set<String> strings) {
		if (strings.size() == 0) {
			view.setText("None");
			view.setTypeface(null, Typeface.ITALIC);
		}
		else {
			String s = strings.toString();
			view.setText(s.substring(1, s.length() - 1));
			view.setTypeface(null, Typeface.NORMAL);
		}
	}
	
	public static void displayInTextView(TextView view, String[] strings) {
		if (strings.length == 0) {
			view.setText("None");
			view.setTypeface(null, Typeface.ITALIC);
		}
		else {
			String s = Arrays.toString(strings);
			view.setText(s.substring(1, s.length() - 1));
			view.setTypeface(null, Typeface.NORMAL);
		}
	}
	
	public static void displayInTextView(TextView view, String[][] strings) {
		if (strings.length == 0) {
			view.setText("None");
			view.setTypeface(null, Typeface.ITALIC);
		}
		else {
			StringBuilder builder = new StringBuilder();
			for (String[] array : strings) {
				if (array.length == 0) {
					builder.append("None\n");
				}
				else {
					String s = Arrays.toString(array);
					builder.append(s.substring(1, s.length() - 1));
					builder.append('\n');
				}
			}
			builder.deleteCharAt(builder.length() - 1);
			view.setText(builder.toString());
			view.setTypeface(null, Typeface.NORMAL);
		}
	}
}
