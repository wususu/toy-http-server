package http.util;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "serial", "rawtypes" })
public final class ParameterMap extends HashMap {

	private boolean locked = true;
		
	public ParameterMap() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public ParameterMap(int initialCapacity){
		super(initialCapacity);
	}
	
	public ParameterMap(int initialCapacity, float loadFactor){
		super(initialCapacity, loadFactor);
	}
	
	public ParameterMap(Map map){
		super(map);
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	public void clear(){
		if (locked) 
			throw new IllegalStateException();
		super.clear();
	}
	
	public Object put(Object key, Object value){
		if (locked) 
			throw new IllegalStateException();
		return super.put(key, value);
	}
	
	public void putAll(Map map){
		if (locked) 
			throw new IllegalStateException();
		super.putAll(map);
	}
	
	public Object remove(Object key){
		if (locked) 
			throw new IllegalStateException();
		return super.remove(key);
	}
}
