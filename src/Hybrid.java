import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class Hybrid extends Extents {
	By loginID= By.id("WC_UserRegistrationAddForm_FormInput_logonId_In_Register_1_1");
	By password=By.id("WC_UserRegistrationAddForm_FormInput_logonPassword_In_Register_1");
	By verifyPassword=By.id("WC_UserRegistrationAddForm_FormInput_logonPasswordVerify_In_Register_1");
	By firstName=By.id("WC_UserRegistrationAddForm_NameEntryForm_FormInput_firstName_1");
	By lastName=By.id("WC_UserRegistrationAddForm_NameEntryForm_FormInput_lastName_1");
	By street=By.id("WC_UserRegistrationAddForm_AddressEntryForm_FormInput_address1_1");
	By city=By.id("WC_UserRegistrationAddForm_AddressEntryForm_FormInput_city_1");
	By country=By.id("WC_UserRegistrationAddForm_AddressEntryForm_FormInput_country_1");
	By state=By.id("WC_UserRegistrationAddForm_AddressEntryForm_FormInput_state_1");
	By zipcode=By.id("WC_UserRegistrationAddForm_AddressEntryForm_FormInput_zipCode_1");
	By email=By.id("WC_UserRegistrationAddForm_FormInput_email1_In_Register_1");
	By phone=By.id("WC_UserRegistrationAddForm_FormInput_phoneNum_In_Register_1");
	By currency=By.id("WC_UserRegistrationAddForm_FormInput_preferredCurrency_In_Register_1");
	By gender=By.id("WC_UserRegistrationAddForm_FormInput_gender_In_Register_1");
	By year=By.id("WC_PersonalInfoExtension_birth_year");
	By month=By.id("WC_PersonalInfoExtension_birth_month");
	By date=By.id("WC_PersonalInfoExtension_birth_date");
	By countryPhone=By.id("WC_PersonalInfoExtension_mobile_div_Country_dropdown");
	By phoneNum=By.id("WC_PersonalInfoExtension_mobile_div_12");
	By submit=By.xpath(".//*[@id='WC_UserRegistrationAddForm_links_1']/div[2]");
	
	WebDriver driver = new FirefoxDriver();
	boolean res;
	ReadProp rp= new ReadProp();
	
	
	
@Test(dataProvider="login")
public void testing(String userID, String pass,String verifypass, String first, String last, String streetname, String cityName, String countryName, String stateName, String pincode, String gmail, String phne, String money, String gend, String y,String m, String d, String cp, String pn, int row){
	
	try{
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    String baseUrl = "http://mssintegration1.miraclesoft.com/webapp/wcs/stores/servlet/en/aurora";
    driver.get(baseUrl);
    driver.manage().window().maximize();
    try{
    WebElement signin=driver.findElement(By.id("SignInLink"));
    highlight(signin);
    signin.click();
    Thread.sleep(5000);
    }
    catch(Exception e){
    	driver.findElement(By.id("SignInLink")).click();
    }
    try{
    WebElement register=driver.findElement(By.xpath(".//*[@id='WC_AccountDisplay_links_3']/div[2]"));
    highlight(register);
    register.click();
    Thread.sleep(5000);
    }
    catch(Exception e){
    	driver.findElement(By.xpath(".//*[@id='WC_AccountDisplay_links_3']/div[2]")).click();
    }
    fillingForm("SENDKEYS",loginID,userID);
    fillingForm("SENDKEYS",password,pass);
    fillingForm("SENDKEYS",verifyPassword,verifypass);
    fillingForm("SENDKEYS",firstName,first);
    fillingForm("SENDKEYS",lastName,last);
    fillingForm("SENDKEYS",street,streetname);
    fillingForm("SENDKEYS",city,cityName);
    fillingForm("SELECT",country,countryName);
    fillingForm("SENDKEYS",state,stateName);
    fillingForm("SENDKEYS",zipcode,pincode);
    fillingForm("SENDKEYS",email,gmail);
    fillingForm("SENDKEYS",phone,phne);
    fillingForm("SELECT",currency,money);
    fillingForm("SELECT",gender,gend);
    fillingForm("SELECT",year,y);
    fillingForm("SELECT",month,m);
    fillingForm("SELECT",date,d);
    fillingForm("SELECT",countryPhone,cp);
    fillingForm("SENDKEYS",phoneNum,pn);
    
    WebElement click= driver.findElement(submit);
    highlight(click);
    click.click();
    Thread.sleep(3000);
    
    String actual=driver.getTitle();
    System.out.println(actual);
    
    if(!(actual.contentEquals("My Account"))){
    	res=false;
    	WriteExcelSheet wes;
    	wes=new WriteExcelSheet(rp.getPath().getProperty("path"));
    	wes.writeData(rp.getPath().getProperty("registration"), row, 19, "Fail", res);
    	
    }
    else{
    	res=true;
    	WriteExcelSheet wespass;
    	wespass=new WriteExcelSheet(rp.getPath().getProperty("path"));
    	wespass.writeData(rp.getPath().getProperty("registration"), row, 19, "Pass", res);
    	int i=0;
		
		Properties prop=rp.getPath();
		
		ReadExcelSheet er=new ReadExcelSheet(rp.getPath().getProperty("path"));
		GettingData gd= new GettingData(driver);
		String scenarios=rp.getPath().getProperty("mainsheet");
		System.out.println(scenarios);
		System.out.println("count: "+er.count(scenarios));
		er.removeCells(scenarios,3);
		for(int j=1;j<=er.count(scenarios);j++){
			if(er.readData(scenarios, j, 2).equals("Y")){
			er.removeCells(er.readData(scenarios, j, 0),5);
			}
		}
		for(int k=1;k<=er.count(scenarios);k++){
			String required=er.readData(scenarios, k, 2);
			String tc=er.readData(scenarios, k, 1);
			if(required.equals("Y")){
				String sh=er.readData(scenarios, k, 0);
				System.out.println(sh);
		test=er1.startTest(tc+" -- "+sh);
		int count= er.count(sh);
		WriteExcelSheet wesheet = new WriteExcelSheet(prop.getProperty("path"));
		wesheet.writeData(scenarios,k,3,"Pass",true);
		
		for(i=1;i<=count;i++){
			Thread.sleep(500);
			gd.operation(prop, er.readData(sh, i, 0) ,er.readData(sh, i, 1), er.readData(sh, i, 2), er.readData(sh, i, 3), er.readData(sh, i, 4),er.readData(sh, i, 5),er.readData(sh, i, 6), i,sh,k,"Firefox");
			
			if(er.readData(sh, i, 2).equals("Verify")){
				String verify=driver.findElement(By.xpath(".//*[@id='OrderStatusDetailsDisplayExt_status_1']/span")).getText();
				if(verify.equals("Order received and ready for processing")){
					WriteExcelSheet wespass1;
			    	wespass1=new WriteExcelSheet(rp.getPath().getProperty("path"));
			    	wespass1.writeData(rp.getPath().getProperty("registration"), row, 20, "Pass", true);
				}
				else{
					WriteExcelSheet wespass2;
			    	wespass2=new WriteExcelSheet(rp.getPath().getProperty("path"));
			    	wespass2.writeData(rp.getPath().getProperty("registration"), row, 20, "Fail", false);
				}
			}
		}
			}
		endExtent();
		}
		System.out.println("Test execution is completed");
		
    
	}
	}
    catch(Exception e){
    	e.printStackTrace();
    }
	}


public void fillingForm(String key, By element, String value) {
	try {
		switch(key){
		case "SENDKEYS":
			WebElement we=driver.findElement(element);
			highlight(we);
			we.sendKeys(value);
			break;
		case "SELECT":
			WebElement ele=driver.findElement(element);
			highlight(ele);
			Select sel=new Select(ele);

			value= value.replace("_", " ");
			sel.selectByVisibleText(value.trim());
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	
}
@AfterTest
public  void closing(){
	driver.quit();
	driver= new FirefoxDriver();
	driver.manage().window().maximize();
	driver.get(folder);
	try {
		Properties prop=rp.getPath();
		Desktop.getDesktop().open(new File(prop.getProperty("path")));
	} catch (Exception e) {
		e.printStackTrace();
	}
}	

public void highlight(WebElement element1) throws Exception {
	JavascriptExecutor js = (JavascriptExecutor) driver;
	js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red');",element1);
	Thread.sleep(500);
	js.executeScript("arguments[0].setAttribute('style','border: solid 2px white');",element1);
	
}
public void mark(WebElement element1) throws Exception{
	JavascriptExecutor js = (JavascriptExecutor) driver;
	js.executeScript("arguments[0].setAttribute('style','border: solid 2px red');",element1);
}

@DataProvider(name="login")
public Object[][] passdata() throws Exception{
	String path=rp.getPath().getProperty("path");
	ReadExcelSheet er= new ReadExcelSheet(path);
	int rows=er.count(rp.getPath().getProperty("registration"));
	System.out.println(rows);
	Object users[][]=new Object[rows][20];
	for(int i=1;i<=rows;i++){
		for(int j=0;j<19;j++){
	users[i-1][j]=er.readData(rp.getPath().getProperty("registration"), i, j);
	System.out.println(users[i-1][j]);
	
	}
		users[i-1][19]=i;
		System.out.println(users[i-1][19]);
	}
	return users;
}

}
