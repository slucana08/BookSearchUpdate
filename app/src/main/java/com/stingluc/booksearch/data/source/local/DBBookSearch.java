package com.stingluc.booksearch.data.source.local;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.stingluc.booksearch.data.entities.Book;
import com.stingluc.booksearch.data.source.local.dao.DaoBook;

@Database(entities = {Book.class},version = 1,exportSchema = false)
public abstract class DBBookSearch extends RoomDatabase {

    public abstract DaoBook daoBook();

    private static volatile DBBookSearch INSTANCE;

    public static DBBookSearch getDatabase(Context context){
        if (INSTANCE == null){
            synchronized (DBBookSearch.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context,DBBookSearch.class,"DBBookSearch")
                            .addCallback(sCallBack)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sCallBack = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

}
