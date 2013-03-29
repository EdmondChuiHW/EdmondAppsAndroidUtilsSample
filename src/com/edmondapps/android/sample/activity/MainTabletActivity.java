package com.edmondapps.android.sample.activity;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.widget.ListView;

import com.edmondapps.android.sample.fragment.BitmapTaskFragment;
import com.edmondapps.android.sample.fragment.PresidentFragment;
import com.edmondapps.android.sample.fragment.PresidentsFragment;
import com.edmondapps.android.sample.framework.IPresident;
import com.edmondapps.utils.android.Utils;
import com.edmondapps.utils.android.activity.DualPaneActivity;

public class MainTabletActivity extends DualPaneActivity implements
		PresidentsFragment.Callback,
		PresidentFragment.Callback,
		BitmapTaskFragment.Callback {

	@Override
	protected Fragment onCreateFragment() {
		return new PresidentsFragment();
	}

	@Override
	protected Fragment onCreateDetailFragment() {
		return new PresidentFragment();
	}

	@Override
	public void onPresidentSelected(PresidentsFragment f, IPresident p, ListView v, int position) {
		if (Utils.hasHoneyComb()) {
			v.setItemChecked(position, true);
		}
		((PresidentFragment)getDetailFragment()).setPresident(p);
	}

	@Override
	public void onBitmapLoaded(BitmapTaskFragment f, Bitmap bitmap) {
		if (bitmap != null) {
			((PresidentFragment)getDetailFragment()).onBitmapLoaded(bitmap);
		} else {
			((PresidentFragment)getDetailFragment()).onBitmapLoadFailed();
		}
		f.finish();
	}
}
