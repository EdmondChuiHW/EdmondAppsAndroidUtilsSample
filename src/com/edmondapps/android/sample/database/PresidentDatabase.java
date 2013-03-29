package com.edmondapps.android.sample.database;

import android.content.Context;

import com.edmondapps.android.sample.model.President;
import com.edmondapps.android.sample.model.Presidents;
import com.edmondapps.utils.android.database.Database;

public class PresidentDatabase extends Database<President> {
	public PresidentDatabase(Context context) {
		super(context, Presidents.DATABASE_TABLE);
	}

}
