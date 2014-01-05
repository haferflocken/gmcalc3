package org.gmcalc3.util;

import org.json.JSONArray;
import org.json.JSONException;

public final class Handies {

	public static String[] jsonArrayToStringArray(JSONArray jsonArray) throws JSONException {
		int length = jsonArray.length();
		String[] out = new String[length];
		for (int i = 0; i < length; i++) {
			out[i] = jsonArray.getString(i);
		}
		return out;
	}
	
}
