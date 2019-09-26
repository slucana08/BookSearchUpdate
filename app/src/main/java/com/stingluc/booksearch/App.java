package com.stingluc.booksearch;

import android.app.Application;

import com.stingluc.booksearch.injection.AppComponent;
import com.stingluc.booksearch.injection.DaggerAppComponent;
import com.stingluc.booksearch.injection.modules.AppModule;

import timber.log.Timber;

public class App extends Application {

    protected static volatile App INSTANCE;

    public AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;

        if (BuildConfig.DEBUG){
            Timber.plant(new Timber.DebugTree());
        }

        buildDependencyInjection();
    }

    public static App getInstance() {
        return INSTANCE;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public void buildDependencyInjection(){
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(INSTANCE))
                .build();
    }
}
