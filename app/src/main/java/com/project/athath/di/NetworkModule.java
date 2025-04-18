package com.project.athath.di;


import android.content.Context;

import com.google.gson.GsonBuilder;
import com.project.athath.data.network.ApiService;
import com.project.athath.data.network.CookieManager;
import com.project.athath.ui.utils.SharedPrefUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    @Provides
    @Singleton
    public static Retrofit provideRetrofit(GsonConverterFactory gson, OkHttpClient okHttpClient) {
        String BASE_URL = "https://furniture-api-5y74.onrender.com";
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(gson)
                .client(okHttpClient)
                .build();
    }

    @Provides
    public static GsonConverterFactory provideGson() {
        return GsonConverterFactory.create(new GsonBuilder().create());
    }

    @Provides
    public static HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    @Provides
    public static OkHttpClient provideOkHttpClient(@ApplicationContext Context context, HttpLoggingInterceptor loggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new CookieManager(context))
                .build();
    }

    @Provides
    @Singleton
    public static ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }
}