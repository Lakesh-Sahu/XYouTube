package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level; 

import demo.utils.ExcelDataProvider;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases extends ExcelDataProvider{ // Lets us read the data
        ChromeDriver driver;
        Wrappers wp;
        WebDriverWait wait;

        /*
         * TODO: Write your tests here with testng @Test annotation.
         * Follow `testCase01` `testCase02`... format or what is provided in
         * instructions
         */
        @Test
        public void testCase01() {
                System.out.println("Start of testCase01");
                
                wp.openUrl();
                Assert.assertEquals(wp.getUrl(), "https://www.youtube.com/", "Url Verification Failed");
                WebElement aboutBtn = wp.findElementVisi("//a[text()='About']");
                wp.scrollTO(aboutBtn);
                wp.click(aboutBtn);

                wp.findElementVisi("//section[@class='ytabout__content']/p[1]");
                driver.getCurrentUrl().contains("about");

                List<WebElement> textElements = new ArrayList<>(driver.findElements(By.xpath("//section[@class='ytabout__content']/*")));
                for (WebElement message : textElements) {
                        System.out.println(message.getText());
                }
                System.out.println("End of testCase01");
        }

        @Test
        public void testCase02() {
                System.out.println("Start of testCase02");

                wp.openUrl();
                WebElement moviesBtn = wp.findElementVisi("//yt-formatted-string[text()='Movies']");
                wp.click(moviesBtn);

                WebElement nextBtn = wp.findElementVisi("//span[text()='Top selling']/ancestor::div[@id='dismissible']//button[@aria-label='Next']");
                while(nextBtn.isDisplayed()) {
                        wp.click(nextBtn);
                        try {
                                nextBtn = wp.findElementVisi("//span[text()='Top selling']/ancestor::div[@id='dismissible']//button[@aria-label='Next']", 5);
                        }
                        catch(Exception e) {
                                break;
                        }
                }

                SoftAssert sa = new SoftAssert();
                String age = wp.findElementVisi("(//span[text()='Top selling']/ancestor::div[@id='primary']//ytd-grid-movie-renderer[last()]/ytd-badge-supported-renderer//p)[2]").getText();
                sa.assertNotEquals(age, "A", "It is not for Mature");

                String category = wp.findElementVisi("//span[text()='Top selling']/ancestor::div[@id='primary']//ytd-grid-movie-renderer[last()]/a/span").getText();
                sa.assertNotEquals(category.length(), 0, "There is not category present in this card");

                sa.assertAll();
                System.out.println("End of testCase02");
        }

        @Test
        public void testCase03() {
                System.out.println("Start of testCase03");

                SoftAssert sa = new SoftAssert();
                wp.openUrl();
                WebElement musicBtn = wp.findElementVisi("//yt-formatted-string[text()='Music']");
                wp.click(musicBtn);

                WebElement nextBtn = wp.findElementVisi("(//ytd-item-section-renderer[1])//button[@aria-label='Next']");
                while(nextBtn.isDisplayed()) {
                        wp.click(nextBtn);
                        try {
                                nextBtn = wp.findElementVisi("(//ytd-item-section-renderer[1])//button[@aria-label='Next']", 5);
                        }
                        catch(Exception e) {
                                break;
                        }
                }
                String playlistName = wp.findElementVisi("(//ytd-item-section-renderer[1])//ytd-compact-station-renderer[last()]//h3", 5).getText();
                System.out.println("Playlist Name : " + playlistName);

                WebElement videoCount = wp.findElementVisi("(//ytd-item-section-renderer[1])//ytd-compact-station-renderer[last()]//p[@id='video-count-text']", 5);
                int videoCountNum = wp.intFromString(videoCount.getText());
                sa.assertTrue(videoCountNum <= 50, "Video Count is greater than 50 : " + videoCountNum);

                sa.assertAll();
                System.out.println("End of testCase03");
        }

        @Test
        public void testCase04() {
                System.out.println("Start of testCase04");
                
                wp.openUrl();
                Assert.assertEquals(wp.getUrl(), "https://www.youtube.com/", "Url Verification Failed");
                
                WebElement newsBtn = wp.findElementVisi("//yt-formatted-string[text()='News']");
                wp.click(newsBtn);

                wp.getNewsTitleBodyLike(3);

                System.out.println("End of testCase04");
        }

        @Test (dataProvider = "excelData")
        public void testCase05(String text) throws InterruptedException {
                System.out.println("Start of testCase05");
                
                wp.openUrl();
                Assert.assertEquals(wp.getUrl(), "https://www.youtube.com/", "Url Verification Failed");
               
                wp.search(text);

                wp.viewCount();

                System.out.println("End of testCase05");
        }


 

        /*
         * Do not change the provided methods unless necessary, they will help in
         * automation and assessment
         */
        @BeforeTest
        public void startBrowser() {
                System.setProperty("java.util.logging.config.file", "logging.properties");

                // NOT NEEDED FOR SELENIUM MANAGER
                // WebDriverManager.chromedriver().timeout(30).setup();

                ChromeOptions options = new ChromeOptions();
                LoggingPreferences logs = new LoggingPreferences();

                logs.enable(LogType.BROWSER, Level.ALL);
                logs.enable(LogType.DRIVER, Level.ALL);
                options.setCapability("goog:loggingPrefs", logs);
                options.addArguments("--remote-allow-origins=*");

                System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

                driver = new ChromeDriver(options);

                driver.manage().window().maximize();
                wait = new WebDriverWait(driver, Duration.ofSeconds(30));
                wp = new Wrappers(driver, wait);
        }

        @AfterTest
        public void endTest() {
                // driver.close();
                driver.quit();

        }
}