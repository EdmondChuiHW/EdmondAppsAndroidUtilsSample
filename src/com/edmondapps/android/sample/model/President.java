package com.edmondapps.android.sample.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import com.edmondapps.android.sample.column.PresidentCol;
import com.edmondapps.android.sample.framework.IPresident;
import com.edmondapps.utils.android.database.DatabaseEntry;
import com.edmondapps.utils.android.database.DatabaseTable;
import com.edmondapps.utils.android.json.JsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

public class President implements IPresident, Parcelable, DatabaseEntry {
	public static final Creator<President> CREATOR = new Creator<President>() {
		@Override
		public President createFromParcel(Parcel source) {
			return new President(source);
		}

		@Override
		public President[] newArray(int size) {
			return new President[size];
		}
	};

	public static final class Builder {
		private long mId;
		private String mName;
		private Party mParty;
		private int mBirthyear;

		public Builder setId(long id) {
			mId = id;
			return this;
		}

		public Builder setName(String name) {
			mName = name;
			return this;
		}

		public Builder setParty(Party party) {
			mParty = party;
			return this;
		}

		public Builder setBirthyear(int birthyear) {
			mBirthyear = birthyear;
			return this;
		}

		public President build() {
			return new President(this);
		}

		public static final JsonBuilder<President> JSON_BUILDER = new JsonBuilder<President>() {
			@Override
			public President build(long id, JsonReader reader) throws IOException {
				return fromJson(id, reader);
			}
		};

		public static final President fromJson(long id, JsonReader r) throws IOException {
			Builder b = new Builder();
			b.setId(id);

			r.beginObject();
			while (r.hasNext()) {
				String nextName = r.nextName();
				if (PresidentCol.NAME.equals(nextName)) {
					b.setName(r.nextString());
				} else if (PresidentCol.BIRTHYEAR.equals(nextName)) {
					b.setBirthyear(r.nextInt());
				} else if (PresidentCol.PARTY.equals(nextName)) {
					b.setParty(Party.Builder.fromJson(r));
				} else {
					r.skipValue();
				}
			}
			r.endObject();

			return b.build();
		}

		public static final President fromCursor(Cursor c) {
			Builder b = new Builder();
			b.setBirthyear(c.getInt(c.getColumnIndex(PresidentCol.BIRTHYEAR)));
			b.setId(c.getLong(c.getColumnIndex(BaseColumns._ID)));
			b.setName(c.getString(c.getColumnIndex(PresidentCol.NAME)));
			b.setParty(Party.Builder.fromCursor(c));
			return b.build();
		}
	}

	public static President valueOf(IPresident president) {
		if (president instanceof President) {
			return (President)president;
		} else {
			return new President(president);
		}
	}

	public static ArrayList<President> toAndroid(List<? extends IPresident> presidents) {
		ArrayList<President> list = new ArrayList<President>(presidents.size());
		for (IPresident p : presidents) {
			list.add(President.valueOf(p));
		}
		return list;
	}

	private final long mId;
	private final String mName;
	private final Party mParty;
	private final int mBirthyear;

	private President(Parcel p) {
		mName = p.readString();
		mParty = p.readParcelable(Party.class.getClassLoader());
		mBirthyear = p.readInt();
		mId = p.readLong();
	}

	public President(IPresident p) {
		mName = p.getName();
		mBirthyear = p.getBirthyear();
		mParty = Party.valueOf(p.getParty());
		mId = p.getId();
	}

	public President(Builder b) {
		mId = b.mId;
		mName = b.mName;
		mParty = b.mParty;
		mBirthyear = b.mBirthyear;
	}

	@Override
	public String getName() {
		return mName;
	}

	@Override
	public Party getParty() {
		return mParty;
	}

	@Override
	public int getBirthyear() {
		return mBirthyear;
	}

	@Override
	public long getId() {
		return mId;
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append("President [mName=").append(mName)
				.append(", mParty=").append(mParty)
				.append(", mBirthyear=").append(mBirthyear)
				.append("]")
				.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (mBirthyear ^ (mBirthyear >>> 32));
		result = (prime * result) + ( (mName == null) ? 0 : mName.hashCode());
		result = (prime * result) + ( (mParty == null) ? 0 : mParty.hashCode());
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
		if (! (obj instanceof President)) {
			return false;
		}
		President other = (President)obj;
		if (mBirthyear != other.mBirthyear) {
			return false;
		}
		if (mName == null) {
			if (other.mName != null) {
				return false;
			}
		} else if (!mName.equals(other.mName)) {
			return false;
		}
		if (mParty == null) {
			if (other.mParty != null) {
				return false;
			}
		} else if (!mParty.equals(other.mParty)) {
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
		p.writeParcelable(mParty, 0);
		p.writeInt(mBirthyear);
		p.writeLong(mId);
	}

	@Override
	public JsonObject toJson() {
		JsonObject object = new JsonObject();
		object.addProperty(PresidentCol.NAME, mName);
		object.addProperty(PresidentCol.BIRTHYEAR, mBirthyear);
		object.add(PresidentCol.PARTY, mParty.toJson());
		return object;
	}

	@Override
	public ContentValues toContentValues(DatabaseTable db) {
		ContentValues values = new ContentValues();
		values.put(PresidentCol.NAME, mName);
		values.put(PresidentCol.BIRTHYEAR, mBirthyear);
		values.putAll(mParty.toContentValues());
		return values;
	}
}
