package edition.academy.seventh.serivce;

import edition.academy.seventh.database.model.Book;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Scrap data from empik.com bookstore website in sales section using JSoup library.
 *
 * @author Bartosz Kupajski
 */
public class EmpikScrapping implements IPromotionScrapping {

    private List<Book> listOfBooks = new CopyOnWriteArrayList<>();
    private ExecutorService service = Executors.newFixedThreadPool(40);
    private Phaser phaser = new Phaser(1);
  /**
        Above that number SSLException is thrown
     */
    private final int maxConnections = 1201;

  /**
   * Index in for loop is incremented by 30 because of the fact that
   * every page URL of empik sales section contains id witch is incremented by 30.
   *
   * @return list of books after all threads finish their jobs
   *
   */
  @Override
  public List<Book> scrapPromotion() {

      for (int i = 1; i <= maxConnections; i = i + 30) {
        service.submit(createScrappingTask(i));
        }

        phaser.arriveAndAwaitAdvance();
      return listOfBooks;
    }

  private Runnable createScrappingTask(int serchSiteNumber) {
    return () -> {
      phaser.register();
      String url = createUrl(serchSiteNumber);
      Document document = null;
      try {
        document = Jsoup.connect(url).get();
      } catch (IOException exception) {
        System.err.println(exception.getMessage());
      }
      Elements elementsByClass =
          document.getElementsByClass("product-details-wrapper ta-details-box");

      mappingToBookList(listOfBooks, elementsByClass);
    };
  }

  private String createUrl(int numberOfPage) {
    String startOfUrl = "https://www.empik.com/promocje?searchCategory=31&hideUnavailable=true&start=";
    String endOfUrl = "&qtype=facetForm";
    return startOfUrl + numberOfPage + endOfUrl;
  }

  private void mappingToBookList(List<Book> listOfBooks, Elements elementsByClass) {
    elementsByClass.stream()
        .map(
            element -> {
              String title = element.getElementsByClass("ta-product-title").text();
              String author = element.getElementsByClass("smartAuthor").text();
              String promotionPrice = element.getElementsByClass("ta-price-tile").text();
              String[] prices = promotionPrice.split(" ");
              Book book = new Book();
              book.setTitle(title);
              book.setAuthors(author);
              book.setSubtitle("");
              book.setPrice(prices[2]);
              book.setPromotion(prices[0]);
              return book;
            })
        .forEach(listOfBooks::add);
        phaser.arrive();
    }
}