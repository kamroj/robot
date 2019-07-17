package edition.academy.seventh.database.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Kamil Rojek
 * @author Bartosz Kupajski
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DTBook {

  private String title;
  private String subtitle;
  private String authors;
  private String bookstore;
  private String price;
  private String promoPrice;
  private String href;
  private String image;

  public DTBook(
      String title,
      String subtitle,
      String authors,
      String bookstore,
      String price,
      String promoPrice,
      String href,
      String image) {
    this.title = title;
    this.subtitle = subtitle;
    this.authors = authors;
    this.bookstore = bookstore;
    this.price = price;
    this.promoPrice = promoPrice;
    this.href = href;
    this.image = image;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSubtitle() {
    return subtitle;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }

  public String getAuthors() {
    return authors;
  }

  public void setAuthors(String authors) {
    this.authors = authors;
  }

  public String getBookstore() {
    return bookstore;
  }

  public void setBookstore(String bookstore) {
    this.bookstore = bookstore;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getPromoPrice() {
    return promoPrice;
  }

  public void setPromoPrice(String promoPrice) {
    this.promoPrice = promoPrice;
  }

  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  @Override
  public String toString() {
    return "DTBook{" +
            "title='" + title + '\'' +
            ", subtitle='" + subtitle + '\'' +
            ", authors='" + authors + '\'' +
            ", bookstore='" + bookstore + '\'' +
            ", price='" + price + '\'' +
            ", promoPrice='" + promoPrice + '\'' +
            ", href='" + href + '\'' +
            ", image='" + image + '\'' +
            '}';
  }
}
