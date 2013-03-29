package com.edmondapps.android.sample.activity;

import android.support.v4.app.Fragment;
import android.widget.ListView;

import com.edmondapps.android.sample.R;
import com.edmondapps.android.sample.fragment.PresidentsFragment;
import com.edmondapps.android.sample.framework.IPresident;
import com.edmondapps.android.sample.model.President;
import com.edmondapps.utils.android.activity.SinglePaneActivity;

public class PresidentsActivity extends SinglePaneActivity implements PresidentsFragment.Callback {

	@Override
	protected Fragment onCreateFragment() {
		return new PresidentsFragment();
	}

	@Override
	protected int onCreateLayoutId() {
		return R.layout.activity_presidents;
	}

	@Override
	protected int onCreateFragmentLayoutId() {
		return R.id.frame_main;
	}

	@Override
	public void onPresidentSelected(PresidentsFragment f, IPresident p, ListView v, int position) {
		PresidentActivity.startWithPresident(this, President.valueOf(p));
	}
}
