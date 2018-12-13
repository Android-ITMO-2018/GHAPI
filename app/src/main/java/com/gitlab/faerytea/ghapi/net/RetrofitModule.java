package com.gitlab.faerytea.ghapi.net;

import android.util.Log;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Call;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
public class RetrofitModule {
    private static final String LOG_TAG = RetrofitModule.class.getSimpleName();

    @Provides
    Call.Factory callFactory() {
        Log.d(LOG_TAG, "callFactory() called");
        return new OkHttpClient();
    }

    @Provides
    Converter.Factory converterFactory() {
        Log.d(LOG_TAG, "converterFactory() called");
        return MoshiConverterFactory.create();
    }

    @Provides
    Retrofit retrofit(Call.Factory callFactory, Converter.Factory mapper) {
        Log.d(LOG_TAG, "retrofit() called with: callFactory = [" + callFactory + "], mapper = [" + mapper + "]");
        return new Retrofit.Builder()
                .callFactory(callFactory)
                .addConverterFactory(mapper)
                .baseUrl("https://api.github.com/")
                .build();
    }
}
