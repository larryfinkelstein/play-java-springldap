/**
 * 
 */
package models;

import java.io.IOException;

import javax.naming.Name;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author larryf
 *
 */
public class JsonNameSerializer extends JsonSerializer<Name> {

	@Override
	public void serialize(Name name, JsonGenerator generator, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		generator.writeString(name.toString());
	}

}
