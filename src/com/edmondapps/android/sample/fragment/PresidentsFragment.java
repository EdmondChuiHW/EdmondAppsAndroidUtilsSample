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

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnActionExpandListener;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.edmondapps.android.sample.R;
import com.edmondapps.android.sample.column.PartyCol;
import com.edmondapps.android.sample.column.PresidentCol;
import com.edmondapps.android.sample.database.PresidentDatabase;
import com.edmondapps.android.sample.framework.IPresident;
import com.edmondapps.android.sample.model.President;
import com.edmondapps.utils.android.Utils;
import com.edmondapps.utils.android.annotaion.FragmentName;
import com.edmondapps.utils.android.database.DatabaseFilter;
import com.edmondapps.utils.android.database.SimpleCursorLoader;

@FragmentName(PresidentsFragment.TAG)
public class PresidentsFragment extends SherlockListFragment implements
        Filterable,
        LoaderCallbacks<Cursor> {

    public static final String TAG = "PresidentsFragment";

    private static final int PRESIDENTS_LOADER = 1;

    public interface Callback {
        void onPresidentSelected(PresidentsFragment f, IPresident p, ListView v, int position);
    }

    private Callback mCallback;
    private PresidentAdapter mAdapter;
    private PresidentDatabase mDatabase;

    private DatabaseFilter mFilter;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof Callback) {
            mCallback = (Callback)activity;
        } else {
            throw new AssertionError(activity + " must implement Callback.");
        }

        mDatabase = new PresidentDatabase(activity);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.frag_president, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchItem.setOnActionExpandListener(new OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                getLoaderManager().restartLoader(PRESIDENTS_LOADER, null, PresidentsFragment.this);
                return true;
            }
        });
        SearchView searchView = (SearchView)searchItem.getActionView();
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getLoaderManager().restartLoader(PRESIDENTS_LOADER, null, this);

        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mCallback.onPresidentSelected(this, President.Builder.fromCursor(mAdapter.getItem(position)), l, position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.getCursor().close();
        mDatabase.close();
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new DatabaseFilter(mDatabase, PresidentCol.NAME, PartyCol.NAME) {
                @Override
                protected void publishResults(CharSequence constraint, int count, Cursor c) {
                    mAdapter.changeCursor(c);
                }
            };
        }
        return mFilter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle b) {
        return new SimpleCursorLoader(getActivity()) {
            @Override
            public Cursor loadInBackground() {
                mDatabase.setOrderBy(PresidentCol.NAME);
                return mDatabase.query();
            }
        };
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        mAdapter = new PresidentAdapter(getActivity(), c,
                Utils.hasHoneyComb() ?
                        android.R.layout.simple_list_item_activated_2 :
                        android.R.layout.simple_list_item_2);
        setListAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private static class PresidentAdapter extends SimpleCursorAdapter {
        public PresidentAdapter(Context context, Cursor c, int listItemId) {
            super(context, listItemId, c,
                    new String[] {PresidentCol.NAME, PartyCol.NAME},
                    new int[] {android.R.id.text1, android.R.id.text2}, 0);
        }

        @Override
        public Cursor getItem(int position) {
            // AOSP, although no guarantee it will stay unchanged
            return (Cursor)super.getItem(position);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
