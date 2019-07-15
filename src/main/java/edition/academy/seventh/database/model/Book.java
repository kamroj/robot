package edition.academy.seventh.database.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author Kamil Rojek
 * @author Bartosz Kupajski
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String subtitle;
  private String authors;
  private String price;
  private String promotion;

  public Book(String title, String authors, String price, String promotion) {
    this.title = title;
    this.authors = authors;
    this.price = price;
    this.promotion = promotion;
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

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  @Override
  public String toString() {
    return "Book{"
        + "title='"
        + title
        + '\''
        + ", subtitle='"
        + subtitle
        + '\''
        + ", authors='"
        + authors
        + '\''
        + ", price='"
        + price
        + '\''
        + '}';
  }
}
