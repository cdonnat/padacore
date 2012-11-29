package org.padacore.core.gnat;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;

public class Symbol {

	private static final int STRING = 1;
	private static final int STRING_LIST = 2;

	private ArrayList<String> value;
	private int type;

	public static Symbol CreateString(String value) {
		return new Symbol(value);
	}

	public static Symbol CreateStringList(List<String> value) {
		return new Symbol(value);
	}

	private Symbol(String value) {
		this.value = new ArrayList<String>(1);
		this.value.add(RemoveQuotes(value));
		this.type = STRING;
	}

	private Symbol(List<String> value) {
		this.value = new ArrayList<String>(RemoveQuotes(value));
		this.type = STRING_LIST;
	}

	public boolean isAString() {
		return type == STRING;
	}

	public String getAsString() {
		Assert.isLegal(isAString());
		return value.get(0);
	}

	public List<String> getAsStringList() {
		return value;
	}

	public static Symbol Concat(Symbol left, Symbol right) {
		Assert.isLegal((left.isAString() && right.isAString() || (!left.isAString())));

		if (left.isAString()) {
			return CreateString(left.getAsString() + right.getAsString());
		} else {
			List<String> newList = new ArrayList<String>(left.getAsStringList());
			if (right.isAString()) {
				newList.add(right.getAsString());
			} else {
				newList.addAll(right.getAsStringList());
			}
			return CreateStringList(newList);
		}
	}

	private static String RemoveQuotes(String input) {
		String res = input;
		if (input.startsWith("\"") && input.endsWith("\"")) {
			res = input.substring(1, input.length() - 1);
		}
		return res;
	}

	private static List<String> RemoveQuotes(List<String> input) {
		ArrayList<String> res = new ArrayList<String>(input.size());
		for (String element : input) {
			res.add(RemoveQuotes(element));
		}
		return res;
	}
}
