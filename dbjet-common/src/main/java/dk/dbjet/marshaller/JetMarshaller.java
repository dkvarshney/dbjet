package dk.dbjet.marshaller;

import dk.dbjet.annotation.JetColumn;

public interface JetMarshaller<T> {

	/**
	 * Marshal an object before inserting it to database.
	 * 
	 * For Example: A data value can be encrypted using this method before inserting it to database.
	 * 
	 * Note: Currently Supported for TEXT and BINARY types.
	 * 
	 * @return
	 */
	public T marshal(JetColumn column, T value); 
	
	/**
	 * Un-marshal an object after fetching from database.
	 * 
	 * For Example: A data value can be decrypted using this method after fetching it from database.
	 * 
	 * Note: Currently Supported for TEXT and BINARY types.
	 * 
	 * @return
	 */
	public T unmarshal(JetColumn column, T value);
}
