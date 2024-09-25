package RapiFuzz.RapiFuzz;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.github.bonigarcia.wdm.WebDriverManager;

public class UseCase_1and2 {
	
	WebDriver driver;
	ExtentReports report;

	@BeforeTest
	public void setup() {
		System.out.println("starts...");
		
		WebDriverManager.chromedriver().setup();
		driver=new ChromeDriver();
		driver.get("https://erail.in/");
		
		
		
		String reportPath =System.getProperty("user.dir")+"\\Extent_Report\\index.html";
//System.out.println("++++++"+reportPath+"++++++");
		ExtentSparkReporter reporter=new ExtentSparkReporter(reportPath);
		reporter.config().setReportName("Our_Exten_Report");
		reporter.config().setDocumentTitle("Our_Exten_Report_title");
		
		report=new ExtentReports();
		report.attachReporter(reporter);
		
		report.setSystemInfo("Created by", "Karan Chaunwal");
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));//Or (5,TimeUnit.SECONDS)
	}
	
	WebElement from ;
	WebElement option;
	
	@Test(priority=1)
	public void fun1() {
		ExtentTest Test=report.createTest("Test-1 started");
		WebElement from = driver.findElement(By.xpath("//input[@id='txtStationFrom']"));
		from.clear();
//		from.sendKeys("DEL"+Keys.DOWN+Keys.DOWN+Keys.DOWN+Keys.ENTER);
		from.sendKeys("DEL");
		WebElement option=driver.findElement(By.xpath("//div[@title='Delhi Azadpur']"));
		System.out.println( option.getText());
		
		//option.click();
		Test.pass("Test-1 got passed.");
	}
		
		
		
		
		
		@Test(priority=2)
	public void fun2() throws IOException, InterruptedException {
			ExtentTest Test=report.createTest("Test-2 started");
			System.out.println("------------------------reading excel-----------------------------------"); 
			
			//reading expected values
			try {
		FileInputStream inputStreamObj = new FileInputStream(".\\Excels\\Rapifuzz.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook(inputStreamObj);
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		int lastRowno = sheet.getLastRowNum();
		XSSFRow rowObj = sheet.getRow(0);
		
		List<String> ExpectedValues = new ArrayList<String>();
		DataFormatter formatter=new DataFormatter();
		for(int r=0;r<=lastRowno;r++) {
			
			XSSFRow row = sheet.getRow(r);
			
			
				XSSFCell cell = row.getCell(0);
				
				String value = formatter.formatCellValue(cell);
				System.out.println(value);
				ExpectedValues.add(value);
			
		}
		System.out.println("----"+ExpectedValues+"----"); 
//		XSSFWorkbook workbook2=new XSSFWorkbook();
//		XSSFSheet Sheet=workbook.createSheet("Sheet_1");
		
		
		System.out.println("-----------------------------writing excel-----------------------------------"); 
		
		from = driver.findElement(By.xpath("//input[@id='txtStationFrom']"));
		
		from.clear();
		Thread.sleep(2000);
		from.sendKeys("DEL");
		
		WebElement value = driver.findElement(By.xpath("//div[@class='selected']"));
		
		WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10)); 
		
		List<String> dropdownValues = new ArrayList<String>();
		
		int j=1;
		do {
			if(j==1) {
			System.out.println( value.getText());
			dropdownValues.add( value.getText());
			}
			from.sendKeys(Keys.DOWN);
			try {
                Thread.sleep(500);  // Can be replaced with an explicit wait if needed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
			//WebElement value=wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='selected']")));
			value = driver.findElement(By.xpath("//div[@class='selected']"));
			System.out.println(value.getText());
			dropdownValues.add( value.getText());
			
			j++;
		}while(j<=5);
		
		
		for(int r=0;r<dropdownValues.size();r++) {
			XSSFRow row = sheet.createRow(r);
			
				XSSFCell cell = row.createCell(0);
				
				{
					cell.setCellValue(dropdownValues.get(r));
				}
			
		}
		FileOutputStream outputstreamObj=new FileOutputStream(".\\Excels\\RapifuzzValues.xlsx");
		workbook.write(outputstreamObj);
//		workbook.close();
//		outputstreamObj.close();
			
		
		
		System.out.println("-----------------------reading excel2-----------------------------------");
		//read actual vaules
		FileInputStream inputStreamObj2 = new FileInputStream(".\\Excels\\RapifuzzValues.xlsx");
		XSSFWorkbook workbook2 = new XSSFWorkbook(inputStreamObj2);
		XSSFSheet sheet2 = workbook2.getSheetAt(0);
		
		int lastRowno2 = sheet2.getLastRowNum();
		XSSFRow rowObj2 = sheet2.getRow(0);
		
		List<String> ActualValues = new ArrayList<String>();
		DataFormatter formatter2=new DataFormatter();
		for(int r=0;r<=lastRowno2;r++) {
			
			XSSFRow row = sheet.getRow(r);
			
			
				XSSFCell cell = row.getCell(0);
				
				String value2 = formatter2.formatCellValue(cell);
				System.out.println(value2);
				ActualValues.add(value2);
		}
		System.out.println("----"+ActualValues+"----"); 
		
		boolean allMatch = true;

        for (String name : ExpectedValues) {
            boolean found = false;
            for (String fullName : ActualValues) {
                if (fullName.contains(name)) {
                    found = true;
                    break; // No need to check further if found
                }
            }
            if (!found) {
                allMatch = false;
                System.out.println(name + " is not present in ExpectedValues.");
            }
        }
        
	
//		if (ExpectedValues.contains(ActualValues)) {
//            System.out.println("Actual list matches Expected list.");
//        } else {
//            System.out.println("Actual list doesnot matches Expected list.");
//        }
		
		if (allMatch) {
            System.out.println("All names from the ExpectedValues are present in ActualValues list.");
            Test.pass("Test-2 got passed.");
        } else {
            System.out.println("Not all names from the ExpectedValues are present in ActualValues list.");
            Assert.fail();
            Test.fail("Test-2 got failed!");
        }
		
			}catch(Exception e) {
				 Test.fail("Test-2 got failed! : The specified folder or Excel file was not found");
				System.out.println("Error: The specified folder or Excel file was not found.");
				Assert.fail();
			}
	}
		
		
		@Test(priority=3)
		public void fun3() {
			ExtentTest Test=report.createTest("Test-3 started");
	//	WebElement nextArrow = driver.findElement(By.xpath("//tbody/tr[2]/td[4]/a[2]"));
		
		WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
		for(int i=1;i<=30;i++) {
			try {
                
				WebElement nextArrow = wait2.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//tbody/tr[2]/td[4]/a[2]")));

                // Click the next arrow
                nextArrow.click();
              //  System.out.println(i);

             
            } catch (Exception e) {
                System.out.println("Exception occurred: " + e.getMessage());
            }
		}
		Test.pass("Test-3 got passed.");
		setup2();
	}
	   
		
		
		@DataProvider
		public Object[][] data() throws IOException {
	//ExtentTest Test=report.createTest("Test-4 started");
			try {
			FileInputStream inputStreamObj = new FileInputStream(".\\Excels\\Logincred.xlsx");
			XSSFWorkbook workbook = new XSSFWorkbook(inputStreamObj);
			XSSFSheet sheet = workbook.getSheetAt(0);
			int lastRowno = sheet.getLastRowNum();
			XSSFRow rowObj = sheet.getRow(0);
			short lastColumn = rowObj.getLastCellNum();
			DataFormatter formatter=new DataFormatter();
			
			Object[][] data = new Object[lastRowno][2];
			
			for(int r=1;r<=lastRowno;r++) {
				System.out.print("\n");
				XSSFRow row = sheet.getRow(r);
				String username = formatter.formatCellValue(row.getCell(0)); // As username is in the first column
	            String password = formatter.formatCellValue(row.getCell(1)); 
	            data[r - 1][0] = username;
	            data[r - 1][1] = password;
//				for(int c=0;c<lastColumn;c++) {
//					XSSFCell cell = row.getCell(c);
//					
//					String value = formatter.formatCellValue(cell);
//					System.out.print(value);
//					
//					if(c<lastColumn-1)
//				        System.out.print(" | ");
//				        
//				}    
			}
			return data;
			}catch(Exception e) {
		//Test.fail("Test-4 got failed! : The specified folder or Excel file was not found");
				System.out.println("Error: The specified folder or Excel file was not found.");
				report.flush();
				Assert.fail();
				
				return null;
				
			}
			
			
		}
		
	  // @Test(priority=4,dependsOnMethods = {"fun3"})
		public void setup2() {
		   driver.switchTo().newWindow(WindowType.TAB);
			driver.navigate().to("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
		}
		
		@Test(priority=4,dataProvider="data")
		public void fun4(String user, String pass) throws InterruptedException {
			ExtentTest Test=report.createTest("Test-4 started");
//			 Actions action = new Actions(driver);
//			 action.keyDown(Keys.CONTROL).sendKeys("t").perform();
//			 action.keyUp(Keys.CONTROL).perform();
			
//			driver.switchTo().newWindow(WindowType.TAB);
//			driver.navigate().to("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
			
			
			
			WebElement username = driver.findElement(By.xpath("//input[@placeholder='Username']"));
			WebElement password = driver.findElement(By.xpath("//input[@placeholder='Password']"));
			WebElement loginBtn = driver.findElement(By.xpath("//button[@type='submit']"));
		
			username.sendKeys(user);
			password.sendKeys(pass);
			loginBtn.click();
			
			Thread.sleep(1000);
			if(driver.getCurrentUrl().equals("https://opensource-demo.orangehrmlive.com/web/index.php/dashboard/index")){
				System.out.println("User got logged in successfully");
				Test.pass("Test got passed.");
				
			}else {
				try {
					System.out.println("User credentials are not valid");
					Test.fail("Test-4 got failed! : User credentials are not valid");
					Assert.fail();
					
				
				username.clear();
				password.clear();
				
			  
				}
				catch(StaleElementReferenceException e) {
		          //  System.out.println("Stale Element Reference Exception occurred, re-locating the elements");
		            username = driver.findElement(By.xpath("//input[@placeholder='Username']"));
		            password = driver.findElement(By.xpath("//input[@placeholder='Password']"));
		            username.clear();
		            password.clear();
		            }
			}
			report.flush();
		}
	
}
