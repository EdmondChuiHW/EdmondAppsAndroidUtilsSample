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
