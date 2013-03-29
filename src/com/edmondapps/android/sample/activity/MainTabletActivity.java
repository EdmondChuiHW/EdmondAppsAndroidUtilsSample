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
