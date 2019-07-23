package edition.academy.seventh.service.scrapper;

import edition.academy.seventh.database.model.BookDto;
import java.util.List;
import org.jsoup.select.Elements;

/**
 * Scraps data from swiatksiazki.pl bookstore website in sales section using JSoup library.
 *
 * <p>{@link AbstractScrapper} {@link edition.academy.seventh.service.PromotionProvider}
 *
 * @author Kacper Staszek
 */
class SwiatKsiazkiScrapper extends AbstractScrapper {
  private final String bookstoreName;

  SwiatKsiazkiScrapper(
      String startOfUrl, String endOfUrl, String documentClassName, String bookstoreName) {
    super(startOfUrl, endOfUrl, documentClassName);
    this.bookstoreName = bookstoreName;
  }

  /**
   * Scraps 30 positions for each iteration.
   *
   * @return {@link List< BookDto >} after all threads finish their jobs.
   */
  @Override
  public List<BookDto> getPromotions() {
    for (int i = 1; i <= 1; i++) {
      service.submit(createScrappingTask(i));
      logger.info("Submitting scrapping task for page: " + startOfUrl + i + endOfUrl);
    }

    phaser.arriveAndAwaitAdvance();

    return listOfBooks;
  }

  @Override
  void mappingToBookList(Elements elementsByClass) {
    logger.info("Starting particular task. Executor is: " + service + " AND Phaser is: " + phaser);
    elementsByClass.stream()
        .map(
            element -> {
              String title = element.getElementsByClass("product name product-item-name").text();
              title = deleteOutletSign(title);
              String href = element.getElementsByClass("product-item-link").attr("href");
              String imageLink =
                  element.getElementsByClass("product-image-photo lazy").attr("data-src");
              String author =
                  element.getElementsByClass("product author product-item-author").text();
              String promotionalPrice = element.getElementsByClass("special-price").text();
              String retailPrice = element.getElementsByClass("old-price").text();
              return new BookDto(
                  title, "", author, retailPrice, promotionalPrice, imageLink, href, bookstoreName);
            })
        .forEach(listOfBooks::add);
    phaser.arriveAndDeregister();
    logger.info("Ending particular task. Executor is: " + service + " AND Phaser is: " + phaser);
  }

  private String deleteOutletSign(String title) {
    return title.replace("[OUTLET] ", "");
  }
}
