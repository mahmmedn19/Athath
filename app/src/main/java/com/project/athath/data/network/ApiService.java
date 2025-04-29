package com.project.athath.data.network;


import com.project.athath.data.model.AiRecommendationResponse;
import com.project.athath.data.model.NextRecommendationResponse;
import com.project.athath.data.model.ResponseModel;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart// ok
    @POST("/objectdetection")
    Call<ResponseModel> uploadImage(@Part MultipartBody.Part file);

    // ✅ Endpoint for getting AI recommendations
    @POST("/recommend")
    Call<AiRecommendationResponse> getRecommendations(@Body Map<String, String> userPreferences);

    // ✅ Endpoint for cycling to the next recommendation
    @POST("/next_recommendation")
    Call<NextRecommendationResponse> getNextRecommendation();
}