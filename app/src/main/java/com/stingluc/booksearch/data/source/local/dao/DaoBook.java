package com.stingluc.booksearch.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.stingluc.booksearch.data.entities.Book;

import java.util.List;
import io.reactivex.Single;

@Dao
public interface DaoBook {

    @Insert
    long[] insertAll(List<Book> entities);

    @Query("DELETE FROM BOOK")
    void deleteAll();

    @Query("SELECT * FROM Book")
    Single<List<Book>> getAllBooks();

}
