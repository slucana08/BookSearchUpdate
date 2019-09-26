package com.stingluc.booksearch.injection;

import android.content.Context;

import com.stingluc.booksearch.data.source.local.DBBookSearch;
import com.stingluc.booksearch.data.source.remote.WebService;
import com.stingluc.booksearch.features.search.SearchActivity;
import com.stingluc.booksearch.injection.annotations.ApplicationScope;
import com.stingluc.booksearch.injection.modules.AppModule;
import com.stingluc.booksearch.injection.modules.SQLiteModule;
import com.stingluc.booksearch.injection.modules.ViewModelModule;
import com.stingluc.booksearch.injection.modules.WebServiceModule;
import com.stingluc.booksearch.viewmodel.ViewModelFactory;

import dagger.Component;

@ApplicationScope
@Component(modules = {AppModule.class, SQLiteModule.class, WebServiceModule.class, ViewModelModule.class})
public interface AppComponent {

    Context context();

    WebService webService();

    DBBookSearch dbBookSearch();

    void inject(SearchActivity activity);

    ViewModelFactory viewModelFactory();

}
