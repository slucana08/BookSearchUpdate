package com.stingluc.booksearch.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.stingluc.booksearch.injection.annotations.ApplicationScope;
import com.stingluc.booksearch.utils.Constants;

import javax.inject.Inject;

@ApplicationScope
public class PreferenceManager {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Inject
    public PreferenceManager(Context context){
        prefs = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public boolean getFirstLoad(){
        return prefs.getBoolean(Constants.PREFS_FIRST_LOAD,true);
    }

    public void setFirstLoad(boolean firstLoad){
        editor.putBoolean(Constants.PREFS_FIRST_LOAD,firstLoad);
    }

    public long getTimeOutWS(){
        return 4000L;
    }

    public String getBaseUrl(){
        return "https://www.googleapis.com/books/v1/";
    }

    public boolean getOldInTitle(){
        return prefs.getBoolean(Constants.PREFS_OLD_IN_TITLE,false);
    }

    public void setOldInTitle(boolean oldInTitle){
        editor.putBoolean(Constants.PREFS_OLD_IN_TITLE,oldInTitle).commit();
    }

    public boolean getOldInAuthor(){
        return prefs.getBoolean(Constants.PREFS_OLD_IN_AUTHOR,false);
    }

    public void setOldInAuthor(boolean oldInAuthor){
        editor.putBoolean(Constants.PREFS_OLD_IN_AUTHOR,oldInAuthor).commit();
    }

    public int getOldPrintType(){
        return prefs.getInt(Constants.PREFS_OLD_PRINT_TYPE,2);
    }

    public void setOldPrintType(int oldPrintType){
        editor.putInt(Constants.PREFS_OLD_PRINT_TYPE,oldPrintType).commit();
    }

    public int getOldResultsPage(){
        return prefs.getInt(Constants.PREFS_OLD_RESULTS_PAGE,0);
    }

    public void setOldResultsPage(int oldResultsPage){
        editor.putInt(Constants.PREFS_OLD_RESULTS_PAGE,oldResultsPage).commit();
    }

    public String getOldQueryText(){
        return prefs.getString(Constants.PREFS_OLD_QUERY_TEXT,"");
    }

    public void setOldQueryText(String oldQueryText){
        editor.putString(Constants.PREFS_OLD_QUERY_TEXT,oldQueryText).commit();
    }

    public String getOldAuthorText(){
        return prefs.getString(Constants.PREFS_OLD_AUTHOR_TEXT,"");
    }

    public void setOldAuthorText(String oldAuthorText){
        editor.putString(Constants.PREFS_OLD_AUTHOR_TEXT,oldAuthorText).commit();
    }

    public boolean getInTitle(){
        return prefs.getBoolean(Constants.PREFS_IN_TITLE,false);
    }

    public void setInTitle(boolean inTitle){
        editor.putBoolean(Constants.PREFS_IN_TITLE,inTitle).commit();
    }

    public boolean getInAuthor(){
        return prefs.getBoolean(Constants.PREFS_IN_AUTHOR,false);
    }

    public void setInAuthor(boolean inAuthor){
        editor.putBoolean(Constants.PREFS_IN_AUTHOR,inAuthor).commit();
    }

    public int getPrintType(){
        return prefs.getInt(Constants.PREFS_PRINT_TYPE,2);
    }

    public void setPrintType(int printType){
        editor.putInt(Constants.PREFS_PRINT_TYPE,printType).commit();
    }

    public int getResultsPage(){
        return prefs.getInt(Constants.PREFS_RESULTS_PAGE,0);
    }

    public void setResultsPage(int resultsPage){
        editor.putInt(Constants.PREFS_RESULTS_PAGE,resultsPage).commit();
    }

    public String getQueryText(){
        return prefs.getString(Constants.PREFS_QUERY_TEXT,"");
    }

    public void setQueryText(String queryText){
        editor.putString(Constants.PREFS_QUERY_TEXT,queryText).commit();
    }

    public String getAuthorText(){
        return prefs.getString(Constants.PREFS_AUTHOR_TEXT,"");
    }

    public void setAuthorText(String authorText){
        editor.putString(Constants.PREFS_AUTHOR_TEXT,authorText).commit();
    }

    public int getPageIndex(){
        return prefs.getInt(Constants.PREFS_PAGE_INDEX,1);
    }

    public void setPageIndex(int pageIndex){
        editor.putInt(Constants.PREFS_PAGE_INDEX,pageIndex).commit();
    }
}
