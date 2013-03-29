package com.edmondapps.android.sample.model;

import com.edmondapps.android.sample.framework.IParty;

public final class Parties {
	private Parties() {
		throw new AssertionError("nice try");
	}

	public static final class US {
		public static final IParty NO_PARTY = newInstance(null);

		public static final IParty DEMOCRATIC = newInstance("Democratic");
		public static final IParty DEMOCRATIC_REPUBLICAN = newInstance("Democratic-Republican");
		public static final IParty FEDERALIST = newInstance("Federalist");
		public static final IParty INDEPENDENT = newInstance("Independent");
		public static final IParty REPUBLICAN = newInstance("Republican");
		public static final IParty WHIG = newInstance("Whig");
	}

	public static final IParty newInstance(String name) {
		return new Party(name);
	}
}
