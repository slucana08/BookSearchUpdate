package com.stingluc.booksearch.injection.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stingluc.booksearch.data.PreferenceManager;
import com.stingluc.booksearch.data.source.remote.WebService;
import com.stingluc.booksearch.injection.annotations.ApplicationScope;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module (includes = NetworkModule.class)
public class WebServiceModule {

    @Provides
    @ApplicationScope
    public WebService webService(Retrofit retrofit){
        return retrofit.create(WebService.class);
    }

    @Provides
    @ApplicationScope
    public Gson gson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.create();
    }

    @Provides
    @ApplicationScope
    public Retrofit retrofit(OkHttpClient client, Gson gson, PreferenceManager preferenceManager){
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(preferenceManager.getBaseUrl())
                .client(client)
                .build();

    }
}
