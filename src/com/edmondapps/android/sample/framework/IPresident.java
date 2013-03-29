package com.edmondapps.android.sample.framework;

import com.edmondapps.utils.android.json.Jsonable;
import com.edmondapps.utils.java.Identifiable;

public interface IPresident extends Identifiable, Jsonable {
	public static final String TAG = "IPresident";

	String getName();

	IParty getParty();

	int getBirthyear();
}
