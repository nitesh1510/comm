package DriverFactory;

import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import Utilities.ExcelFileUtil;
import commonFunLibrary.FunctionLibrary;

public class DriverScript {
	public static WebDriver driver;
String inputpath="D:\\ojtproject\\StockAccount_Maven\\TestInput\\HybridTest.xlsx";
String outputpath ="D:\\ojtproject\\StockAccount_Maven\\TestOutput\\HybridResults.xlsx";
ExtentReports report;
ExtentTest test;
public void startTest()throws Throwable
{
	//access excel file util methods
	ExcelFileUtil xl = new ExcelFileUtil(inputpath);
	//iterate all rows in Mastertestcases sheet
	for(int i=1;i<=xl.rowCount("MasterTestCases");i++)
	{
		String moduleStatus="";
		if(xl.getCellData("MasterTestCases", i, 2).equalsIgnoreCase("Y"))
		{
			//store corresponding Sheet into TCModule
			String TCModule =xl.getCellData("MasterTestCases", i, 1);
			//define path for html
			report= new ExtentReports("./Reports/"+TCModule+"  "+FunctionLibrary.generateDate()+".html");
			//iterate all in TCModule sheet
			for(int j=1;j<=xl.rowCount(TCModule);j++)
			{
				test=report.startTest(TCModule);
				//read all cells from TCModule
				String Descrition = xl.getCellData(TCModule, j, 0);
				String ObjectType =xl.getCellData(TCModule, j, 1);
				String LocatorType =xl.getCellData(TCModule, j, 2);
				String LocatorValue =xl.getCellData(TCModule, j, 3);
				String TestData = xl.getCellData(TCModule, j, 4);
				try
				{
					if(ObjectType.equalsIgnoreCase("startBrowser"))
					{
						driver = FunctionLibrary.startBrowser();
						test.log(LogStatus.INFO, Descrition);
					}
					else if(ObjectType.equalsIgnoreCase("openApplication"))
					{
						FunctionLibrary.openApplication(driver);
						test.log(LogStatus.INFO, Descrition);
					}
					else if(ObjectType.equalsIgnoreCase("waitForElement"))
					{
						FunctionLibrary.waitForElement(driver, LocatorType, LocatorValue, TestData);
						test.log(LogStatus.INFO, Descrition);
					}
					else if(ObjectType.equalsIgnoreCase("typeAction"))
					{
						FunctionLibrary.typeAction(driver, LocatorType, LocatorValue, TestData);
						test.log(LogStatus.INFO, Descrition);
					}
					else if(ObjectType.equalsIgnoreCase("clickAction"))
					{
						FunctionLibrary.clickAction(driver, LocatorType, LocatorValue);
						test.log(LogStatus.INFO, Descrition);
					}
					else if(ObjectType.equalsIgnoreCase("validateTitle"))
					{
						FunctionLibrary.validateTitle(driver, TestData);
						test.log(LogStatus.INFO, Descrition);
					}
					else if(ObjectType.equalsIgnoreCase("closeBrowser"))
					{
						FunctionLibrary.closeBrowser(driver);
						test.log(LogStatus.INFO, Descrition);
					}
					else if(ObjectType.equalsIgnoreCase("captureData"))
					{
						FunctionLibrary.captureData(driver, LocatorType, LocatorValue);
						test.log(LogStatus.INFO, Descrition);
					}
					else if(ObjectType.equalsIgnoreCase("supplierTable"))
					{
						FunctionLibrary.supplierTable(driver, TestData);
						test.log(LogStatus.INFO, Descrition);
					}
					else if(ObjectType.equalsIgnoreCase("mouseClick"))
					{
						FunctionLibrary.mouseClick(driver);
						test.log(LogStatus.INFO, Descrition);
					}
					else if(ObjectType.equalsIgnoreCase("StockCategoriesTable"))
					{
						FunctionLibrary.StockCategoriesTable(driver, TestData);
						test.log(LogStatus.INFO, Descrition);
					}
					//write as pass into status cell in TCModule
					xl.setCellData(TCModule, j, 5, "Pass", outputpath);
					test.log(LogStatus.PASS, Descrition);
					moduleStatus="True";
				}catch(Exception e)
				{
					System.out.println(e.getMessage());
					//write as fail into status cell in TCModule
					xl.setCellData(TCModule, j, 5, "Fail", outputpath);
					test.log(LogStatus.FAIL, Descrition);
					moduleStatus="False";
				}
				if(moduleStatus.equalsIgnoreCase("True"))
				{
					xl.setCellData("MasterTestCases", i, 3, "Pass", outputpath);
				}
				if(moduleStatus.equalsIgnoreCase("False"))
				{
					xl.setCellData("MasterTestCases", i, 3, "Fail", outputpath);
				}
			}
			report.endTest(test);
			report.flush();
		}
		else
		{
			//write as blocked which are flag to N
			xl.setCellData("MasterTestCases", i, 3, "Blocked", outputpath);
		}
		
		
		
	}
}
}
