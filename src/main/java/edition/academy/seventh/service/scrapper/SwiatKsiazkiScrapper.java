package edition.academy.seventh.service.scrapper;

import edition.academy.seventh.database.model.DtoBook;
import edition.academy.seventh.service.PromotionProvider;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

/**
 * Scraps data from swiatksiazki.pl bookstore website in sales section using JSoup library.
 *
 * @author Kacper Staszek
 */
@Service
public class SwiatKsiazkiScrapper implements PromotionProvider {

  private List<DtoBook> listOfBooks = new CopyOnWriteArrayList<>();
  private ExecutorService service = Executors.newFixedThreadPool(40);
  private Phaser phaser = new Phaser(1);

  /**
   * Scraps 30 positions for each iteration.
   *
   * @return list of books after all threads finish their jobs
   */
  @Override
  public List<DtoBook> getPromotions() {
    for (int i = 1; i <= 3; i++) {
      service.submit(createScrappingTask(i));
    }

    phaser.arriveAndAwaitAdvance();
    return listOfBooks;
  }

  private String createUrl(int i) {

    String startOfUrl = "https://www.swiatksiazki.pl/Ksiazki/outlet-3255.html?p=";
    String endOfUrl = "&product_list_limit=30&product_list_mode=grid";

    return startOfUrl + i + endOfUrl;
  }

  private Runnable createScrappingTask(int searchSiteNumber) {
    return () -> {
      phaser.register();
      String url = createUrl(searchSiteNumber);
      Document document = null;
      try {
        document = Jsoup.connect(url).timeout(0).get();
      } catch (IOException exception) {
        System.err.println(exception.getMessage());
      }
      Elements elementsByClass = document.getElementsByClass("item product product-item");

      mappingToBookList(elementsByClass);
    };
  }

  private void mappingToBookList(Elements elementsByClass) {
    String nameOfTheBookstore = "ŚWIAT KSIĄŻKI";
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
              return new DtoBook(
                  title,
                  "",
                  author,
                  retailPrice,
                  promotionalPrice,
                  imageLink,
                  href,
                  nameOfTheBookstore);
            })
        .forEach(listOfBooks::add);
    phaser.arrive();
  }

  private String deleteOutletSign(String title) {
    return title.replace("[OUTLET] ", "");
  }
}
