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
