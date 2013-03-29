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

import java.io.IOException;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.edmondapps.android.sample.column.PartyCol;
import com.edmondapps.android.sample.framework.IParty;
import com.edmondapps.utils.android.json.JsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

public class Party implements IParty, Parcelable {
	public static final Creator<Party> CREATOR = new Creator<Party>() {
		@Override
		public Party createFromParcel(Parcel source) {
			return new Party(source);
		}

		@Override
		public Party[] newArray(int size) {
			return new Party[size];
		}
	};

	public static final class Builder {
		private String mName;

		public Builder setName(String name) {
			mName = name;
			return this;
		}

		public Party build() {
			return new Party(this);
		}

		public static final JsonBuilder<Party> JSON_BUILDER = new JsonBuilder<Party>() {
			@Override
			public Party build(long id, JsonReader reader) throws IOException {
				return fromJson(reader);
			}
		};

		public static final Party fromJson(JsonReader r) throws IOException {
			Builder b = new Builder();

			r.beginObject();
			while (r.hasNext()) {
				String name = r.nextName();
				if (PartyCol.NAME.equals(name)) {
					b.setName(r.nextString());
				} else {
					r.skipValue();
				}
			}
			r.endObject();

			return b.build();
		}

		public static final Party fromCursor(Cursor c) {
			Builder b = new Builder();
			b.setName(c.getString(c.getColumnIndex(PartyCol.NAME)));
			return b.build();
		}
	}

	public static Party valueOf(IParty party) {
		if (party instanceof Party) {
			return (Party)party;
		} else {
			return new Party(party);
		}
	}

	private final String mName;

	private Party(Builder b) {
		mName = b.mName;
	}

	private Party(Parcel source) {
		mName = source.readString();
	}

	public Party(String name) {
		mName = name;
	}

	public Party(IParty party) {
		mName = party.getName();
	}

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("Party [mName=").append(mName)
				.append("]")
				.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ( (mName == null) ? 0 : mName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (! (obj instanceof Party)) {
			return false;
		}
		Party other = (Party)obj;
		if (mName == null) {
			if (other.mName != null) {
				return false;
			}
		} else if (!mName.equals(other.mName)) {
			return false;
		}
		return true;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel p, int flags) {
		p.writeString(mName);
	}

	@Override
	public JsonObject toJson() {
		JsonObject object = new JsonObject();
		object.addProperty(PartyCol.NAME, mName);
		return object;
	}

	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		values.put(PartyCol.NAME, mName);
		return values;
	}
}
