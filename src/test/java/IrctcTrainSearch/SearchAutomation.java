package IrctcTrainSearch;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

public class SearchAutomation {

    public static WebDriver driver;

    void launchbrowser(String browser1) throws InterruptedException {
        String browser2 = browser1;
        driver = DriverSetup.setupdriver(browser2);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.get("https://www.irctc.co.in");
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
    }


    void alert() {
        WebDriverWait mywait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            mywait.until(ExpectedConditions.alertIsPresent());
            Alert alertwind = driver.switchTo().alert();
            System.out.println("Alert Text: " + alertwind.getText());
            alertwind.accept();
        } catch (Exception e) {
            System.out.println("No alert found within the timeout.");
        }
    }

    void verify() throws InterruptedException {
        String expectedResult = "IRCTC Next Generation eTicketing System";
        String actualResult = driver.findElement(By.xpath("/html/head/title")).getText();
        System.out.println(actualResult);
        if (expectedResult.equalsIgnoreCase(expectedResult)) {
            System.out.println("Website Launched");
        } else {
            System.out.println("Page Not matched ");
        }
    }

    void selection() throws InterruptedException, IOException {
        String data1[] = null;
        data1 = utils.readdata("C:\\Users\\Pushkar Mishra\\eclipse-workspace\\check\\src\\test\\java\\check\\Book.xlsx");
        driver.findElement(By.xpath("//*[@id='origin']/span/input")).sendKeys(data1[0]);

        List<WebElement> list = driver.findElements(By.xpath("//*[@id='pr_id_1_list']"));
        for (int i = 0; i < list.size(); i++) {
            String Text = list.get(i).getText();
            if (Text.contains("HYDERABAD DECAN")) {
                list.get(i).click();
                break;
            }
        }

        driver.findElement(By.xpath("//*[@id='destination']/span/input")).sendKeys(data1[1]);
        List<WebElement> list1 = driver.findElements(By.xpath("//*[@id='pr_id_2_list']/li"));
        for (int i = 0; i < list1.size(); i++) {
            String Text = list1.get(i).getText();
            if (Text.contains("PUNE JN")) {
                list1.get(i).click();
                break;
            }
        }
    }

    void selectdate() throws InterruptedException {
        LocalDate current = LocalDate.now();
        LocalDate Futuredate = current.plusDays(4);
        String formatfuturedate = Futuredate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String xpathexp = "//*[@id=\"jDate\"]/span/input";
        WebElement dateElement = driver.findElement(By.xpath(xpathexp));
        dateElement.sendKeys(Keys.CONTROL + "a");
        dateElement.sendKeys(Keys.DELETE);
        dateElement.sendKeys(formatfuturedate);
        driver.findElement(By.xpath("//*[@id=\"divMain\"]/div/app-main-page/div/div/div[1]/div[1]/div[1]/app-jp-input/div/form/div[2]/div[2]/div[1]/span/i")).click();
    }

    void classselect() throws InterruptedException {
        driver.findElement(By.xpath("//*[@id=\"journeyClass\"]/div")).click();
        driver.findElement(By.xpath("//*[@id=\"journeyClass\"]/div/div[4]/div/ul/p-dropdownitem[12]/li")).click();
    }

    void search() throws InterruptedException {
        driver.findElement(By.xpath("//*[@id=\"divMain\"]/div/app-main-page/div/div/div[1]/div[1]/div[1]/app-jp-input/div/form/div[4]/div/span[1]/label")).click();
        driver.findElement(By.xpath("//*[@id=\"divMain\"]/div/app-main-page/div/div/div[1]/div[1]/div[1]/app-jp-input/p-confirmdialog/div/div/div[3]/button")).click();
        driver.findElement(By.xpath("//*[@id=\"divMain\"]/div/app-main-page/div/div/div[1]/div[1]/div[1]/app-jp-input/div/form/div[5]/div[1]/button")).click();
    }
    void validation() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            // Wait for the search results to be visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='col-sm-5 col-xs-11 train-heading']/strong")));

            WebElement title = driver.findElement(By.xpath("//*[@id=\"divMain\"]/div/app-train-list/div[4]/div/div[1]/span/strong"));
            String reqtitle = title.getText();
            String[] str = reqtitle.split(" ");
            String[] str1 = reqtitle.split(", ");
            String fromStation = str[0];
            String toStation = str[2];
            String date = str1[1];
            LocalDate date1 = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd MMM yyyy"));
            String formatteddate = date1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            String Expected1 = "HYDERABAD";
            String Expected2 = "PUNE";
            if (fromStation.equalsIgnoreCase(Expected1) && toStation.equalsIgnoreCase(Expected2) && formatteddate.equals(LocalDate.now().plusDays(4).toString())) {
                System.out.println("Trains displayed are correct");
            } else {
                System.out.println("Trains displayed are incorrect. Taking a screenshot...");

                // Take a screenshot
                takeScreenshot("validation_failed");

                System.out.println("Screenshot taken.");

                // Optionally, you can throw an exception to indicate validation failure
                throw new RuntimeException("Validation failed: Trains displayed are incorrect");
            }
        } catch (Exception e) {
            System.out.println("Validation failed: " + e.getMessage());
        }
    }




    void display() throws InterruptedException {
        List<WebElement> list3 = driver.findElements(By.xpath("//div[@class='col-sm-5 col-xs-11 train-heading']/strong"));
        System.out.println("The number of trains available: " + list3.size());
        
        // Take a screenshot of the result page
        takeScreenshot("result_page");

        for (int i = 0; i < list3.size(); i++) {
            String names = list3.get(i).getText();
            System.out.println(names);
        }
    }


    public void takeScreenshot(String screenshotName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            
            // Specify the absolute path where you want to save the screenshot
            String absolutePath = "C:\\Users\\DELL\\eclipse-workspace\\IrctcTrain\\src\\test\\resources\\Screenshot\\" + screenshotName + ".png";
            
            FileUtils.copyFile(source, new File(absolutePath));
            System.out.println("Screenshot taken: " + screenshotName);
        } catch (IOException e) {
            System.out.println("Exception while taking screenshot: " + e.getMessage());
        }
    }


    public static void main(String args[]) {
        SearchAutomation ob = new SearchAutomation();
        Scanner sc = new Scanner(System.in);
        System.out.println("enter the browser :");
        String x = sc.nextLine();
        try {
            ob.launchbrowser(x);
            ob.verify();
            ob.alert();
            ob.selection();
            ob.selectdate();
            ob.classselect();
            ob.search();
            ob.validation();
            ob.display();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred: " + e.getMessage());
        } finally {
            sc.close();
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
