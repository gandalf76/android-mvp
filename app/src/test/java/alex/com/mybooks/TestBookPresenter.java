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
import alex.com.mybooks.presenter.BookContract;
import alex.com.mybooks.presenter.BookPresenter;
import alex.com.mybooks.presenter.BooksPresenter;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, Uri.class, Uri.Builder.class})
public class TestBookPresenter {

    private final String BOOK_ID = "fake_book_id";

    @Mock
    private BookContract.View bookView;

    @Mock
    private BooksHttpInteractor volleyBooksHttpInteractor;

    private BookPresenter bookPresenter;

    private Book testBook;

    private BookApiObject testBookApiObject;

    @Captor
    private ArgumentCaptor<Book> bookArgumentCaptor;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Uri.class);
        PowerMockito.mockStatic(UriBuilder.class);

        this.testBook = new Book();
        testBook.setId("id");
        Book.BookInfo bookInfo = new Book.BookInfo();
        bookInfo.setTitle("title");
        this.testBook.setBookInfo(bookInfo);
        Book.Image image = new Book.Image();
        image.setThumbnail("thumbnail");
        testBook.getBookInfo().setImage(image);
        testBook.getBookInfo().setDescription("description");
        Price price = new Price();
        price.setAmount(10f);
        price.setCurrencyCode("EUR");
        Book.SaleInfo saleInfo = new Book.SaleInfo();
        saleInfo.setListPrice(price);
        testBook.setSaleInfo(saleInfo);
        testBook.getSaleInfo().setListPrice(price);
        this.bookPresenter = new BookPresenter(this.bookView, this.volleyBooksHttpInteractor);
        this.bookPresenter.start();
    }

    @Test
    public void testLoadBook() {
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                bookPresenter.onBookLoaded(testBook);
                return null;
            }
        }).when(volleyBooksHttpInteractor).getBook(Mockito.anyString());

        this.bookPresenter.loadBook(BOOK_ID);

        Mockito.verify(bookView).showBook(bookArgumentCaptor.capture());
        Book book = bookArgumentCaptor.getValue();
        Assert.assertNotNull("Test book not null.", book);
        Assert.assertEquals("Test book equality.", this.testBook, book);
    }

   @After
   public void tearDown() {
       this.bookPresenter.stop();
   }

}
