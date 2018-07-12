package http;

import java.util.List;

import model.CharactersList;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 Created by Yogs on 17/04/17. */

public interface WebApi {

    @GET("people")
    Call<CharactersList> getCharactersList(
            @Header("Content-Type") String ContentType,
            @Query("page") int page
    );

    @GET("people/{id}/")
    Call<CharactersList> getCharacterDetail(
            @Header("Content-Type") String ContentType,
            @Path("id") int id
    );
    
}



