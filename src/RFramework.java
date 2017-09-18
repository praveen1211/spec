import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.util.NumberToTextConverter;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class RFramework extends Extents {

	WebDriver driver = browser();
	boolean res;
	ReadProp rp = new ReadProp();
	
	@Test(dataProvider = "login")
	public void parameters(int row, String p1, String p2, String p3, String p4, String p5,String p6, String p7, String p8, String p9,String p10,
						String p11, String p12, String p13, String p14, String p15, String p16, String p17, String p18, String p19, String p20,
						String p21, String p22, String p23, String p24, String p25, String p26, String p27, String p28, String p29, String p30,
						String p31, String p32, String p33, String p34, String p35, String p36, String p37, String p38, String p39) {
	testing(row,p1,p2,p3,p4,p5,p6,p7,p8,p9,p10,p11,p12,p13,p14,p15,p16,p17,p18,p19,p20,p21,p22,p23,p24,p25,p26,p27,p28,p29,p30,p31,p32,p33,p34,p35,p36,p37,p38,p39);
	System.out.println(row+"  "+p1+"  "+p2+" "+p3);
	}

	public WebDriver browser(){
		WebDriver d;
		String browser=browserName();
		if(browser.equalsIgnoreCase("firefox")){
			ProfilesIni listProfiles = new ProfilesIni();   
			FirefoxProfile profile = listProfiles.getProfile("default");   
			d=new FirefoxDriver(profile);
		}
		else if(browser.equalsIgnoreCase("Chrome")){
			System.setProperty("webdriver.chrome.driver", "C:\\Users\\miracle\\git\\VSTSQA\\libs\\chromedriver.exe");
			ChromeOptions options= new ChromeOptions();
			options.addArguments("--disable-extensions");
			options.addArguments("--test-type");
			d= new ChromeDriver(options);
		}
		else{
			d=null;
			System.out.println("browser is incorrect");
		}
		return d;
	}
	
	public String browserName(){
		String name="Firefox";
		return name;
	}
	
	public void testing(int row, String... information) {
		
		try {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.manage().window().maximize();
			ReadExcelSheet excel = new ReadExcelSheet(rp.getPath().getProperty("path"));
			int columnCount=excel.columnCount(rp.getPath().getProperty("registration"));
			System.out.println("Length is "+(information.length+1));
			//for calling locators of usernames and passwords
			parent=er1.startTest(information[0]);
			String datadriven = rp.getPath().getProperty("datadriven");
			System.out.println(datadriven);
			System.out.println("count: " + excel.count(datadriven));
			excel.removeCells(datadriven, 3);
			for (int m = 1; m <= excel.count(datadriven); m++) {
				Properties proper = rp.getPath();
				String requiredcases = excel.readData(datadriven, m, 2);
				String tc = excel.readData(datadriven, m, 1);
				if (requiredcases.equals("Y")) {
					String sh = excel.readData(datadriven, m, 0);
					System.out.println(sh);

					int counting = excel.count(sh);
					WriteExcelSheet wesheet = new WriteExcelSheet(rp.getPath().getProperty("path"));
					wesheet.writeData(datadriven, m, 3, "Pass", true);
					wesheet.writeData(rp.getPath().getProperty("registration"), row, excel.columnCount(rp.getPath().getProperty("registration"))-1, 0, "Warning");
					
					//for executing username and passwords in site
					int r = 0, p = 0;
					for (r = 1; r <= counting; r++) {
						Thread.sleep(500);
						
						if (!excel.readData(sh, r, 1).equalsIgnoreCase("SEARCH")) {
							if (excel.readData(sh, r, 1).equals("WRITETEXT")|| excel.readData(sh, r, 1).equals("SELECT")) {
								fillingForm(proper, excel.readData(sh, r, 1),excel.readData(sh, r, 2),excel.readData(sh, r, 3),information[p]);
								p++;
							} else {
								fillingForm(proper, excel.readData(sh, r, 1),excel.readData(sh, r, 2),excel.readData(sh, r, 3),excel.readData(sh, r, 4));
							}
							if(excel.readData(sh, r, 1).equalsIgnoreCase("GOTOURL")){
								String title_name=driver.getTitle();
								String value="Problem loading page";
								if(title_name.equals(value.trim())){
									wesheet.writeData(sh,r,8,"Fail",false);
									break;
								}
								else
									System.out.println("URL is succussfully running");	
							}
							if(excel.readData(sh, r, 1).equalsIgnoreCase("TITLE")){
								String actual = driver.getTitle();
								String title = rp.getPath().getProperty(excel.readData(sh, r, 2));
								
								if (actual.trim().contentEquals(title.trim())) {
									System.out.println("title is correct");
									wesheet.writeData(rp.getPath().getProperty("registration"),row, columnCount - 2, "Pass", false);
								}
								else{
									System.out.println("title is  not correct");
									wesheet.writeData(rp.getPath().getProperty("registration"),row, columnCount - 2, "Fail", false);
									break;
								}
							}
							
						} 
						
						else {
								Random rand = new Random();
								
								int i = 0;

								Properties prop = rp.getPath();
//								er = new ReadExcelSheet(rp.getPath().getProperty("path"));
								GettingData gd = new GettingData(driver);
								String scenarios = rp.getPath().getProperty("mainsheet");
								System.out.println(scenarios);
								System.out.println("count: "+ excel.count(scenarios));
								int Items = Integer.parseInt(excel.readData(rp.getPath().getProperty("registration"),row, excel.columnCount(rp.getPath().getProperty("registration")) - 3));
								int placedItems=0;
								for (int noOfItems = 1; noOfItems <= Items; noOfItems++) {
									String search = rp.getPath().getProperty("search");
									int searchRecords = excel.count(search);
									int searchItem = rand.nextInt(searchRecords) + 1;
									System.out.println(excel.readData(search, searchItem, 0));
									fillingForm(proper, excel.readData(sh, r, 1),excel.readData(sh, r, 2),excel.readData(sh, r, 3),excel.readData(search, searchItem, 0)+ "\n");
									String category=excel.readData(search, searchItem, 0);
									
									//for number of images in web page
									String startXpath,xpath;
									startXpath=prop.getProperty("totalImagesXpath");
									
									List<WebElement> totalImages=driver.findElements(By.xpath(startXpath));
									
									if(totalImages.size()!=0){
									int image=rand.nextInt(totalImages.size())+1;
									xpath=startXpath+"["+image+"]";
									highlight(driver.findElement(By.xpath(xpath)));
									driver.findElement(By.xpath(xpath)).click();
									
									String product=prop.getProperty("product");
									String productName=driver.findElement(By.xpath(product)).getText();
									excel.removeCells(scenarios, 3);
									
									int testcaseCount=0;
									for (int a = 1; a <= excel.count(scenarios); a++) {
										String required = excel.readData(scenarios, a, 2);
										if (required.equals("Y")) {
											testcaseCount++;
										}
									}
									
									int originalCount=0;
									//operations after login and search box
									for (int k = 1; k <= excel.count(scenarios); k++) {
										String required = excel.readData(scenarios, k, 2);
										if (required.equals("Y")) {
											originalCount++;
											if(Items==noOfItems || k==1){
											tc=excel.readData(scenarios, k, 1);
											String operations = excel.readData(scenarios, k, 0);
											System.out.println(operations);
											if(k==1)
											test = er1.startTest(productName+" in "+category+" Category for "+operations+" TESTCASE -- "+tc );
											else
											test = er1.startTest(operations+" TESTCASE -- "+tc );
												
											int count = excel.count(operations);
											wesheet.writeData(scenarios, k, 3,"Pass", true);

											//operations
											for (i = 1; i <= count; i++) {
												Thread.sleep(500);
												if(i!=count){
													if(excel.readData(operations, i, 1).equalsIgnoreCase("WRITETEXT") || excel.readData(operations, i, 1).equalsIgnoreCase("SELECT")){
														gd.operation(prop,excel.readData(operations, i, 0),excel.readData(operations, i, 1),excel.readData(operations, i, 2),excel.readData(operations, i, 3),excel.readData(operations, i, 4),information[p],excel.readData(operations, i, 6),i, operations, k, browserName());
														p++;
													}
													else if(excel.readData(operations, i, 1).equalsIgnoreCase("SIZE")){
														gd.operation(prop,excel.readData(operations, i, 0),excel.readData(operations, i, 1),excel.readData(operations, i, 2),excel.readData(operations, i, 3),excel.readData(search, searchItem, 1),excel.readData(search, searchItem, 2),excel.readData(operations, i, 6),i, operations, k, browserName());
													}
													else{
													gd.operation(prop,excel.readData(operations, i, 0),excel.readData(operations, i, 1),excel.readData(operations, i, 2),excel.readData(operations, i, 3),excel.readData(operations, i, 4),excel.readData(operations, i, 5),excel.readData(operations, i, 6),i, operations, k, browserName());
													}
												if(excel.readData(operations, i, 1).equals("VERIFY TEXT")){
													String actual_text=driver.findElement(getElement(prop,excel.readData(operations, i, 3),excel.readData(operations, i, 4))).getText();
													String value=excel.readData(operations, i, 5).replace("_", " ");
													System.out.println(actual_text);
													System.out.println(value);
													if(actual_text.trim().contains(value.trim())){
														placedItems++;
													}	
													}
													
												} else if(testcaseCount!=originalCount){
													gd.operation(prop,excel.readData(operations, i, 0),excel.readData(operations, i, 1),excel.readData(operations, i, 2),excel.readData(operations, i, 3),excel.readData(operations, i, 4),excel.readData(operations, i, 5),excel.readData(operations, i, 6),i, operations, k, browserName());
												}
											
											}
											if(Items==noOfItems && testcaseCount==originalCount){
												gd.operation(prop,excel.readData(operations, count, 0),excel.readData(operations, count, 1),excel.readData(operations, count, 2),excel.readData(operations, count, 3),excel.readData(operations, count, 4),excel.readData(operations, count, 5),excel.readData(operations, count, 6),count, operations, k, browserName());
											}
											WriteExcelSheet weproduct= new WriteExcelSheet(rp.getPath().getProperty("path"));
											weproduct.writeData(rp.getPath().getProperty("registration"), row, excel.columnCount(rp.getPath().getProperty("registration"))-1, placedItems, "Warning");
											parent.appendChild(test);
										}
										}
									}
								}
									
									else{
										noOfItems--;
									}
								}
							

						}
					}
					endParentExtent();
				}
				else if(!(requiredcases.equals("")||requiredcases.equals("empty"))){
					String sh=excel.readData(datadriven, m, 0);
					System.out.println(sh);
					System.out.println(requiredcases);
					WriteExcelSheet wedata= new WriteExcelSheet(rp.getPath().getProperty("path"));
					wedata.writeData(datadriven, m, 3, "Invalid Input", false);
				}

			}

			System.out.println("Test execution is completed");

		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fillingForm(Properties p, String key, String objectname,String objecttype, String value) {
		try {
			GettingData g= new GettingData(driver);
			switch (key) {
			case "GOTOURL":
				driver.manage().window().maximize();
				driver.get(p.getProperty(objectname));
				break;
			case "SEARCH":
			case "WRITETEXT":
				WebElement we = driver.findElement(this.getElement(p,objectname, objecttype));
				highlight(we);
				we.clear();
				we.sendKeys(value);
				break;
			case "SELECT":
				WebElement ele = driver.findElement(this.getElement(p,
						objectname, objecttype));
				highlight(ele);
				Select sel = new Select(ele);
				value = value.replace("_", " ");
				sel.selectByVisibleText(value.trim());
				break;
			case "CLICK":
				WebElement click = driver.findElement(this.getElement(p,
						objectname, objecttype));
				highlight(click);
				click.click();
				break;
			case "TITLE":
				String actual = driver.getTitle();
				System.out.println(actual);
				String title = rp.getPath().getProperty("title");
				System.out.println(title);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public By getElement(Properties p, String objectName, String objectType) {
		if (objectType.equalsIgnoreCase("ID")) {
			return By.id(p.getProperty(objectName));
		} else if (objectType.equalsIgnoreCase("LINK")) {
			return By.linkText(p.getProperty(objectName));
		} else if (objectType.equalsIgnoreCase("CLASS")) {
			return By.className(p.getProperty(objectName));
		} else if (objectType.equalsIgnoreCase("XPATH")) {
			return By.xpath(p.getProperty(objectName));
		} else if (objectType.equalsIgnoreCase("CSS")) {
			return By.cssSelector(p.getProperty(objectName));
		} else if (objectType.equalsIgnoreCase("NAME")) {
			return By.name(p.getProperty(objectName));
		}
		return null;

	}

	@AfterTest
	public void closing() {
		driver.quit();
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
		driver.get(folder);
		try {
			Properties prop = rp.getPath();
			Desktop.getDesktop().open(new File(prop.getProperty("path")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void highlight(WebElement element1) throws Exception {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(
				"arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red');",
				element1);
		Thread.sleep(500);
		js.executeScript(
				"arguments[0].setAttribute('style','border: solid 2px white');",
				element1);

	}

	public void mark(WebElement element1) throws Exception {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(
				"arguments[0].setAttribute('style','border: solid 2px red');",
				element1);
	}

	@DataProvider(name = "login")
	public Object[][] passdata() throws Exception {
		String path = rp.getPath().getProperty("path");
		ReadExcelSheet er = new ReadExcelSheet(path);
		int rows = er.count(rp.getPath().getProperty("registration"));
		System.out.println(rows);
		int total = er.columnCount(rp.getPath().getProperty("registration")) - 2;
		System.out.println(total);
		Object users[][] = new Object[rows][40];
		for (int i = 1; i <= rows; i++) {
			users[i - 1][0] = i;
			System.out.println(users[i - 1][0]);
			for (int j = 1; j < total; j++) {
				users[i - 1][j] = er.readData(rp.getPath().getProperty("registration"), i, j-1);
				System.out.println(users[i - 1][j]);
			}
		}
		return users;
	}

}
