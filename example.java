import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriverException;

import org.openqa.selenium.JavascriptExecutor;

import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import org.openqa.selenium.support.ui.Select;

import io.flood.selenium.FloodSump;

public class SeleniumExample  {

  public static void main(String[] args) throws Exception {
    int iterations = 0;

    // Create a new instance of the html unit driver
    // Notice that the remainder of the code relies on the interface,
    // not the implementation.
    WebDriver driver = new RemoteWebDriver(new URL("http://" + System.getenv("WEBDRIVER_HOST") + ":" + System.getenv("WEBDRIVER_PORT") + "/wd/hub"), DesiredCapabilities.chrome());
    JavascriptExecutor js = (JavascriptExecutor)driver;

    // Create a new instance of the Flood IO agent
    FloodSump flood = new FloodSump();

    // Inform Flood IO the test has started
    flood.started();

    // It's up to you to control test duration / iterations programatically.
    while( iterations < 1000 ) {
      try {

        System.out.println("Starting iteration " +  String.valueOf(iterations));

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        // And now use this to visit the target site
        driver.get("https://jriz.io/");
        System.out.println("DEBUG - Navigate to main site at https://jriz.io");

        // Log a passed transaction in Flood IO
        flood.passed_transaction(driver, "The Flood Store - Home");

        //select the accessories section
        driver.findElement(By.cssSelector("img[alt='Accessories']")).click();
        System.out.println("DEBUG - Click Accessories category on the main site");
        flood.passed_transaction(driver, "The Flood Store - Accessories");

        //add item to cart
        Random r = new java.util.Random();
        List <WebElement> links = driver.findElements(By.cssSelector("a[class='button product_type_simple add_to_cart_button ajax_add_to_cart']"));
        WebElement randomElement = links.get(r.nextInt(links.size()));

        int linkListCount = links.size();
        System.out.println("Total Number of link count on page = "  + linkListCount);

        randomElement.click();
        flood.passed_transaction(driver, "The Flood Store - Add Item to Cart");

        //Go to your cart
        driver.get("http://jriz.io/cart/");
        flood.passed_transaction(driver, "The Flood Store - View Cart");

        // Good idea to introduce some form of pacing / think time into your scripts
        Thread.sleep(2000);

        iterations++;

      } catch (WebDriverException e) {
        // Log a webdriver exception in flood
        flood.webdriver_exception(driver, e);
      } catch(InterruptedException e) {
        Thread.currentThread().interrupt();
        String[] lines = e.getMessage().split("\\r?\\n");
        System.err.println("Browser terminated early: " + lines[0]);
      } catch(Exception e) {
        String[] lines = e.getMessage().split("\\r?\\n");
        System.err.println("Other exception: " + lines[0]);
      } finally {
        iterations++;
      }
    }

    driver.quit();

    // Inform Flood IO the test has finished
    flood.finished();
  }
}
