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
