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
package com.edmondapps.android.sample.model;

import android.provider.BaseColumns;

import com.edmondapps.android.sample.column.PartyCol;
import com.edmondapps.android.sample.column.PresidentCol;
import com.edmondapps.utils.android.database.DatabaseTable;

public final class Presidents {
	private Presidents() {
		throw new AssertionError("nice try");
	}

	public static final Table DATABASE_TABLE = new Table();

	public static final class Table implements DatabaseTable {
		private Table() {
		}

		@Override
		public String onCreateTableCommand() {
			return new StringBuilder()
					.append("CREATE TABLE ").append(getTableName())
					.append("( ")
					.append(BaseColumns._ID).append(" INTEGER PRIMARY KEY").append(", ")
					.append(PartyCol.NAME).append(" TEXT").append(", ")
					.append(PresidentCol.NAME).append(" TEXT UNIQUE").append(", ")
					.append(PresidentCol.BIRTHYEAR).append(" INTEGER")
					.append(" )")
					.toString();
		}

		@Override
		public int getTableVersion() {
			return 1;
		}

		@Override
		public String getTableName() {
			return "parties";
		}
	};
}
