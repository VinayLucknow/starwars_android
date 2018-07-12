package jackson;

import android.util.SparseIntArray;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;


/**
 *
 * @author Vinay
 * <br/>
 * <br/>
 * Deserializer written to convert JSON string of Key-Value map to SparseIntArray type which is not supported by Jackson as of now
 *
 */
public class SparseIntArrayDeserializer extends JsonDeserializer<SparseIntArray> {

	@Override
	public SparseIntArray deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {

		SparseIntArray sparseIntArray = new SparseIntArray(20);

		JsonToken currentToken = null;

		while ((currentToken = jp.nextValue()) != null) {
			if (currentToken == JsonToken.VALUE_NUMBER_INT) {
				sparseIntArray.put(Integer.parseInt( jp.getCurrentName() ), jp.getIntValue());
			}
		}

		return sparseIntArray;
	}

}
