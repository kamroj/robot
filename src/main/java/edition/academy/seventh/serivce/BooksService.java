package edition.academy.seventh.serivce;

import edition.academy.seventh.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edition.academy.seventh.database.model.Book;
import java.util.List;

/**
 * Service that provides API reponsible for adding books into database.
 *
 * @author Agnieszka Trzewik
 */
@Service
public class BooksService {

  private BookRepository bookRepository;

  @Autowired
  public BooksService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  void addBooksToDataBase(List<Book> books) {
    bookRepository.addBooksToDataBase(books);
  }
}
