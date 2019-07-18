package edition.academy.seventh.serivce;

import com.fasterxml.jackson.databind.ObjectMapper;
import edition.academy.seventh.database.model.Book;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/** @author Bartosz Kupajski */
@Service
public class ItBookMapper {

  private ObjectMapper objectMapper;

  public ItBookMapper() {
    this.objectMapper = new ObjectMapper();
  }

  /**
   * Maps JSON books file into {@code List<Book>}
   *
   * @param listOfJSONBook list with books' JSONs
   * @return {@code List<Book>}
   * @throws IOException when JSON isn't read properly
   */
  public List<Book> mapListOfJson(List<String> listOfJSONBook) throws IOException {
    List<Book> listOfBooks = new LinkedList<>();

    for (String bookJSON : listOfJSONBook) {
      Book book = objectMapper.readValue(bookJSON, Book.class);
      listOfBooks.add(book);
    }

    return listOfBooks;
  }
}