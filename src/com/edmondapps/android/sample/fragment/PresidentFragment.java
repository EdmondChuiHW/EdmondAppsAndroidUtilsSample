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

import static com.edmondapps.utils.android.Utils.findFragment;
import static com.edmondapps.utils.android.Utils.findView;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.edmondapps.android.sample.R;
import com.edmondapps.android.sample.framework.IPresident;
import com.edmondapps.android.sample.model.President;
import com.edmondapps.utils.android.annotaion.FragmentName;
import com.edmondapps.utils.android.view.LoadingViews;

@FragmentName(PresidentFragment.TAG)
public class PresidentFragment extends SherlockFragment {
	public static final String TAG = "PresidentFragment";

	private static final String KEY_BITMAP = "key_bitmap";

	public static PresidentFragment newInstance(President president) {
		Bundle bundle = new Bundle();
		bundle.putParcelable(IPresident.TAG, president);

		PresidentFragment fragment = new PresidentFragment();
		fragment.setArguments(bundle);

		return fragment;
	}

	public interface Callback {
	}

	@SuppressWarnings("unused")
	private Callback mCallback;
	private IPresident mPresident;
	private boolean mViewCreated;
	private View mParentView;
	private TextView mPresidentView;
	private ImageView mPresidentImageView;
	private ProgressBar mProgressBar;
	private TextView mPartyView;
	private TextView mBirthdayView;
	private Bitmap mBitmap;
	private LoadingViews<ImageView, ProgressBar> mLoadingViews;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		if (activity instanceof Callback) {
			mCallback = (Callback)activity;
		} else {
			throw new IllegalStateException(activity + " must implement Callback.");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null) {
			mBitmap = savedInstanceState.<Bitmap> getParcelable(KEY_BITMAP);
			mPresident = savedInstanceState.getParcelable(IPresident.TAG);
			return;
		}

		Bundle arguments = getArguments();
		if (arguments != null) {
			mPresident = arguments.getParcelable(IPresident.TAG);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putParcelable(KEY_BITMAP, mBitmap);
		outState.putParcelable(IPresident.TAG, President.valueOf(mPresident));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_president, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mParentView = findView(view, R.id.view_parent);
		mPresidentView = findView(view, R.id.lbl_president);
		mPresidentImageView = findView(view, R.id.img_president);
		mProgressBar = findView(view, android.R.id.progress);
		mPartyView = findView(view, R.id.view_party);
		mBirthdayView = findView(view, R.id.view_birthday);

		mLoadingViews = LoadingViews.of(mPresidentImageView, mProgressBar);

		mViewCreated = true;

		updateViews(savedInstanceState);
	}

	private void updateViews(Bundle savedInstanceState) {
		if (mViewCreated && (mPresident != null)) {
			if (savedInstanceState != null) {
				mBitmap = savedInstanceState.getParcelable(KEY_BITMAP);
				updateImage();
			} else {
				onLoadBitmap("http://lorempixel.com/400/200", mBitmap);
			}

			mParentView.setVisibility(View.VISIBLE);

			mPresidentView.setText(mPresident.getName());
			mPartyView.setText(mPresident.getParty().getName());
			mBirthdayView.setText(String.valueOf(mPresident.getBirthyear()));
		}
	}

	private void onLoadBitmap(String url, Bitmap inBitmap) {
		mLoadingViews.startLoading();

		FragmentManager manager = getFragmentManager();
		BitmapTaskFragment fragment = findBitmapTaskFragment(manager);

		if (fragment == null) {
			manager.beginTransaction()
					.add(BitmapTaskFragment.newInstance(url), BitmapTaskFragment.TAG)
					.commit();
		} else {
			fragment.loadNewBitmap(url, inBitmap);
		}
	}

	private BitmapTaskFragment findBitmapTaskFragment(FragmentManager manager) {
		return findFragment(manager, BitmapTaskFragment.TAG);
	}

	public void onBitmapLoaded(Bitmap bitmap) {
		if ( (mBitmap != null) && (mBitmap != bitmap)) {// identity check
			mBitmap.recycle();
		}
		mBitmap = bitmap;
		updateImage();
	}

	public void onBitmapLoadFailed() {
		mPresidentImageView.setImageResource(R.drawable.ic_launcher);
		mLoadingViews.doneLoading();
	}

	private void updateImage() {
		if (isAdded()) {
			mPresidentImageView.setImageBitmap(mBitmap);
			mLoadingViews.doneLoading();
		}
	}

	public void setPresident(IPresident president) {
		if (mPresident != president) { // identity check
			mPresident = president;
			updateViews(null);
		}
	}
}
