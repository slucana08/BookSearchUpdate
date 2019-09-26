package com.stingluc.booksearch.injection.modules;

import android.content.Context;

import com.stingluc.booksearch.data.source.local.DBBookSearch;
import com.stingluc.booksearch.injection.annotations.ApplicationScope;

import dagger.Module;
import dagger.Provides;

@Module
public class SQLiteModule {

    @Provides
    @ApplicationScope
    public DBBookSearch dbBookSearch(Context context){
        return DBBookSearch.getDatabase(context);
    }
}
