package dk.dbjet.marshaller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

import dk.dbjet.annotation.JetColumn;

public class JetBase64Marshaller implements JetMarshaller <String> {
	
	@Override
	public String marshal(JetColumn column, String value) {
		if (Objects.isNull(value)) {
			return value;
		}
		return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
	}
	
	@Override
	public String unmarshal(JetColumn column, String value) {
		if (Objects.isNull(value)) {
			return value;
		}	
		return new String(Base64.getDecoder().decode(value.getBytes(StandardCharsets.UTF_8)));
	}
}