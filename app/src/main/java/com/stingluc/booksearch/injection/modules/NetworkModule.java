package com.stingluc.booksearch.injection.modules;

import android.content.Context;

import com.stingluc.booksearch.data.PreferenceManager;
import com.stingluc.booksearch.injection.annotations.ApplicationScope;

import java.io.File;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

@Module
public class NetworkModule {

    @Provides
    @ApplicationScope
    public File cachefile(Context context){
        return new File(context.getCacheDir(),"okhttp-cache");
    }

    @Provides
    @ApplicationScope
    public Cache cache(File cachefile){
        return new Cache(cachefile,10 * 1000 * 1000);
    }

    @Provides
    @ApplicationScope
    public HttpLoggingInterceptor httpLoggingInterceptor(){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(message -> Timber.i(message));
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    @Provides
    @ApplicationScope
    public OkHttpClient okHttpClient(HttpLoggingInterceptor httpLoggingInterceptor, Cache cache,
                                     PreferenceManager preferenceManager){
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .connectionPool(new ConnectionPool(0,1,TimeUnit.NANOSECONDS))
                .readTimeout(preferenceManager.getTimeOutWS(), TimeUnit.MILLISECONDS)
                .connectTimeout(preferenceManager.getTimeOutWS(),TimeUnit.MILLISECONDS)
                .cache(cache)
                .build();
    }


}
