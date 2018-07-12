package util;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.util.SparseIntArray;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.util.TimeZone;

import jackson.ObinoJsonDateFormatterWithZone;
import jackson.SparseIntArrayDeserializer;
import jackson.SparseIntArraySerializer;


public class StarWarConstants {
    
    
    
    public static ObjectMapper jsonObjMapper = new ObjectMapper();
    static {
        jsonObjMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        
        jsonObjMapper.setDateFormat(new ObinoJsonDateFormatterWithZone());
        jsonObjMapper.setTimeZone(TimeZone.getDefault());
        
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(SparseIntArray.class, new SparseIntArraySerializer());
        simpleModule.addDeserializer(SparseIntArray.class, new SparseIntArrayDeserializer());
        jsonObjMapper.registerModule(simpleModule);
        
    }

    public interface FragmentsName{

        String CHARACTER_LIST_FRAGMENT="CharactersFragment";
        String CHARACTER_DETAIL_FRAGMENT="CharacterDetailFragment";
    }


}
