import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.Random;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.WebDriverException;

import org.openqa.selenium.JavascriptExecutor;

import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import org.openqa.selenium.support.ui.Select;

public class NoFlood  {

  public static void main(String[] args) throws Exception {
    int iterations = 0;

    // Create a new instance of the html unit driver
    // Notice that the remainder of the code relies on the interface,
    // not the implementation.
    WebDriverManager.chromedriver().setup();
    WebDriver driver = new ChromeDriver();

    // It's up to you to control test duration / iterations programatically.
    while( iterations < 1000 ) {
      try {

        System.out.println("Starting iteration " +  String.valueOf(iterations));

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        // And now use this to visit the target site
        driver.get("http://ec2-3-88-221-246.compute-1.amazonaws.com:3000");
        System.out.println("DEBUG - Navigate to RoundRobin LB");

        // Change select element to desired video
        Select videos = new Select(driver.findElement(By.id("videoSelector")));
        videos.selectByVisibleText("Big Buck Bunny");

        // Wait for video to finish playing
        Thread.sleep(185000);

        iterations++;

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
  }
}
