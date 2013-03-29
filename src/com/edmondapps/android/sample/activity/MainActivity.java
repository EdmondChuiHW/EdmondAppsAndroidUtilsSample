package com.edmondapps.android.sample.activity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.edmondapps.android.sample.R;
import com.edmondapps.android.sample.database.PresidentDatabase;
import com.edmondapps.android.sample.model.President;
import com.edmondapps.android.sample.model.Presidents;
import com.edmondapps.utils.android.Logs;
import com.edmondapps.utils.java.IoUtils;
import com.google.gson.stream.JsonReader;

public class MainActivity extends SherlockFragmentActivity {
	public static final String TAG = "MainActivity";
	private AsyncTask<Void, Void, Void> mAsyncTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_loading);

		mAsyncTask = new FillDatabaseTask(this).execute((Void[])null);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mAsyncTask != null) {
			mAsyncTask.cancel(false);
			mAsyncTask = null;
		}
	}

	protected void onDoneTask() {
		launchActivityAndFinish(getResources().getBoolean(R.bool.ed__has_7_inch) ?
				MainTabletActivity.class :
				PresidentsActivity.class);
	}

	private void launchActivityAndFinish(Class<? extends Activity> activity) {
		Intent intent = new Intent(this, activity);
		startActivity(intent);
		finish();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	private static class FillDatabaseTask extends AsyncTask<Void, Void, Void> {
		private final WeakReference<MainActivity> mActivity;

		public FillDatabaseTask(MainActivity activity) {
			mActivity = new WeakReference<MainActivity>(activity);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				MainActivity activity = mActivity.get();
				if (activity != null) {
					fillDatabase(activity);
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			return null;
		}

		private void fillDatabase(Activity a) throws IOException {
			JsonReader r = new JsonReader(new InputStreamReader(a.getAssets().open("presidents.json")));

			PresidentDatabase database = new PresidentDatabase(a);
			SQLiteDatabase db = database.getWritableDatabase();

			db.beginTransaction();
			try {
				r.beginArray();
				while (r.hasNext()) {
					if (isCancelled()) {
						return;// endTransaction(), revert changes
					}

					long id = db.insert(database.getDatabaseName(), null,
							President.Builder.fromJson(-1, r).toContentValues(Presidents.DATABASE_TABLE));
					Logs.i(TAG, "inserted id: " + id);
				}
				r.endArray();

				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
				db.close();
				database.close();
				IoUtils.quietClose(r);
			}
		}

		@Override
		protected void onPostExecute(Void result) {
			MainActivity activity = mActivity.get();
			if (activity != null) {
				activity.onDoneTask();
			}
		}
	}
}
