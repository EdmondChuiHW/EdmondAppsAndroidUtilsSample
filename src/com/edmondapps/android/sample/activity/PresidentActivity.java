package com.edmondapps.android.sample.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;

import com.edmondapps.android.sample.fragment.BitmapTaskFragment;
import com.edmondapps.android.sample.fragment.PresidentFragment;
import com.edmondapps.android.sample.framework.IPresident;
import com.edmondapps.android.sample.model.President;
import com.edmondapps.utils.android.activity.SinglePaneActivity;
import com.edmondapps.utils.android.annotaion.ParentActivity;

@ParentActivity(PresidentsActivity.class)
public class PresidentActivity extends SinglePaneActivity implements
		PresidentFragment.Callback,
		BitmapTaskFragment.Callback {

	public static void startWithPresident(Context context, President president) {
		Intent intent = new Intent(context, PresidentActivity.class);
		intent.putExtra(IPresident.TAG, president);
		context.startActivity(intent);
	}

	@Override
	protected Fragment onCreateFragment() {
		return PresidentFragment.newInstance(
				getIntent().<President> getParcelableExtra(IPresident.TAG));
	}

	@Override
	public void onBitmapLoaded(BitmapTaskFragment f, Bitmap bitmap) {
		if (bitmap != null) {
			((PresidentFragment)getFragment()).onBitmapLoaded(bitmap);
		} else {
			((PresidentFragment)getFragment()).onBitmapLoadFailed();
		}
		f.finish();
	}
}
