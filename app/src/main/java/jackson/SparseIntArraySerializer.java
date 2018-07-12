package jackson;

import android.util.SparseIntArray;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 *
 * @author Vinay
 * <br/>
 * <br/>
 * Serializer written to convert SparseIntArray to Key-Value JSON String; which is not supported by Jackson as of now
 *
 */
public class SparseIntArraySerializer extends JsonSerializer<SparseIntArray> {

	@Override
	public void serialize(SparseIntArray sparseIntArray, JsonGenerator jsonGenerator, SerializerProvider sp) throws IOException, JsonProcessingException {

		if (sparseIntArray != null) {
			jsonGenerator.writeStartObject();
			//dump all data
			for(int i=0; i<sparseIntArray.size(); i++){
				
				int key=sparseIntArray.keyAt(i);
				jsonGenerator.writeNumberField(String.valueOf(key) , sparseIntArray.get(key));
				
			}

			jsonGenerator.writeEndObject();

		}

	}

}
