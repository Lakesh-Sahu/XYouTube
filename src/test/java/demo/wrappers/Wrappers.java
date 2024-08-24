package demo.wrappers;

import org.apache.poi.hpsf.Array;
import org.apache.xmlbeans.impl.xb.xsdschema.ListDocument.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.ArrayList;
import dev.failsafe.internal.util.Durations;

import java.time.Duration;

public class Wrappers {
    /*
     * Write your selenium wrappers here
     */
    ChromeDriver driver;
    WebDriverWait wait;

    public Wrappers(ChromeDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void openUrl() {
        driver.get("https://www.youtube.com/");
    }

    public void click(WebElement element) {
        element.click();
    }

    public void sendKeys(WebElement element, String message) {
        element.sendKeys(message);
    }

    public String getUrl() {
        return driver.getCurrentUrl();
    }

    public void scrollTO(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", element);
    }

    public WebElement findElementVisi(String xpath) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    public WebElement findElementPres(String xpath) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
    }

    public WebElement findElementVisi(String xpath, int waitTime) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitTime));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    public WebElement findElementPres(String xpath, int waitTime) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitTime));
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
    }

    public int intFromString(String intString) {
        int a = 0;
        String b = "0123456789";
        for (int i = 0; i < intString.length(); i++) {
            if (!b.contains(String.valueOf(intString.charAt(i)))) {
                break;
            }
            a++;
        }
        return Integer.parseInt(intString.substring(0, a));
    }

    public void getNewsTitleBodyLike(int n) {
        for (int i = 1; i <= n; i++) {
            WebElement titleBody = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                    "//span[text()='Latest news posts']/ancestor::div[@id='dismissible']//ytd-rich-item-renderer[" + i
                            + "]")));
            ArrayList<WebElement> allDescriptions = new ArrayList<>(
                    titleBody.findElements(By.xpath(".//span[@dir='auto']")));
            StringBuilder sb = new StringBuilder();
            for (WebElement webElement : allDescriptions) {
                sb.append(webElement.getText() + " ");
            }
            String titleBodyString = sb.toString();
            // String titleBodyString = titleBody.getText();
            WebElement like = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                    "//span[text()='Latest news posts']/ancestor::div[@id='dismissible']//ytd-rich-item-renderer[" + i
                            + "]//span[@id='vote-count-middle']")));
            String likeString = like.getText();
            if (likeString.equals(""))
                likeString = "0";
            System.out.println("News " + i + " : " + titleBodyString + "\n" + "Likes : " + likeString);
        }
    }

    public void search(String text) {
        WebElement searchBar = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='search']")));
        searchBar.sendKeys(text);
        searchBar.sendKeys(Keys.ENTER);
    }

    public void viewCount() throws InterruptedException {
        long totalViewCount = 0;
        int totalElementRendered = 0;

        while (totalViewCount < 100000000) {
            wait.until(ExpectedConditions
                    .presenceOfElementLocated(
                            By.xpath("(//ytd-video-renderer//span[contains(text(),'views')])[position() > "
                                    + totalElementRendered + "]")));
            ArrayList<WebElement> views = new ArrayList<>(
                    driver.findElements(By.xpath("(//ytd-video-renderer//span[contains(text(),'views')])[position() >"
                            + totalElementRendered + "]")));
            for (WebElement element : views) {                                                                      //Filtering K, lakh, crore, M, B and ajusting the viewCount value accordingly
                String viewString = element.getText();

                double count = intViewCountSeperator(viewString);

                if (viewString.contains("K"))
                    count = count * 1000;
                else if (viewString.contains("lakh"))
                    count *= 100000;
                else if (viewString.contains("crore"))
                    count *= 10000000;
                else if (viewString.contains("M"))
                    count *= 1000000;
                else if (viewString.contains("B"))
                    count *= 1000000000;

                totalViewCount = totalViewCount + (long) count;
            }
            // System.out.println("Total view count : " + totalViewCount);
            
            if (totalViewCount >= 100000000)
                return;

            totalElementRendered = views.size();
            // System.out.println("Total element rendered : " + totalElementRendered);

            scrollTO(views.get(totalElementRendered-1));                                                                          //Scrolling to the last rendered element
        }
    }

    public double intViewCountSeperator(String viewString) {
        int i = 0;
        for (i = 0; i < viewString.length(); i++) {
            if ("0123456789.".contains(Character.toString(viewString.charAt(i))))
                continue;
            else
                break;
        }
        return Double.parseDouble(viewString.substring(0, i));
    }
}