package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import demo.utils.ExcelDataProvider;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases extends ExcelDataProvider { // Lets us read the data
        ChromeDriver driver;
        Wrappers wp;
        WebDriverWait wait;

        /*
         * Go to YouTube.com and Assert you are on the correct URL. Click on "About" at
         * the bottom of the sidebar, and print the message on the screen.
         */
        @Test(enabled = true)
        public void testCase01() {

                try {
                        System.out.println("Start of testCase01");

                        // Opening YouTube url
                        wp.openUrl();

                        // Asserting that we are landing on correct url
                        Assert.assertEquals(wp.getUrl(), "https://www.youtube.com/", "Url Verification Failed");

                        // Locating, scrolling and clicking on "About" button
                        WebElement aboutBtn = wp.findElementVisi("//a[text()='About']");
                        wp.scrollTO(aboutBtn);
                        wp.click(aboutBtn);

                        // Waiting for about content to show to confirm page is open fully
                        wp.findElementVisi("//section[@class='ytabout__content']/p[1]");

                        // Storing status of current url contains "about" word on not
                        boolean status = driver.getCurrentUrl().contains("about");

                        // Asserting that we are on about page
                        Assert.assertEquals(status, true, "About page is not opend");

                        // Storing and printing the message shown on about page
                        List<WebElement> textElements = new ArrayList<>(
                                        driver.findElements(By.xpath("//section[@class='ytabout__content']/*")));
                        for (WebElement message : textElements) {
                                System.out.println(message.getText());
                        }

                        System.out.println("testCase01 Passed");

                } catch (Exception e) {
                        System.out.println("testCase01 Failed");
                }
                System.out.println("End of testCase01");
        }

        /*
         * Go to the "Films" or "Movies" tab and in the “Top Selling” section, scroll
         * to the extreme right. Apply a Soft Assert on whether the movie is marked “A”
         * for Mature or not. Apply a Soft assert on the movie category to check if it
         * exists ex: "Comedy", "Animation", "Drama".
         */
        @Test(enabled = true)
        public void testCase02() {
                try {
                        System.out.println("Start of testCase02");

                        // Opening YouTube url
                        wp.openUrl();

                        // Locating and clicking 'Movies' button
                        WebElement moviesBtn = wp.findElementVisi("//yt-formatted-string[text()='Movies']");
                        wp.click(moviesBtn);

                        // Locating and clicking the 'Next' button till it reaches last
                        WebElement nextBtn = wp.findElementVisi(
                                        "//span[text()='Top selling']/ancestor::div[@id='dismissible']//button[@aria-label='Next']");
                        while (nextBtn.isDisplayed()) {
                                wp.click(nextBtn);
                                try {
                                        nextBtn = wp.findElementVisi(
                                                        "//span[text()='Top selling']/ancestor::div[@id='dismissible']//button[@aria-label='Next']",
                                                        5);
                                } catch (Exception e) {
                                        break;
                                }
                        }

                        // Creating object of 'SoftAssert' class
                        SoftAssert sa = new SoftAssert();

                        // Locating the age category text of last movie and asserting that it is for
                        // Adult 'A'
                        String age = wp.findElementVisi(
                                        "(//span[text()='Top selling']/ancestor::div[@id='primary']//ytd-grid-movie-renderer[last()]/ytd-badge-supported-renderer//p)[2]")
                                        .getText();
                        sa.assertNotEquals(age, "A", "It is not for Mature");

                        // Locating that comedy, animation or any other category is present in the last
                        // movie
                        String category = wp.findElementVisi(
                                        "//span[text()='Top selling']/ancestor::div[@id='primary']//ytd-grid-movie-renderer[last()]/a/span")
                                        .getText();
                        sa.assertNotEquals(category.length(), 0, "There is not category present in this card");

                        // Asserting all the soft assert
                        sa.assertAll();

                        System.out.println("testCase02 Passed");

                } catch (Exception e) {
                        System.out.println("testCase02 Failed");
                }

                System.out.println("End of testCase02");
        }

        /*
         * Go to the "Music" tab and in the 1st section, scroll to the extreme right.
         * Print the name of the playlist. Soft Assert on whether the number of tracks
         * listed is less than or equal to 50.
         */
        @Test(enabled = true)
        public void testCase03() {
                try {
                        System.out.println("Start of testCase03");

                        // Creating object of 'SoftAssert'
                        SoftAssert sa = new SoftAssert();

                        // Opening 'YouTube' url
                        wp.openUrl();

                        // Locating and clicking 'Music' button
                        WebElement musicBtn = wp.findElementVisi("//yt-formatted-string[text()='Music']");
                        wp.click(musicBtn);

                        // Locating 'Next' button in the first section and clicking until it reaches
                        // last
                        WebElement nextBtn = wp.findElementVisi(
                                        "(//ytd-item-section-renderer[1])//button[@aria-label='Next']");
                        while (nextBtn.isDisplayed()) {
                                wp.click(nextBtn);
                                try {
                                        nextBtn = wp.findElementVisi(
                                                        "(//ytd-item-section-renderer[1])//button[@aria-label='Next']",
                                                        5);
                                } catch (Exception e) {
                                        break;
                                }
                        }

                        // Locating, getting and printing the last playlist name
                        String playlistName = wp.findElementVisi(
                                        "(//ytd-item-section-renderer[1])//ytd-compact-station-renderer[last()]//h3", 5)
                                        .getText();
                        System.out.println("Playlist Name : " + playlistName);

                        // Locating and storing total number of track videos present in the last music
                        // card
                        WebElement videoCount = wp.findElementVisi(
                                        "(//ytd-item-section-renderer[1])//ytd-compact-station-renderer[last()]//p[@id='video-count-text']",
                                        5);
                        int videoCountNum = wp.intFromString(videoCount.getText());

                        // Soft Asserting that total track count is less than or equal to 50
                        sa.assertTrue(videoCountNum <= 50, "Video Count is greater than 50 : " + videoCountNum);

                        // Asserting all the soft assert
                        sa.assertAll();

                        System.out.println("testCase03 Passed");

                } catch (Exception e) {
                        System.out.println("testCase03 Failed");
                }

                System.out.println("End of testCase03");
        }

        /*
         * Go to the "News" tab and print the title and body of the 1st 3 “Latest News
         * Posts” along with the sum of the number of likes on all 3 of them. No likes
         * given means 0.
         */
        @Test(enabled = true)
        public void testCase04() {
                try {
                        System.out.println("Start of testCase04");

                        // Opening 'YouTube' url
                        wp.openUrl();

                        // Asserting that 'YouTube' url is opened
                        Assert.assertEquals(wp.getUrl(), "https://www.youtube.com/", "Url Verification Failed");

                        // Locating and clicking the 'News' button
                        WebElement newsBtn = wp.findElementVisi("//yt-formatted-string[text()='News']");
                        wp.click(newsBtn);

                        // Getting the Title, Body and number of likes in News cards
                        wp.getNewsTitleBodyLike(3);

                        System.out.println("testCase04 Passed");

                } catch (Exception e) {
                        System.out.println("testCase04 Failed");
                }

                System.out.println("End of testCase04");
        }

        /*
         * Search for each of the items given in the stubs:
         * src/test/resources/data.xlsx, and keep scrolling till the sum of each video’s
         * views reach 10 Cr.
         */
        @Test(enabled = true, dataProvider = "excelData")
        public void testCase05(String text) throws InterruptedException {
                try {
                        System.out.println("Start of testCase05");

                        // Opening 'YouTube' url
                        wp.openUrl();

                        // Asserting that 'YouTube' url is opened
                        Assert.assertEquals(wp.getUrl(), "https://www.youtube.com/", "Url Verification Failed");

                        // Searching for text provided by the dataProvider
                        wp.search(text);

                        // Scrolling until total view count of videos is greater than or equal to the
                        // targetViewCount
                        wp.viewCount(100000000);

                        System.out.println("testCase05 Passed");

                } catch (Exception e) {
                        System.out.println("testCase05 Failed");
                }

                System.out.println("End of testCase05");
        }

        @BeforeTest
        public void startBrowser() {
                System.setProperty("java.util.logging.config.file", "logging.properties");

                // Creating object of ChromeOptions class
                ChromeOptions options = new ChromeOptions();

                // Creating object of LoggingPreferences
                LoggingPreferences logs = new LoggingPreferences();

                logs.enable(LogType.BROWSER, Level.ALL);
                logs.enable(LogType.DRIVER, Level.ALL);

                // Setting up the required options
                options.setCapability("goog:loggingPrefs", logs);
                options.addArguments("--remote-allow-origins=*");

                System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

                // Creating object of ChromeDriver class with options as arugument
                driver = new ChromeDriver(options);

                // Maximizing the browser window
                driver.manage().window().maximize();

                // Creating WebDriver object
                wait = new WebDriverWait(driver, Duration.ofSeconds(30));

                // Creating object of Wrappers class
                wp = new Wrappers(driver, wait);
        }

        // Quiting the browser
        @AfterTest
        public void endTest() {
                try {
                        driver.quit();
                        System.out.println("Quiting Complete");
                } catch (Exception e) {
                        System.out.println("Quiting Failed");
                }
        }
}