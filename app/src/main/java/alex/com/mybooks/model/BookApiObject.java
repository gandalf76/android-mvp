package alex.com.mybooks.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BookApiObject {

    private int totalItems;

    @JsonProperty("items")
    private List<Book> books;

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
