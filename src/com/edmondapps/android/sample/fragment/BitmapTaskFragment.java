/*
 * Copyright 2013 Edmond Chui
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.edmondapps.android.sample.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import com.edmondapps.utils.android.Logs;
import com.edmondapps.utils.android.fragment.AbstractAsyncFragment;
import com.edmondapps.utils.java.IoUtils;

public class BitmapTaskFragment extends AbstractAsyncFragment<String, Void, Bitmap> {
	public static final String TAG = "BitmapTaskFragment";

	private static final String KEY_URL = "key_url";

	public interface Callback {
		void onBitmapLoaded(BitmapTaskFragment f, Bitmap b);
	}

	public static BitmapTaskFragment newInstance(String url) {
		Bundle bundle = new Bundle();
		bundle.putString(KEY_URL, url);

		BitmapTaskFragment fragment = new BitmapTaskFragment();
		fragment.setArguments(bundle);

		return fragment;
	}

	private Callback mCallback;
	private String mUrl;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (activity instanceof Callback) {
			mCallback = (Callback)activity;
		} else {
			throw new IllegalStateException(activity + " must implement Callback.");
		}

		mUrl = getArguments().getString(KEY_URL);
	}

	public void loadNewBitmap(String url, Bitmap inBitmap) {
		mUrl = url;
		newAsyncTask(onCreateAsyncTask());
	}

	public void finish() {
		if (!isAdded()) {
			getFragmentManager()
					.beginTransaction()
					.remove(this)
					.commit();
		}
	}

	@Override
	protected AsyncTask<String, Void, Bitmap> onCreateAsyncTask() {
		return new AsyncTask<String, Void, Bitmap>() {
			private static final int MAX_RETRIES = 3;
			private int mRetryCount;

			@Override
			protected Bitmap doInBackground(String... url) {
				Bitmap bitmap = null;

				HttpURLConnection conn = null;
				InputStream stream = null;
				try {
					conn = IoUtils.newConnection(mUrl);
					stream = conn.getInputStream();

					bitmap = BitmapFactory.decodeStream(stream);
				} catch (IOException e) {
					mRetryCount++;
					Logs.e(TAG, "mRetryCount: " + mRetryCount, e);
					if (mRetryCount <= MAX_RETRIES) {
						return doInBackground(url);
					}
				} finally {
					IoUtils.quietClose(stream);
					IoUtils.quietDisconnet(conn);
				}

				return bitmap;
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				super.onPostExecute(result);
				mCallback.onBitmapLoaded(BitmapTaskFragment.this, result);
			}

			@Override
			protected void onCancelled(Bitmap result) {
				if (result != null) {
					result.recycle();
				}
				mCallback.onBitmapLoaded(BitmapTaskFragment.this, null);
			}
		};
	}
}
