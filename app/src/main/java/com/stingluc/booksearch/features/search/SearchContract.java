package com.stingluc.booksearch.features.search;

import com.stingluc.booksearch.data.entities.Book;
import com.stingluc.booksearch.features.shared.BaseContractView;
import com.stingluc.booksearch.features.shared.BasePresenter;
import com.stingluc.booksearch.viewmodel.DataViewModel;
import com.stingluc.booksearch.viewmodel.StatusViewModel;

import java.util.List;

interface SearchContract {

    interface View extends BaseContractView{

        void setColorStateList();

        void setUpActionBar();

        void setUpViews(boolean inTitle, boolean inAuthor, String queryText, String authorText,
                        int printType, int resultsPage, boolean firstLoad);

        void shouldShowAuthorText();

        void showBooks(List<Book> books, int pageIndex);

        void showError();

        void checkQueries(int type);

        void performSearch(int type);
    }

    interface Presenter extends BasePresenter<SearchContract.View>{

        void setBooks();

        void getBooks();

        void getBooksBD();

        void observeBD();

        void stopObservingBD();

        void setUpViews();

        void setInTitle(boolean inTitle);

        void setInAuthor(boolean inAuthor);

        void setPrintType(int printType);

        void setResultsPage(int resultsPage);

        void setQueryText(String queryText);

        void setAuthorText(String authorText);

        void setSearchType(int type);

        boolean verifyParams();

        int verifyQueries(String queryText, String authorText, boolean inTitle, boolean inAuthor);

        boolean buildParams(int type);

        void setDataViewModel(DataViewModel dataViewModel);

        void setStatusViewModel(StatusViewModel statusViewModel);

        void setStatus(int status);

        int getStatus();

        void setSearching(boolean isSearching);

        boolean isSearching();

        void setBackOnline(boolean backOnline);

        boolean isBackOnline();

        void setSettingsShowing(boolean settingsShowing);

        boolean isSettingsShowing();

        void setType(int type);

        int getType();

        int getID();

        int getStatusPresenterID();
    }
}
