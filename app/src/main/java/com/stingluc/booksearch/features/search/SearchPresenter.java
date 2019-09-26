package com.stingluc.booksearch.features.search;

import android.content.Context;
import android.text.TextUtils;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.stingluc.booksearch.R;
import com.stingluc.booksearch.data.PreferenceManager;
import com.stingluc.booksearch.data.entities.Book;
import com.stingluc.booksearch.data.pojos.ImageLinks;
import com.stingluc.booksearch.data.pojos.Items;
import com.stingluc.booksearch.data.pojos.VolumeInfo;
import com.stingluc.booksearch.data.source.DataSourceRepository;
import com.stingluc.booksearch.utils.UtilMethods;
import com.stingluc.booksearch.viewmodel.DataViewModel;
import com.stingluc.booksearch.viewmodel.StatusViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class SearchPresenter implements SearchContract.Presenter {

    private SearchContract.View view;
    private PreferenceManager preferenceManager;
    private DataSourceRepository dataSourceRepository;
    private Context context;

    private String queryToSearch = "";
    private int pageIndex, presenterID;
    private Map<String, String> searchParams = new HashMap<>();

    private LiveData<List<Book>> booksBD;

    private DataViewModel dataViewModel;
    private StatusViewModel statusViewModel;

    @Inject
    public SearchPresenter(Context context, PreferenceManager preferenceManager, DataSourceRepository dataSourceRepository){
        this.context = context;
        this.preferenceManager = preferenceManager;
        this.dataSourceRepository = dataSourceRepository;
    }

    @Override
    public int verifyQueries(String queryText, String authorText, boolean inTitle, boolean inAuthor) {
        queryToSearch = "";
        if (TextUtils.isEmpty(queryText)){
            return 0;
        } else {
            if (inTitle && inAuthor){
                if (TextUtils.isEmpty(authorText)) return 1;
                else queryToSearch = "intitle:" + queryText + "+" + "inauthor:" + authorText;
            } else if (inTitle){
                queryToSearch = "intitle:" + queryText;
            } else if (inAuthor){
                queryToSearch = "inauthor:" + queryText;
            } else {
                queryToSearch += queryText;
            }
            return 2;
        }
    }

    @Override
    public boolean buildParams(int type) {
        String printType = "all";
        int maxResults = 10;
        int startIndex = 0;
        pageIndex = preferenceManager.getPageIndex();

        switch (preferenceManager.getPrintType()){
            case 0:
                printType = "books";
                break;
            case 1:
                printType = "magazines";
                break;
            case 2:
                printType = "all";
                break;
        }

        switch (preferenceManager.getResultsPage()){
            case 0:
                maxResults = 10;
                break;
            case 1:
                maxResults = 20;
                break;
            case 2:
                maxResults = 40;
                break;
        }

        switch (type){
            case 1:
                pageIndex += 1;
                break;
            case 2:
                if (pageIndex == 1) {
                    UtilMethods.showToast(context, "First page in records");
                    return false;
                } else
                    pageIndex -= 1;
                break;
        }

        if (pageIndex != 1) startIndex = (pageIndex - 1) * maxResults;
        statusViewModel.setPageIndex(pageIndex);

        searchParams.put("q", queryToSearch);
        searchParams.put("printType", printType);
        searchParams.put("maxResults",String.valueOf(maxResults));
        searchParams.put("startIndex",String.valueOf(startIndex));
        return true;
    }

    @Override
    public void setDataViewModel(DataViewModel dataViewModel) {
        this.dataViewModel = dataViewModel;
    }

    @Override
    public void setStatusViewModel(StatusViewModel statusViewModel) {
        this.statusViewModel = statusViewModel;
        pageIndex = statusViewModel.getPageIndex();
        statusViewModel.setPresenterID(statusViewModel.getPresenterID() + 1);
        presenterID = statusViewModel.getPresenterID();
    }

    @Override
    public void setStatus(int status) {
        statusViewModel.setStatus(status);
    }

    @Override
    public int getStatus() {
        return statusViewModel.getStatus();
    }

    @Override
    public void setSearching(boolean searching) {
        statusViewModel.setSearching(searching);
    }

    @Override
    public boolean isSearching() {
        return statusViewModel.isSearching();
    }

    @Override
    public void setBackOnline(boolean backOnline) {
        statusViewModel.setBackOnline(backOnline);
    }

    @Override
    public boolean isBackOnline() {
        return statusViewModel.isBackOnline();
    }

    @Override
    public void setSettingsShowing(boolean settingsShowing) {
        statusViewModel.setSettingsShowing(settingsShowing);
    }

    @Override
    public boolean isSettingsShowing() {
        return statusViewModel.isSettingsShowing();
    }

    @Override
    public void setType(int type) {
        statusViewModel.setType(type);
    }

    @Override
    public int getType() {
        return statusViewModel.getType();
    }

    @Override
    public int getID() {
        return presenterID;
    }

    @Override
    public int getStatusPresenterID() {
        return statusViewModel.getPresenterID();
    }

    @Override
    public void setBooks() {
        dataViewModel.clearData();
        dataViewModel.setBooks(searchParams);
    }

    @Override
    public void getBooks() {
        dataViewModel.getBooks().observe((LifecycleOwner) getView(), response -> {
            List<Book> books = new ArrayList<>();
            if (response == null) {
                getView().showError();
                return;
            }
            if (response.getTotalItems() >= 0) {
                List<Items> items = response.getItems();
                if (items != null) {
                    for (Items currentItem : items) {
                        VolumeInfo volumeInfo = currentItem.getVolumeInfo();
                        Book book = new Book();

                        book.setTitle(volumeInfo.getTitle());

                        List<String> authors = volumeInfo.getAuthors();
                        String author = context.getString(R.string.not_available);
                        if (authors != null) {
                            author = "";
                            for (int i = 0; i < authors.size(); i++) {
                                if (i == authors.size() - 1) author += authors.get(i);
                                else author += authors.get(i) + ", ";
                            }
                        }
                        book.setAuthor(author);

                        if (volumeInfo.getPublisher() == null && volumeInfo.getPublishedDate() == null) {
                            book.setPublished(context.getString(R.string.not_available));
                        } else if (volumeInfo.getPublisher() == null) {
                            book.setPublished(volumeInfo.getPublishedDate());
                        } else if (volumeInfo.getPublishedDate() == null) {
                            book.setPublished(volumeInfo.getPublisher());
                        } else {
                            book.setPublished(volumeInfo.getPublisher() + " - " + volumeInfo.getPublishedDate());
                        }

                        List<String> categories = volumeInfo.getCategories();
                        String category = context.getString(R.string.not_available);
                        if (categories != null) {
                            category = "";
                            for (int i = 0; i < categories.size(); i++) {
                                if (i == categories.size() - 1)
                                    category += categories.get(i);
                                else category += categories.get(i) + ", ";
                            }
                        }
                        book.setCategory(category);

                        book.setLanguage(volumeInfo.getLanguage());

                        ImageLinks imageLinks = volumeInfo.getImageLinks();
                        book.setImageURL(imageLinks != null ? imageLinks.getThumbnail() : "");

                        books.add(book);
                    }
                }
                preferenceManager.setPageIndex(pageIndex);
                preferenceManager.setFirstLoad(false);
                dataSourceRepository.insertAll(books);
                SearchPresenter.this.getView().showBooks(books, pageIndex);
            }
        });
    }

    @Override
    public void getBooksBD() {
        dataViewModel.setStoredBooks();
    }

    @Override
    public void observeBD() {
        if (statusViewModel.getStatus() == 0) {
            booksBD = dataViewModel.getStoredBooks();
            booksBD.observe((LifecycleOwner) getView(), books -> {
                pageIndex = preferenceManager.getPageIndex();
                preferenceManager.setPageIndex(pageIndex);
                preferenceManager.setFirstLoad(false);
                SearchPresenter.this.getView().showBooks(books, pageIndex);
            });
        }
    }

    @Override
    public void stopObservingBD() {
        if (!preferenceManager.getFirstLoad() && statusViewModel.getStatus() == 0)
            booksBD.removeObservers((LifecycleOwner) getView());
    }

    @Override
    public void setUpViews() {
        getView().setColorStateList();
        getView().setUpActionBar();
        if (preferenceManager.getFirstLoad()) {
            getView().setUpViews(false, false, "", "",
                    2, 0,true);
        } else {
            getView().setUpViews(preferenceManager.getInTitle(), preferenceManager.getInAuthor(),
                    preferenceManager.getQueryText(), preferenceManager.getAuthorText(),
                    preferenceManager.getPrintType(), preferenceManager.getResultsPage(),
                    preferenceManager.getFirstLoad());
        }
    }

    @Override
    public void setInTitle(boolean inTitle) {
        preferenceManager.setOldInTitle(preferenceManager.getInTitle());
        preferenceManager.setInTitle(inTitle);
    }

    @Override
    public void setInAuthor(boolean inAuthor) {
        preferenceManager.setOldInAuthor(preferenceManager.getInAuthor());
        preferenceManager.setInAuthor(inAuthor);
    }

    @Override
    public void setPrintType(int printType) {
        preferenceManager.setOldPrintType(preferenceManager.getPrintType());
        preferenceManager.setPrintType(printType);
    }

    @Override
    public void setResultsPage(int resultsPage) {
        preferenceManager.setOldResultsPage(preferenceManager.getResultsPage());
        preferenceManager.setResultsPage(resultsPage);
    }

    @Override
    public void setQueryText(String queryText) {
        preferenceManager.setOldQueryText(preferenceManager.getQueryText());
        preferenceManager.setQueryText(queryText);
    }

    @Override
    public void setAuthorText(String authorText) {
        preferenceManager.setOldAuthorText(preferenceManager.getAuthorText());
        preferenceManager.setAuthorText(authorText);
    }

    /**
     * @param type 0 - normal search
     *             1 - next page
     *             2 - previous page
     *             3 - app reload
     */
    @Override
    public void setSearchType(int type) {
        switch (type){
            case 0:
                if (!verifyParams()) {
                    preferenceManager.setPageIndex(1);
                    getView().performSearch(0);
                }
                break;
            case 1:
            case 2:
                if (!verifyParams()) {
                    UtilMethods.showToast(context,"Search parameters have changed, performing new search");
                    preferenceManager.setPageIndex(1);
                    getView().performSearch(0);
                } else getView().performSearch(type);
                break;
            case 3:
                getView().performSearch(0);
                break;
        }
    }

    @Override
    public boolean verifyParams() {
        if (preferenceManager.getInTitle() != preferenceManager.getOldInTitle()) return false;
        if (preferenceManager.getInAuthor() != preferenceManager.getOldInAuthor()) return false;
        if (preferenceManager.getPrintType() != preferenceManager.getOldPrintType()) return false;
        if (preferenceManager.getResultsPage() != preferenceManager.getOldResultsPage()) return false;
        if (!TextUtils.equals(preferenceManager.getQueryText(),preferenceManager.getOldQueryText())) return false;
        if (!TextUtils.equals(preferenceManager.getAuthorText(),preferenceManager.getOldAuthorText())) return false;
        return true;
    }

    @Override
    public void attachView(SearchContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public SearchContract.View getView() {
        return view;
    }
}
