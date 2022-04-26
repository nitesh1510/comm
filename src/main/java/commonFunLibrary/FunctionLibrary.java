package commonFunLibrary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import Utilities.PropertyFileUtil;

public class FunctionLibrary{
	public static WebDriver driver;
	//method for launching browser
	public static WebDriver startBrowser()throws Throwable
	{
		if(PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("chrome"))
		{
			System.setProperty("webdriver.chrome.driver", "./CommonDrivers/chromedriver.exe");
			driver = new ChromeDriver();
			driver.manage().deleteAllCookies();
			driver.manage().window().maximize();
		}
		else if(PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("firefox"))
		{
			System.setProperty("webdriver.gecko.driver", "./CommonDrivers/geckodriver.exe");
			driver = new FirefoxDriver();
			driver.manage().deleteAllCookies();
		}
		else
		{
			System.out.println("Browser value is Not Matching");
		}
		return driver;
	}
	//method for launch url
	public static void openApplication(WebDriver driver) throws Throwable
	{
		driver.get(PropertyFileUtil.getValueForKey("Url"));
	}
	//method for textboxes
	public static void typeAction(WebDriver driver,String locatortype,String locatorvalue,String testdata)
	{
		if(locatortype.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(locatorvalue)).clear();
			driver.findElement(By.name(locatorvalue)).sendKeys(testdata);
		}
		else if(locatortype.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(locatorvalue)).clear();
			driver.findElement(By.id(locatorvalue)).sendKeys(testdata);
		}
		else if(locatortype.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(locatorvalue)).clear();
			driver.findElement(By.xpath(locatorvalue)).sendKeys(testdata);
		}
		else 
		{
			System.out.println("Unable to   execut type Action method");
		}
	}
	//method for wait for element
	public static void waitForElement(WebDriver driver,String locatortype,String locatorvalue,String waittime)
	{
		WebDriverWait  myWait = new WebDriverWait(driver, Integer.parseInt(waittime));
		if(locatortype.equalsIgnoreCase("name"))
		{
			myWait.until(ExpectedConditions.visibilityOfElementLocated(By.name(locatorvalue)));
		}
		else if(locatortype.equalsIgnoreCase("id"))
		{
			myWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(locatorvalue)));
		}
		else if(locatortype.equalsIgnoreCase("xpath"))
		{
			myWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locatorvalue)));
		}
		else
		{
			System.out.println("Unable to execute waitforelement method");
		}
	}
	//method for clickAcion
	public static void clickAction(WebDriver driver,String locatortype,String locatorvalue)
	{
		if(locatortype.equalsIgnoreCase("name"))
		{
			driver.findElement(By.name(locatorvalue)).click();
		}
		else if(locatortype.equalsIgnoreCase("id"))
		{
			driver.findElement(By.id(locatorvalue)).sendKeys(Keys.ENTER);
		}
		else if(locatortype.equalsIgnoreCase("xpath"))
		{
			driver.findElement(By.xpath(locatorvalue)).click();
		}
		else
		{
			System.out.println("Unable to execute clickaction method");
		}
	}
	//method for validate title
	public static void validateTitle(WebDriver driver,String expectedtitle)
	{
		String actualtitle = driver.getTitle();
		try
		{
			Assert.assertEquals(expectedtitle, actualtitle,"Title is Not matching");
		}catch(Throwable t)
		{
			System.out.println(t.getMessage());
		}
	}
	//method for close browser
	public static void closeBrowser(WebDriver driver)
	{
		driver.close();
	}
	//method writing supplier number into notepad
	public static void captureData(WebDriver driver,String locatortype,String locatorvalue)throws Throwable
	{
		String suppliernumber="";
		if(locatortype.equalsIgnoreCase("name"))
		{
			suppliernumber= driver.findElement(By.name(locatorvalue)).getAttribute("value");
		}
		else if(locatortype.equalsIgnoreCase("xpath"))
		{
			suppliernumber= driver.findElement(By.xpath(locatorvalue)).getAttribute("value");
		}
		FileWriter fw = new FileWriter("./CaptureData/supplier.txt");
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(suppliernumber);
		bw.flush();
		bw.close();
	}
	//method for supplier table
	public static void supplierTable(WebDriver driver,String column)throws Throwable
	{
	FileReader fr = new FileReader("./CaptureData/supplier.txt")	;
	BufferedReader br = new BufferedReader(fr);
	String expectedNum = br.readLine();
	int ColNum = Integer.parseInt(column);
	if(!driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).isDisplayed())
		//if search textbox not displayed click search panel
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-panel"))).click();
	Thread.sleep(3000);
	driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).clear();
	Thread.sleep(3000);
	driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).sendKeys(expectedNum);
	driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-button"))).click();
	Thread.sleep(5000);
	WebElement table = driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("web-tablepath")));
	List<WebElement> rows = table.findElements(By.tagName("tr"));
	for(int i=1;i<rows.size();i++)
	{
		//capture supplier number
		String ActualNum = driver.findElement(By.xpath("//table[@id='tbl_a_supplierslist']/tbody/tr[1]/td[6]/div/span/span")).getText();
		System.out.println(expectedNum+"        "+ActualNum);
		Assert.assertEquals(expectedNum, ActualNum,"Supplier number not matching");
		break;
	}
	}
	public static void mouseClick(WebDriver driver)throws Throwable
	{
		Actions ac = new Actions(driver);
		ac.moveToElement(driver.findElement(By.xpath("//body/div[2]/div[2]/div[1]/div[1]/ul[1]/li[2]/a[1]"))).perform();
		Thread.sleep(4000);
		ac.moveToElement(driver.findElement(By.xpath("//body/div[2]/div[2]/div[1]/div[1]/ul[1]/li[2]/ul[1]/li[1]/a[1]"))).click().perform();
	}
	//method for StockCategoriesTable
	public static void StockCategoriesTable(WebDriver driver,String testData)throws Throwable
	{
		if(!driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).isDisplayed())
			//if search textbox not displayed click search panel
			driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-panel"))).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).clear();
		Thread.sleep(3000);
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).sendKeys(testData);
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-button"))).click();
		Thread.sleep(5000);
		String actualdata = driver.findElement(By.xpath("//table[@id='tbl_a_stock_categorieslist']/tbody/tr[1]/td[4]/div/span/span")).getText();
		System.out.println(actualdata+"     "+testData);
		Assert.assertEquals(actualdata, testData,"StockCategories is Not Matching");
		
	}
	public static String generateDate()
	{
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("YYYY_MM_dd hh_mm_ss");
		return df.format(date);
	}
}







