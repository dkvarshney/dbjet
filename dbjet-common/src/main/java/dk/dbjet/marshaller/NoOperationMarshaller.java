package dk.dbjet.marshaller;

import dk.dbjet.annotation.JetColumn;

public class NoOperationMarshaller implements JetMarshaller<Object> {

	@Override
	public Object marshal(JetColumn column, Object value) {
		return value;
	}
	
	@Override
	public Object unmarshal(JetColumn column, Object value) {
		return value;
	}
}
