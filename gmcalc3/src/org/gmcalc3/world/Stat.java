package org.gmcalc3.world;

import java.util.Arrays;

import org.hafermath.expression.Expression;
import org.hafermath.expression.ExpressionBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Stat {
	
	public static final String STRINGS_KEY = "strings";
	public static final String RANGE_KEY = "range";
	public static final String EXPRESSION_KEY = "expression";

	//The different parts of a stat.
	private String[] strings;
	private Range range;
	private Expression expression;
	
	// Constructors.
	public Stat(String[] strings, Range range, Expression expression) {
		this.strings = strings;
		this.range = range;
		this.expression = expression;
	}
	
	public Stat() {
		this(null, null, null);
	}
	
	public Stat(String rawExpression, ExpressionBuilder expBuilder) {
		this(null, null, expBuilder.makeExpression(rawExpression));
	}
	
	public Stat(JSONObject values, ExpressionBuilder expBuilder) throws JSONException {
		this();
		
		// Get the strings.
		JSONArray rawStrings = values.optJSONArray(STRINGS_KEY);
		if (rawStrings != null) {
			strings = new String[rawStrings.length()];
			for (int i = 0; i < strings.length; i++) {
				strings[i] = rawStrings.getString(i);
			}
		}
		
		// Get the range.
		JSONArray rawRange = values.optJSONArray(RANGE_KEY);
		if (rawRange != null) {
			range = new Range(rawRange.getInt(0), rawRange.getInt(1));
		}
		
		// Get the expression.
		String rawExpression = values.optString(EXPRESSION_KEY);
		if (rawExpression != null) {
			expression = expBuilder.makeExpression(rawExpression);
		}
	}
	
	// Accessors.
	public String[] getStrings() {
		return strings;
	}
	
	public Range getRange() {
		return range;
	}
	
	public Expression getExpression() {
		return expression;
	}

	// Return a copy of this stat.
	public Stat copy() {
		Stat out = new Stat();
		if (strings != null)
			out.strings = Arrays.copyOf(strings, strings.length);
		if (range != null)
			out.range = new Range(range.getMin(), range.getMax());
		if (expression != null)
			out.expression = expression.copy();
		return out;
	}
	
	// Take a stat and merge its values into this, adding ranges and numbers.
	public void merge(Stat other) {
		// Merge the strings.
		if (other.strings != null) {
			if (strings == null) {
				strings = Arrays.copyOf(other.strings, other.strings.length);
			}
			else {
				// Count the number of strings in the incoming array that we don't already have.
				int numNew = other.strings.length; // Assume we are adding nothing but new strings.
				for (int q, i = 0; i < other.strings.length; i++) {
					// Loop through strings to see if we have this or not. If we do, get rid of the assumption.
					for (q = 0; q < strings.length; q++) {
						if (strings[q].equals(other.strings[i])) {
							numNew--;
							break;
						}
					}
				}
					
				// Merge the new strings into the array.
				String[] newStrings = Arrays.copyOf(strings, strings.length + numNew);
				for (int n = strings.length, i = 0; i < other.strings.length; i++) {
					// See if the string is unique.
					boolean newString = true;
					for (int q = 0; q < strings.length; q++) {
						if (strings[q].equals(other.strings[i])) {
							newString = false;
							break;
						}
					}
					if (newString)
						newStrings[n++] = other.strings[i];
				}
				strings = newStrings;
			}
		}
		
		// Add the ranges.
		if (other.range != null) {
			if (range == null)
				range = new Range(other.range.getMin(), other.range.getMax());
			else
				range.add(other.range);
		}
		
		// Add the expressions.
		if (other.expression != null) {
			if (expression == null)
				expression = other.expression.copy();
			else
				expression = expression.addWith(other.expression);
		}
	}
	
	// Return an array of strings that represents the different parts of this stat.
	public String[] toDisplayStrings() {
		// Get a number to represent the expression.
		int expVal = (expression == null)? 0 : (int)expression.getValue();
		
		// Create the output array.
		String[] out;
		if (strings == null)
			out = new String[1];
		else if (range == null && expVal == 0)
			out = new String[strings.length];
		else
			out = new String[strings.length + 1];
		
		// If we have strings...
		if (strings != null) {
			// Add the strings in.
			for (int i = 0; i < strings.length; i++) {
				out[i] = strings[i];
			}
			// Return now if the range and expression don't need to be displayed.
			if (range == null && expVal == 0)
				return out;
			// Otherwise, add the range and val appropriately.
			if (range == null && expVal != 0)
				out[strings.length] = "" + expVal;
			else if (range != null && expVal == 0)
				out[strings.length] = range.toString();
			else 
				out[strings.length] = range.toString() + ((expVal < 0)? " - " + (expVal * -1) : " + " + expVal);
			// Return.
			return out;
		}
		
		// If there are no strings, the only output is the range and expression.
		if (range == null)
			out[0] = "" + expVal;
		else if (expVal == 0)
			out[0] = range.toString();
		else
			out[0] = range.toString() + ((expVal < 0)? " - " + (expVal * -1) : " + " + expVal);
		//Return.
		return out;
	}
	
	public String toString() {
		String[] dispStrings = toDisplayStrings();
		if (dispStrings.length == 1)
			return dispStrings[0];
		else
			return  Arrays.toString(dispStrings);
	}
}
