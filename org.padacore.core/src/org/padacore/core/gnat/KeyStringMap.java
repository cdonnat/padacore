package org.padacore.core.gnat;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements a map <String, T>. The string used as a key is not case sensitive.
 *  
 * @param <T> Type of map value.
 */
public class KeyStringMap<T> {

	private Map<String, T> map;

	/**
	 * Default constructor.
	 */
	public KeyStringMap() {
		this.map = new HashMap<String, T>();
	}

	/**
	 * Returns true if this map contains a mapping for the specified key. False otherwise.
	 * 
	 * @param key Key whose presence in this map is to be tested
	 * @return True if this map contains a mapping for the specified key.
	 */
	public boolean contains(String key) {
		return this.map.containsKey(key.toLowerCase());
	}

	/**
	 * Returns the value to which the specified key is mapped
	 * @param key  Key whose associated value is to be returned
	 * @returnVvalue to which the specified key is mapped
	 */
	public T get(String key) {
		return this.map.get(key.toLowerCase());
	}

	/**
	 * Associates the specified value with the specified key in this map
	 * @param key Key with which the specified value is to be associated
	 * @param value Value to be associated with the specified key
	 */
	public void put(String key, T value) {
		this.map.put(key.toLowerCase(), value);
	}

	/**
	 * Returns a Collection view of the values contained in this map.
	 * @return A collection view of the values contained in this map
	 */
	public Collection<T> values() {
		return this.map.values();
	}
}
