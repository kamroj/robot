package edition.academy.seventh.repository;

import edition.academy.seventh.database.connector.ConnectorFactory;
import edition.academy.seventh.database.connector.ConnectorProvider;
import edition.academy.seventh.database.model.DTBook;
import edition.academy.seventh.model.Book;
import edition.academy.seventh.model.Bookstore;
import edition.academy.seventh.model.BookstoreBook;
import edition.academy.seventh.model.HrefAndImage;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import static edition.academy.seventh.database.connector.ConnectorFactory.DatabaseTypes.H2;
import static edition.academy.seventh.repository.BookParser.parseDTBookIntoModel;
import static edition.academy.seventh.repository.BookParser.parseBookstoreBookListIntoDTBookList;

/**
 * Repository that persists book entities in database.
 *
 * @author Agnieszka Trzewik
 */
@Repository
public class BookRepository {
  private EntityManager entityManager;
  private ConnectorProvider connectorProvider;

  public BookRepository() {
    connectorProvider = ConnectorFactory.of(H2);
  }

  /**
   * Adds books records to the database.
   *
   * @param books list of books to be added
   */
  public void addBooksToDataBase(List<DTBook> books) {
    entityManager = connectorProvider.getEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();

    transaction.begin();
    books.forEach(this::addBookToDatabase);
    transaction.commit();

    entityManager.close();
  }

  /**
   * Retrieves all books from database.
   *
   * @return list of books
   */
  public List<DTBook> getBooksFromDataBase() {

    entityManager = connectorProvider.getEntityManager();

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<BookstoreBook> query = criteriaBuilder.createQuery(BookstoreBook.class);

    Root<BookstoreBook> from = query.from(BookstoreBook.class);
    query.select(from);
    List<BookstoreBook> bookstoreBookList = entityManager.createQuery(query).getResultList();

    entityManager.close();
    return parseBookstoreBookListIntoDTBookList(bookstoreBookList);
  }

  private void addBookToDatabase(DTBook dtBook) {
    BookstoreBook bookstoreBook = parseDTBookIntoModel(dtBook);

    Bookstore bookstore =
        entityManager.find(Bookstore.class, bookstoreBook.getBookstore().getName());
    Book book = entityManager.find(Book.class, bookstoreBook.getBook().getBookId());
    HrefAndImage hrefAndImage =
        entityManager.find(HrefAndImage.class, bookstoreBook.getHrefAndImage().getHyperLink());

    refreshVariablesFromBookstoreBook(bookstoreBook, bookstore, book, hrefAndImage);

    entityManager.persist(bookstoreBook);
  }

  private void refreshVariablesFromBookstoreBook(
      BookstoreBook bookstoreBook, Bookstore bookstore, Book book, HrefAndImage hrefAndImage) {
    if (bookstore != null) {
      entityManager.refresh(bookstore);
      bookstoreBook.setBookstore(bookstore);
    }
    if (book != null) {
      entityManager.refresh(book);
      bookstoreBook.setBook(book);
    }
    if (hrefAndImage != null) {
      entityManager.refresh(hrefAndImage);
      bookstoreBook.setHrefAndImage(hrefAndImage);
    }
  }
}
