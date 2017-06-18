package alex.com.mybooks.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Book {

    private String id;

    @JsonProperty("volumeInfo")
    private BookInfo bookInfo;

    @JsonProperty("saleInfo")
    private SaleInfo saleInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }

    public SaleInfo getSaleInfo() {
        return saleInfo;
    }

    public void setSaleInfo(SaleInfo saleInfo) {
        this.saleInfo = saleInfo;
    }

    public static class BookInfo {

        private String title;

        @JsonProperty("imageLinks")
        private Image image;

        private List<String> authors;

        private String description;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Image getImage() {
            return this.image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        public List<String> getAuthors() {
            return authors;
        }

        public void setAuthors(List<String> authors) {
            this.authors = authors;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }

    public static class SaleInfo {

        private Price listPrice;

        public Price getListPrice() {
            return listPrice;
        }

        public void setListPrice(Price listPrice) {
            this.listPrice = listPrice;
        }
    }

    public static class Image {

        private String thumbnail;

        private String medium;

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }
    }

}
