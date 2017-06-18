package alex.com.mybooks;


import android.net.Uri;
import android.util.Log;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import alex.com.mybooks.httpapi.BooksHttpInteractor;
import alex.com.mybooks.httpapi.UriBuilder;
import alex.com.mybooks.model.Book;
import alex.com.mybooks.model.BookApiObject;
import alex.com.mybooks.model.Price;
import alex.com.mybooks.presenter.BooksContract;
import alex.com.mybooks.presenter.BooksPresenter;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, Uri.class, Uri.Builder.class})
public class TestBooksPresenter {

    private final String FILTER_FOR_BOOKS = "thriller";

    private final int START_INDEX = 0;

    private final int MAX_BOOKS_PER_PAGE = 10;

    private final int TOTAL_PAGES_NUMBER = 1;

    @Mock
    private BooksContract.View bookView;

    @Mock
    private BooksHttpInteractor volleyBooksHttpInteractor;

    private BooksPresenter booksPresenter;

    private List<Book> testBooks;

    private Book testBook;

    private BookApiObject testBookApiObject;

    @Captor
    private ArgumentCaptor<List<Book>> bookArgumentCaptor;

    @Captor
    private ArgumentCaptor<Integer> pageCountArgumentCaptor;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Uri.class);
        PowerMockito.mockStatic(UriBuilder.class);

        this.testBook = new Book();
        testBook.setId("id");
        Book.BookInfo bookInfo = new Book.BookInfo();
        bookInfo.setTitle("title");
        testBook.setBookInfo(bookInfo);
        Book.Image image = new Book.Image();
        image.setThumbnail("thumbnail");
        testBook.getBookInfo().setImage(image);
        Price price = new Price();
        price.setAmount(10f);
        price.setCurrencyCode("EUR");
        Book.SaleInfo saleInfo = new Book.SaleInfo();
        saleInfo.setListPrice(price);
        testBook.setSaleInfo(saleInfo);
        this.testBooks = new ArrayList<>();
        this.testBooks.add(testBook);
        this.testBookApiObject = new BookApiObject();
        this.testBookApiObject.setBooks(this.testBooks);
        this.testBookApiObject.setTotalItems(TOTAL_PAGES_NUMBER);
        this.booksPresenter = new BooksPresenter(this.bookView, this.volleyBooksHttpInteractor);
        this.booksPresenter.start();
    }

    @Test
    public void testLoadBooks() {
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                booksPresenter.onBooksLoaded(testBookApiObject, MAX_BOOKS_PER_PAGE);
                return null;
            }
        }).when(volleyBooksHttpInteractor).getBooks(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt());

        this.booksPresenter.loadBooks(FILTER_FOR_BOOKS, START_INDEX, MAX_BOOKS_PER_PAGE);

        Mockito.verify(bookView).showBooks(bookArgumentCaptor.capture(), pageCountArgumentCaptor.capture());
        List<Book> books = bookArgumentCaptor.getValue();
        int totalPagesNumber = pageCountArgumentCaptor.getValue();
        Assert.assertNotNull("Test books not null.", books);
        Assert.assertTrue("Test books size.", books.size() == 1);
        Assert.assertEquals("Test book equality.", this.testBook, books.get(0));
        Assert.assertTrue("Test pages number size.", totalPagesNumber == TOTAL_PAGES_NUMBER);
   }

   @After
   public void tearDown() {
       this.booksPresenter.stop();
   }

}
