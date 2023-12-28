package it.lanos.eventbuddy.data.services;

import static it.lanos.eventbuddy.util.Constants.ACCESS_TOKEN;
import static it.lanos.eventbuddy.util.Constants.QUERY_PARAMETER;
import static it.lanos.eventbuddy.util.Constants.SESSION_TOKEN;
import static it.lanos.eventbuddy.util.Constants.TOKEN_PARAMETER;

import it.lanos.eventbuddy.data.source.models.mapbox.FeatureApiResponse;
import it.lanos.eventbuddy.data.source.models.mapbox.SuggestionsApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MapboxService {
    @GET("search/searchbox/v1/suggest")
    Call<SuggestionsApiResponse> getSuggestions(
            @Query(QUERY_PARAMETER) String query,
            @Query(ACCESS_TOKEN) String accessToken,
            @Query(SESSION_TOKEN) String sessionToken
    );
    @GET("search/searchbox/v1/retrieve/{id}")
    Call<FeatureApiResponse> getFeature(
            @Path(TOKEN_PARAMETER) String id,
            @Query(ACCESS_TOKEN) String accessToken,
            @Query(SESSION_TOKEN) String sessionToken
    );

}
