package edition.academy.seventh.serivce;

import org.testng.annotations.Test;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

@Test
public class BookDataDownloaderTest {

  @Test(
      dataProviderClass = DataProviderForConvertingIsbnToBook.class,
      dataProvider = "dataProviderForConvertingIsbnToBook")
  public void should_returnListOfBooksAsString_when_haveListOfIsbns(
          List<String> isbns, List<String> expectedBooks) {
    // Given
    BookstoreConnector bookstoreConnector = mock(BookstoreConnector.class);
    when(bookstoreConnector.getJsonResponse(anyString())).thenReturn(expectedBooks.get(0));

    BookDataDownloader bookDataDownloader =
        new BookDataDownloader("https://api.itbook.store/1.0/books/", bookstoreConnector);

    // When
    List<String> books = bookDataDownloader.listBooksByIsbn(isbns);

    // Then
    assertEquals(books, expectedBooks);
  }
}
