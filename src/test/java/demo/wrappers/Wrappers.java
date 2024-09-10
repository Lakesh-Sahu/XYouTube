package demo.wrappers;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.ArrayList;
import java.time.Duration;

public class Wrappers {
    /*
     * Write your selenium wrappers here
     */
    ChromeDriver driver;
    WebDriverWait wait;

    // Constructor of Wrappers class
    public Wrappers(ChromeDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    // Open 'YouTube' url
    public void openUrl() {
        driver.get("https://www.youtube.com/");
    }

    // Click on an element
    public void click(WebElement element) {
        element.click();
    }

    // Send message on an element with
    public void sendKeys(WebElement element, String message) {
        element.sendKeys(message);
    }

    // Get the current url of web page
    public String getUrl() {
        return driver.getCurrentUrl();
    }

    // Scroll to a particular element using java script executor
    public void scrollTO(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", element);
    }

    // Locate an element using wait, xpath and Expected Conditions of
    // visibilityOfElementLocated
    public WebElement findElementVisi(String xpath) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    // Locate an element using wait, xpath and Expected Conditions of
    // presenceOfElementLocated
    public WebElement findElementPres(String xpath) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
    }

    // Locate an element using max required wait, xpath and Expected Conditions of
    // visibilityOfElementLocated
    public WebElement findElementVisi(String xpath, int waitTime) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitTime));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    // Locate an element using max required wait, xpath and Expected Conditions of
    // presenceOfElementLocated
    public WebElement findElementPres(String xpath, int waitTime) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(waitTime));
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
    }

    // Seperating Starting Integer from String
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

    // Getting the Title, Body and number of likes in News cards
    public void getNewsTitleBodyLike(int n) {

        // Ittereating through the number of new card(n) given
        for (int i = 1; i <= n; i++) {

            // Locating the title webelement
            WebElement titleBody = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                    "//span[text()='Latest news posts']/ancestor::div[@id='dismissible']//ytd-rich-item-renderer[" + i
                            + "]")));

            // Storing the sub webelements which have title, descriptions from the parent
            // titleBody webelement
            ArrayList<WebElement> allDescriptions = new ArrayList<>(
                    titleBody.findElements(By.xpath(".//span[@dir='auto']")));

            // Creating StringBuilder object
            StringBuilder sb = new StringBuilder();

            // Itterating through all stored webelemnt
            for (WebElement webElement : allDescriptions) {

                // Getting the text and appending to a StringBuilder object
                sb.append(webElement.getText() + " ");
            }

            // Converting the StringBuilder object to String
            String titleBodyString = sb.toString();

            // Locating the like count
            WebElement like = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
                    "//span[text()='Latest news posts']/ancestor::div[@id='dismissible']//ytd-rich-item-renderer[" + i
                            + "]//span[@id='vote-count-middle']")));

            // Getting the like count as String
            String likeString = like.getText();

            // If like count String is empty then taking it as 0
            if (likeString.equals(""))
                likeString = "0";

            // Printing all the data
            System.out.println("News " + i + " : " + titleBodyString + "\n" + "Likes : " + likeString);
        }
    }

    // Searching for given text
    public void search(String text) {
        WebElement searchBar = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='search']")));
        searchBar.sendKeys(text);
        searchBar.sendKeys(Keys.ENTER);
    }

    // Scrolling until total view count of videos is greater than or equal to the
    // targetViewCount
    public void viewCount(long targetViewCount) throws InterruptedException {
        long totalViewCount = 0;
        int totalElementRendered = 0;

        while (totalViewCount < targetViewCount) {

            // Waiting for the view count webelement
            wait.until(ExpectedConditions
                    .presenceOfElementLocated(
                            By.xpath("(//ytd-video-renderer//span[contains(text(),'views')])[position() > "
                                    + totalElementRendered + "]")));

            // Locating and storing the view count webelement
            ArrayList<WebElement> views = new ArrayList<>(
                    driver.findElements(By.xpath("(//ytd-video-renderer//span[contains(text(),'views')])[position() >"
                            + totalElementRendered + "]")));

            // Converting the view count from K, lakh, crore, M, B into digit and adding to
            // totalViewCount
            for (WebElement element : views) {

                // Getting the view count as String
                String viewString = element.getText();

                // Getting the Integer part from the String
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

            if (totalViewCount >= targetViewCount)
                return;

            // Storing the last position of rendered
            totalElementRendered = views.size();

            // Scrolling to the last views element so that new section can render
            scrollTO(views.get(totalElementRendered - 1));
        }
    }

    // Seperating the Integer part from the view count
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