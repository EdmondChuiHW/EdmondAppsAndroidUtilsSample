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
package com.edmondapps.android.sample.database;

import android.content.Context;

import com.edmondapps.android.sample.model.President;
import com.edmondapps.android.sample.model.Presidents;
import com.edmondapps.utils.android.database.Database;

public class PresidentDatabase extends Database<President> {
    public PresidentDatabase(Context context) {
        super(context, Presidents.DATABASE_TABLE);
    }

}
