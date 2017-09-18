import java.awt.Desktop;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.server.browserlaunchers.Sleeper;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;

import com.google.common.base.Function;
import com.relevantcodes.extentreports.LogLevel;
import com.relevantcodes.extentreports.LogStatus;



public class GettingData extends Extents {
WebDriver driver;
boolean cond= false;
int testsheet=8, mainsheet=3;
public GettingData(WebDriver driver){
	this.driver=driver;
}
ReadProp rp= new ReadProp();	// calling ReadProp class

public void operation(Properties p,String stepNumber, String keyword, String description, String objectName, String objectType, String value,String expected,int row, String sh, int resultRow,String driverName ) {
	ReadExcelSheet re=new ReadExcelSheet(p.getProperty("path"));
	try{
		Robot robot=new Robot();
		Actions act=new Actions(driver);
		JavascriptExecutor js1=(JavascriptExecutor) driver;
		Random random= new Random();
	switch(keyword.toUpperCase()){
	case "GOTOURL":										// for run the url
		driver.get(p.getProperty(objectName));
		String title_name=driver.getTitle();
		waitforloading();
		String actual="Problem loading page";;
		if(title_name.equals(actual.trim())){
			cond= true;
			setData(cond,p,row,sh,resultRow,driverName);
			test.log(LogStatus.FAIL, stepNumber,"URL is not running. Please run again");	// for failure purpose
		}
		else{
			cond= true;
			setData(cond,p,row,sh,resultRow,driverName);
			test.log(LogStatus.PASS, stepNumber,"Description: "+description+" --- Output: "+expected);				// for pass purpose
		}
		break;
		
	case "WRITETEXT":									// for typing in application
		try{
		WebElement writetext=driver.findElement(this.getObject(objectName,objectType));
		highlight(writetext);							// highlighting specific element
		fluentWait(this.getObject(objectName,objectType)).clear();
		fluentWait(this.getObject(objectName,objectType)).sendKeys(value);
		}
		catch(Exception wr){
			fluentWait(this.getObject(objectName,objectType)).clear();
			fluentWait(this.getObject(objectName,objectType)).sendKeys(value);
		}
		cond= true;
		setData(cond,p,row,sh,resultRow,driverName);
		test.log(LogStatus.PASS, stepNumber,"Description: "+description+" --- Output: "+expected);
		break;
		
	case "CLICK":										// for clicking on any element
		WebElement click=fluentWait(this.getObject(objectName,objectType));
		highlight(click);
		Thread.sleep(500);
		fluentWait(this.getObject(objectName,objectType)).click();
		waitforloading();
		cond= true;
		setData(cond,p,row,sh,resultRow,driverName);
		test.log(LogStatus.PASS, stepNumber,"Description: "+description+" --- Output: "+expected);
		break;
		
	case "OPTIONAL":
		try{
		WebElement op=driver.findElement(this.getObject(objectName,objectType));
		highlight(op);
		Thread.sleep(500);
		op.click();
		waitforloading();
		cond= true;
		setData(cond,p,row,sh,resultRow,driverName);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		break;
		
	case "PRESS":
		WebElement press=fluentWait(this.getObject(objectName,objectType));
		highlight(press);
		js1.executeScript("arguments[0].click();", press);
		waitforloading();
		cond= true;
		setData(cond,p,row,sh,resultRow,driverName);
		test.log(LogStatus.PASS, stepNumber,"Description: "+description+"\nOutput: "+expected);
		break;
		
	case "RANDOM CLICK":
		List<WebElement> rclick=driver.findElements(this.getObject(objectName, objectType));
		int randnumb=random.nextInt(rclick.size());
		WebElement webele=rclick.get(randnumb);
		highlight(webele);
		webele.click();
		cond= true;
		setData(cond,p,row,sh,resultRow,driverName);
		test.log(LogStatus.PASS, stepNumber,"Description: "+description+" --- Output: "+expected);
		break;
		
	case "CHECKBOX":								// for clicking on checkboxes
		WebElement checkbox=driver.findElement(this.getObject(objectName,objectType));
		highlight(checkbox);
		if(!checkbox.isSelected()){					// for select the checkbox
			checkbox.click();
			waitforloading();
			cond= true;
			setData(cond,p,row,sh,resultRow,driverName);
			test.log(LogStatus.PASS, stepNumber,"Description: "+description+"\nOutput: "+expected);
		}
		else if(value.equalsIgnoreCase("uncheck")&&checkbox.isSelected()){		// for uncheck checkbox
			checkbox.click();
			waitforloading();
			cond= true;
			setData(cond,p,row,sh,resultRow,driverName);
			test.log(LogStatus.PASS, stepNumber,"Description: "+description+"\nOutput: "+expected);
		}
		
		break;
		
	case "POPUP":									// for going to other window
		int popup_number=Integer.parseInt(value);
		popups(driver,popup_number);				// for calling popup method
		waitforloading();
		cond= true;
		setData(cond,p,row,sh,resultRow,driverName);
		test.log(LogStatus.PASS, stepNumber, "Description: "+description+" --- Output: "+expected);
		break;
		
	case "UNPOPUP":									// for close the current window
		Thread.sleep(500);
		int newpopup=Integer.parseInt(value);
		driver.close();
		popups(driver,newpopup);
		waitforloading();
		cond= true;
		setData(cond,p,row,sh,resultRow,driverName);
		test.log(LogStatus.PASS, stepNumber,"Description: "+description+"\nOutput: "+expected);
		break;
		
	case "SELECT":									// select option from drop down
		System.out.println(value);
		WebElement selected=driver.findElement(this.getObject(objectName,objectType));
		highlight(selected);
		Select sel=new Select(selected);
		value=value.replace("_", " ");
		if(!sel.isMultiple()){
	    sel.selectByVisibleText(value.trim());
		}
		else{
			String multiple[]= value.split(",");
			for(int b=0;b<multiple.length;b++){
				sel.selectByVisibleText(multiple[b].trim());
			}
		}
		waitforloading();
		cond= true;
		setData(cond,p,row,sh,resultRow,driverName);
		test.log(LogStatus.PASS, stepNumber,"Description: "+description+" --- Output: "+expected);
		break;
		
	case "OPTION FIELDS":
		String fields=p.getProperty(objectName);
		fields=fields.replace("--", value);
		WebElement options=driver.findElement(By.xpath(fields));
		highlight(options);
		options.click();
		waitforloading();
		cond= true;
		setData(cond,p,row,sh,resultRow,driverName);
		test.log(LogStatus.PASS, stepNumber,"Description: "+description+"\nOutput: "+expected);
		break;
		
	case "CALENDAR":
		WebElement calendar=driver.findElement(this.getObject(objectName,objectType));
		highlight(calendar);
		js1.executeScript("arguments[0].value='"+value+"';", calendar);
		cond= true;
		setData(cond,p,row,sh,resultRow,driverName);
		test.log(LogStatus.PASS, stepNumber,"Description: "+description+"\nOutput: "+expected);
		break;
		
	case "REPEAT":
		int number=Integer.parseInt(value);
		WebElement records=fluentWait(this.getObject(objectName,objectType));
		for(int k=1;k<=number;k++){
			if(records.isDisplayed() && records.isEnabled()){
				highlight(records);
				records.click();
				cond=true;
			}
			else{
				break;
			}
		}
			if(cond){
				test.log(LogStatus.PASS, stepNumber,"Description: "+description+" --- Output: "+expected);
			}
			else{
				test.log(LogStatus.PASS, stepNumber,"Description: "+description+" --- Output: WebElement is in disable state");
			}
			setData(cond,p,row,sh,resultRow,driverName);
		
		break;
		
	case "SIZE":
		if(!(objectType.equals("") || objectType.equals("empty"))){
			objectName=p.getProperty(objectName);
		String multi[]=objectName.split("--");
		if(objectName.contains("select")){
		objectName=multi[0]+objectType+multi[1];
		System.out.println(objectType);
		By selector=By.xpath(objectName);
		if(isElementPresent(selector)){
		WebElement selection=driver.findElement(By.xpath(objectName));
		highlight(selection);
		Select s1=new Select(selection);
		List<WebElement> size=s1.getOptions();
		System.out.println("Total Options are: "+size.size());
		System.out.println(size.get(0).getText()+" "+size.get(2).getText());
		for(int d=0;d<=size.size();d++){
			Random rand=new Random();
			int num=rand.nextInt(size.size());
			if(!(size.get(num).getText().equalsIgnoreCase("Select"))){
				s1.selectByVisibleText(size.get(num).getText());
				cond= true;
				setData(cond,p,row,sh,resultRow,driverName);
				test.log(LogStatus.PASS, stepNumber, "Size is selected - "+size.get(num).getText());
				break;
			}
			else{
				d--;
			}
		}
		}
		}
		else{
			String sizes[]=value.split(",");
			for(int rep=0;rep<sizes.length;rep++){
			objectName=multi[0]+sizes[rep]+multi[1];
			By cloths=By.xpath(objectName);
			if(isElementPresent(cloths)){
				WebElement clothsize=driver.findElement(cloths);
				if(clothsize.isEnabled()){
					highlight(clothsize);
					clothsize.click();
					cond= true;
					setData(cond,p,row,sh,resultRow,driverName);
					test.log(LogStatus.PASS, stepNumber, "Size is selected - "+sizes[rep]);
					break;
				}
			}
			}
		}
		}
		break;
		
	case "ALERT":									// for accept, close or write anything in alert boxes 
		Thread.sleep(2000);
		String alertdata=driver.switchTo().alert().getText();
		if(value.trim().equalsIgnoreCase("ok")){
			driver.switchTo().alert().accept();
			test.log(LogStatus.PASS, stepNumber, "Showing data: "+alertdata+". It accepted");
		}
		else if(value.trim().equalsIgnoreCase("cancel")){
			driver.switchTo().alert().dismiss();
			test.log(LogStatus.PASS, stepNumber, "Showing data: "+alertdata+". It canceled");
		}
		else{
			driver.switchTo().alert().sendKeys(value);
			driver.switchTo().alert().accept();
			test.log(LogStatus.PASS, stepNumber, "Showing data: "+alertdata+". Entered Text is "+value);
		}
		cond= true;
		setData(cond,p,row,sh,resultRow,driverName);
		
		break;
		
	case "VERIFY TEXT":								// for verifying text in application
		int count=0;
		String data="";
		String[] onames=objectName.split(";");
		String[] otypes=objectType.split(";");
		String text[]=value.split(";");
		for(int i=0;i<onames.length;i++){
			try{
		WebElement verifyPass=fluentWait(this.getObject(onames[i],otypes[i]));
		highlight(verifyPass);
		String actual_text=verifyPass.getText().trim();
		text[i]=text[i].replace("_", " ");
		if(actual_text.contains(text[i].trim())){
			mark(verifyPass);
			data=data+actual_text+"\n";
			count++;
		}
		else{
			if(isElementPresent(this.getObject(onames[i],otypes[i])))
			mark(verifyPass);
		}
			}catch(Exception vr){
				vr.printStackTrace();
			}
		}
		if(count==onames.length){
			cond= true;
			setData(cond,p,row,sh,resultRow,driverName);
			demand(stepNumber,"Web page is showing "+data,"pass");
		}
		else{
			cond= false;
			setData(cond,p,row,sh,resultRow,driverName);
			demand(stepNumber,"Web page is showing "+data,"fail");
		}
		for(int j=0;j<onames.length;j++){
			try{
				if(isElementPresent(this.getObject(onames[j],otypes[j]))){
			WebElement verify=fluentWait(this.getObject(onames[j],otypes[j]));
			nomark(verify);
				}
			}
			catch(Exception vr){
				vr.printStackTrace();
			}
		}
		break;
		
	case "NEGATIVE VERIFY":
		By neg=this.getObject(objectName, objectType);
		if(isElementPresent(neg)){
			WebElement negative=driver.findElement(neg);
			String negmsg=negative.getText();
			setData(false,p,row,sh,resultRow,driverName);
			mark(negative);
			demand(stepNumber,"Web page is showing "+negmsg,"fail");
			nomark(negative);
		}
		else{
			cond=true;
			test.log(LogStatus.PASS, stepNumber, "Verification is passed");
			setData(cond,p,row,sh,resultRow,driverName);
		}
		
		break;
	
	case "ACTION":										// for mousehover on element
		Actions action = new Actions(driver);
	    WebElement we = driver.findElement(this.getObject(objectName,objectType));
	    highlight(we);
	    action.moveToElement(we).build().perform();
		cond= true;
		setData(cond,p,row,sh,resultRow,driverName);
		if(value.equalsIgnoreCase("Screen")){
			mark(we);
			demand(stepNumber,"Mouse over on "+"Description: "+description+"\nOutput: "+expected,"pass");
			nomark(we);
		}
		else
			test.log(LogStatus.PASS, stepNumber,"Description: "+description+"\nOutput: "+expected);	
	    Thread.sleep(500);
		break;
		
	case "SCROLL":											// scroll to particular element
		JavascriptExecutor js = ((JavascriptExecutor) driver);
	    WebElement wes = driver.findElement(this.getObject(objectName,objectType));
	    js.executeScript("arguments[0].scrollIntoView(true);",wes);
	    highlight(wes);
		cond= true;
		setData(cond,p,row,sh,resultRow,driverName);
		test.log(LogStatus.PASS, stepNumber,"Description: "+description+"\nOutput: "+expected);
		break;
		
	case "SLEEP":											// sleep for sometime
		float changeToFloat=Float.parseFloat(value);
		int changeToInt=(int) changeToFloat;
		Thread.sleep(changeToInt);
		break;
		
	case "WAIT":
		WebElement wait=fluentWait(this.getObject(objectName, objectType), Integer.parseInt(value));
		break;
		
	case "SCREEN ENABLE":									// take the screenshot before clicking on element
		Thread.sleep(2000);
		By parameter=this.getObject(objectName,objectType);
		if(isElementPresent(parameter)){
			WebElement enable = fluentWait(this.getObject(objectName,objectType));
			highlight(enable);
			mark(enable);
			demand(stepNumber,"Description: "+description+" --- Output: "+expected,"pass");
			nomark(enable);
			cond= true;
			setData(cond,p,row,sh,resultRow,driverName);
		}
		else{
			cond= true;
			setData(cond,p,row,sh,resultRow,driverName);
			test.log(LogStatus.PASS, stepNumber, objectName+" is disabled");
		}
		break;
		
	case "ENABLE":										//clicking on element before taking screenshot
		By endproducts=this.getObject(objectName,objectType);
		if(isElementPresent(endproducts)){
			WebElement visible = driver.findElement(this.getObject(objectName,objectType));
			highlight(visible);
			Thread.sleep(500);
			visible.click();
			waitforloading();
			cond=true;
			if(value.equalsIgnoreCase("Screen")){
				mark(visible);
				demand(stepNumber, objectName+" is clicked","pass");
				nomark(visible);
			}
			else
				test.log(LogStatus.PASS, stepNumber, "Description: "+description+"\nOutput: "+expected);	
		}
		else{
			test.log(LogStatus.PASS, stepNumber, objectName+" is disabled");
		}
		setData(cond,p,row,sh,resultRow,driverName);
		break;
		
	case "LIST":											// counting and verifying number of images in one page
		By list=this.getObject(objectName,objectType);
		String other=value.substring(value.indexOf("=")+1,value.indexOf(","));
		String substring=value.substring(0,value.indexOf("="));
		int size=Integer.parseInt(other);
		if(isElementPresent(list)){
			WebElement prodlist = driver.findElement(this.getObject(objectName,objectType));
			List<WebElement> listImages = null;
			listImages=prodlist.findElements(By.tagName(substring));
			int total=listImages.size();
			int images=0;
			String main=value.substring(value.indexOf(",")+1,value.lastIndexOf("="));
			String mn=value.substring(value.lastIndexOf("=")+1,value.lastIndexOf(","));
			int mainNumber=Integer.parseInt(mn);
			if(value.contains(main)){
					listImages=prodlist.findElements(By.tagName(main));
					images=listImages.size();
			}
			if(total/size==images/mainNumber){
			if(value.contains("screen")){
				cond= true;
				setData(cond,p,row,sh,resultRow,driverName);
				mark(prodlist);
				demand(stepNumber, "Number of images are "+total/size, "pass");
				nomark(prodlist);
			}
			else{
				cond= true;
				setData(cond,p,row,sh,resultRow,driverName);
				test.log(LogStatus.PASS, stepNumber, "Number of images are "+total/size);
			}
			}
			else{
				cond= false;
				setData(cond,p,row,sh,resultRow,driverName);
				mark(prodlist);
				demand(stepNumber, "Number of images are "+total/size+" but showing images are "+images/mainNumber, "fail");
				nomark(prodlist);
			}
		}
		else{
			cond= true;
			setData(cond,p,row,sh,resultRow,driverName);
			test.log(LogStatus.PASS, stepNumber, objectName+" is disabled");
		}

		break;
		
	case "COUNT":
		String[] mainObjectName=objectName.split(";");
		String[] mainObjectType=objectType.split(";");
		int countElements=Integer.parseInt(value);
		WebElement parent=driver.findElement(this.getObject(mainObjectName[0],mainObjectType[0]));
		highlight(parent);
		List<WebElement> child=parent.findElements(this.getObject(mainObjectName[1], mainObjectType[1]));
		mark(parent);
		if(countElements==child.size()){
			cond= true;
			setData(cond,p,row,sh,resultRow,driverName);
		demand(stepNumber, "Actual Number of images or links are "+child.size()+" \n Actual and expected images or links are matched", "pass");
		}
		else{
			cond= false;
			setData(cond,p,row,sh,resultRow,driverName);
			demand(stepNumber, "Actaul Number of images or links are "+child.size()+" \n But expected images or links are"+countElements, "fail");
		}
		nomark(parent);
		break;
		
	case "SCROLL END":								// scroll to end of the page for number of times
		float scrollFloat=Float.parseFloat(value);
		int scrollInt=(int) scrollFloat;
		for(int i=0;i<=scrollInt;i++){
		 Actions actions = new Actions(driver);
		 actions.keyDown(Keys.CONTROL).sendKeys(Keys.END).perform();
		 Thread.sleep(1000);
		}
		break;
		
	case "COPY TEXT":
		WebElement copy=driver.findElement(this.getObject(objectName, objectType));
		highlight(copy);
		act.moveToElement(copy).sendKeys(Keys.chord(Keys.CONTROL,"a")).sendKeys(Keys.chord(Keys.CONTROL,"c")).build().perform();
		break;
		
	case "PASTE":
		WebElement paste=driver.findElement(this.getObject(objectName, objectType));
		highlight(paste);
		paste.sendKeys(Keys.chord(Keys.CONTROL,"v"));
		cond= true;
		setData(cond,p,row,sh,resultRow,driverName);
		test.log(LogStatus.PASS, stepNumber,"Description: "+description+"\nOutput: "+expected);
		break;
		
	case "FRAME":									// for frame purpose
		try{
		int frame_size= driver.findElements(By.tagName("iframe")).size();
		System.out.println(frame_size);
		WebElement frame=driver.findElement(this.getObject(objectName,objectType));
		driver.switchTo().frame(frame);
		highlight(frame);
		}
		catch(Exception fr){
			test.log(LogStatus.WARNING, "Warning", "Frame is failed. Please check once");
			new WriteExcelSheet(p.getProperty("path")).writeData(sh, row, 5, "Warning", false); 
		}
		break;
		
	case "MAIN":									// coming from frame
		try{
		driver.switchTo().defaultContent();
		}
		catch(Exception fr){
			test.log(LogStatus.WARNING, "Warning", "Main Frame is failed. Please check once");
			new WriteExcelSheet(p.getProperty("path")).writeData(sh, row, testsheet, "Warning", false); 
		}
		break;
		
	case "DETAILS":
		WebElement details=fluentWait(this.getObject(objectName, objectType));
		Point point= details.getLocation();
		int x=point.getX();
		int y=point.getY();
		Dimension dimension=details.getSize();
		int height=dimension.getHeight();
		int width=dimension.getWidth();
		String info="Element is in (x,y) position: ("+x+","+y+")\n"+"Height of the Element:"+height+"\nWidth of the Element:"+width;
		test.log(LogStatus.INFO, "Information", info);
		new WriteExcelSheet(p.getProperty("path")).writeData(sh, row, testsheet-1, info, false);
		new WriteExcelSheet(p.getProperty("path")).writeData(sh, row, testsheet, "INFO", false);
		break;
		
	case "MENU":
		cond=false;
		List<WebElement> menulist=driver.findElements(this.getObject(objectName, objectType));
		for(WebElement ele:menulist){
			if(ele.getText().contains(value)){
				ele.click();
				cond= true;
				setData(cond,p,row,sh,resultRow,driverName);
				test.log(LogStatus.PASS, stepNumber, "Description: "+description+"\nOutput: "+expected);
				break;
			}
		}
		if(!cond){
			cond= false;
			setData(cond,p,row,sh,resultRow,driverName);
			test.log(LogStatus.FAIL, stepNumber, "Description: "+description+"\nOutput: "+expected);
		}
		break;
		
	case "UPLOAD":									// upload a file
		WebElement upload=driver.findElement(this.getObject(objectName,objectType));
		highlight(upload);
		fluentWait(this.getObject(objectName,objectType)).click();
	    StringSelection sel1 = new StringSelection(value);
	    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel1,null);
	    System.out.println("selection" +sel1);
	     robot.setAutoDelay(2000);
		 robot.keyPress(KeyEvent.VK_CONTROL);
		 robot.keyPress(KeyEvent.VK_V);
		 robot.keyRelease(KeyEvent.VK_V);
		 robot.keyRelease(KeyEvent.VK_CONTROL);
		 Thread.sleep(2500);

		 robot.keyPress(KeyEvent.VK_ENTER);
		 robot.keyRelease(KeyEvent.VK_ENTER);
		 cond= true;
			setData(cond,p,row,sh,resultRow,driverName);
			test.log(LogStatus.PASS, stepNumber, "Description: "+description+" --- Output: "+expected);
		break;
	
	case "DOWNLOAD":							// download a file
		WebElement download=driver.findElement(this.getObject(objectName,objectType));
		highlight(download);
		fluentWait(this.getObject(objectName,objectType)).click();
		if(driverName.equalsIgnoreCase("firefox")){
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		}
		cond= true;
		setData(cond,p,row,sh,resultRow,driverName);
		test.log(LogStatus.PASS, stepNumber, "Description: "+description+" --- Output: "+expected);
		break;
		
	case "FIND":
		WebElement find=driver.findElement(this.getObject(objectName,objectType));
		highlight(find);
		Thread.sleep(500);
		fluentWait(this.getObject(objectName,objectType)).click();
		row=row-1;
		finding(p,re.readData(sh,  row, 0),re.readData(sh,  row, 1),re.readData(sh,  row, 2),re.readData(sh,  row, 3),re.readData(sh,  row, 4),re.readData(sh,  row, 5),re.readData(sh,  row, 6),row,sh,resultRow,driverName);
		break;
		
	case "TAB":							// for new, shift or close, open with new tab
		robot.keyPress(KeyEvent.VK_CONTROL);
		if(value.trim().equalsIgnoreCase("w")){
			robot.keyPress(KeyEvent.VK_W);
			robot.keyRelease(KeyEvent.VK_W);
		}
		else if(value.trim().equalsIgnoreCase("t")){
			robot.keyPress(KeyEvent.VK_T);
			robot.keyRelease(KeyEvent.VK_T);
		}
		else if(value.trim().equalsIgnoreCase("tab")){
			robot.keyPress(KeyEvent.VK_TAB);
			robot.keyRelease(KeyEvent.VK_TAB);
		}
		robot.keyRelease(KeyEvent.VK_CONTROL);
		if(value.trim().equalsIgnoreCase("link")){
			WebElement rightClick= fluentWait(this.getObject(objectName,objectType));
			highlight(rightClick);
			Actions act1= new Actions(driver);
			act1.contextClick(rightClick).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).build().perform();
			cond= true;
			setData(cond,p,row,sh,resultRow,driverName);
			test.log(LogStatus.PASS, stepNumber,"Description: "+description+"\nOutput: "+expected);
		}
		break;
		
		default:
			String s= p.getProperty("path");
			WriteExcelSheet writeforInvalid = new WriteExcelSheet(s);
			writeforInvalid.writeData(sh,row,testsheet,"Invalid Keyword",false);
			writeforInvalid.writeData(p.getProperty("mainsheet"),resultRow,3,"Invalid Keywords are mentioned",false);
			System.out.println("It is wrong keyword. Please check in Excel sheet");
			driver.quit();
			try {
				Desktop.getDesktop().open(new File(p.getProperty("path")));
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.exit(0);
			break;
	}
	}
	catch(NoSuchElementException ne){
		if(re.readData(sh, row+1, 1).equals("FIND")){
			row=row+1;
			finding(p,re.readData(sh,  row, 0),re.readData(sh,  row, 1),re.readData(sh,  row, 2),re.readData(sh,  row, 3),re.readData(sh,  row, 4),re.readData(sh,  row, 5),re.readData(sh,  row, 6),row,sh,resultRow,driverName);
		}
		else{
		cond=false;
		setData(cond,p,row,sh,resultRow, driverName);
		try {
			mark(driver.findElement(this.getObject(objectName,objectType)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		demand(stepNumber, objectName+" is failed.","fail");
		try {
			nomark(driver.findElement(this.getObject(objectName,objectType)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		ne.printStackTrace();
	}
	}
	catch(Exception e){
		cond=false;
		setData(cond,p,row,sh,resultRow,driverName);
		demand(stepNumber, objectName+" is failed.","fail");
		e.printStackTrace();
	}
}
private By getObject(String objectName, String objectType) throws Exception {	
	Properties p=rp.getPath();	
	if(objectType.equalsIgnoreCase("ID")){
		return By.id(p.getProperty(objectName));
	}
	else if(objectType.equalsIgnoreCase("LINK")){
		return By.linkText(p.getProperty(objectName));
	}
	else if(objectType.equalsIgnoreCase("CLASS")){
		return By.className(p.getProperty(objectName));
	}
	else if(objectType.equalsIgnoreCase("XPATH")){
		return By.xpath(p.getProperty(objectName));
	}
	else if(objectType.equalsIgnoreCase("CSS")){
		return By.cssSelector(p.getProperty(objectName));
	}
	else if(objectType.equalsIgnoreCase("NAME")){
		return By.name(p.getProperty(objectName));
	}
	else if(objectType.equalsIgnoreCase("TAGNAME")){
		return By.tagName(p.getProperty(objectName));
	}
	return null;
}
public void finding(Properties p,String stepNumber, String keyword, String description, String objectName, String objectType, String value,String expected,int row, String sh, int resultRow,String driverName){
	operation(p,stepNumber,keyword,description,objectName,objectType,value,expected,row,sh,resultRow,driverName);
}
public void popups(WebDriver driver,int window){						// for popup or unpopup
	ArrayList<String> wh=new ArrayList<String>(driver.getWindowHandles());
	driver.switchTo().window(wh.get(window));
	driver.manage().window().maximize();
	System.out.println(driver.getTitle());
}
public WebElement fluentWait(final By locator){
    Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
        .withTimeout(10, TimeUnit.SECONDS)
        .pollingEvery(1000, TimeUnit.MILLISECONDS)
        .ignoring(NoSuchElementException.class)
        .ignoring(WebDriverException.class)
        .ignoring(ElementNotVisibleException.class);

    WebElement foo = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(locator);
            }
        }
    );
    return foo;
}

public WebElement fluentWait(final By locator,int time){
    Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
        .withTimeout(time, TimeUnit.MILLISECONDS)
        .pollingEvery(1000, TimeUnit.MILLISECONDS)
        .ignoring(NoSuchElementException.class)
        .ignoring(WebDriverException.class)
        .ignoring(ElementNotVisibleException.class);

    WebElement foo = wait.until(new Function<WebDriver, WebElement>() {
            public WebElement apply(WebDriver driver) {
                return driver.findElement(locator);
            }
        }
    );
    return foo;
}

public void setData(boolean condition,Properties p,int j, String sheetindex,int resultRow,String browser){ // for writing into excel sheet
	String s= p.getProperty("path");
	WriteExcelSheet wes = new WriteExcelSheet(s);
	int actual=7;
		testsheet=8;
		mainsheet=3;
	
if(condition){
	System.out.println("Row:  "+j);
	wes.writeData(sheetindex,j,testsheet,"Pass",condition);
	wes.writeData(sheetindex,j,actual,"Output as expected",condition);
}
	else{
		try {
			String scenarios=new ReadProp().getPath().getProperty("mainsheet");
		
		wes.writeData(sheetindex,j,testsheet,"Fail",condition);
		wes.writeData(sheetindex,j,actual,"User is unable to perform operation on that element",condition);
		wes.writeData(scenarios,resultRow,mainsheet,"Fail",condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


public void highlight(WebElement element1) throws Exception {		//highlighting element
	JavascriptExecutor js = (JavascriptExecutor) driver;
	js.executeScript("arguments[0].setAttribute('style', 'background: yellow; border: 2px solid red');",element1);
	Thread.sleep(500);
	js.executeScript("arguments[0].setAttribute('style','border: solid 2px white');",element1);
}

public void mark(WebElement element1) throws Exception{				// mark the element
	JavascriptExecutor js = (JavascriptExecutor) driver;
	js.executeScript("arguments[0].setAttribute('style','border: solid 2px red');",element1);
}

public void nomark(WebElement element1) throws Exception{			//unmark the element
	JavascriptExecutor js = (JavascriptExecutor) driver;
	js.executeScript("arguments[0].setAttribute('style','border: none');",element1);
}

public boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

public void demand(String step,String any,String result,String... s){			// report and screenshot purpose
	String img;
	if(isAlertBoxPresent()){
		ScreenshotwithURL scurl=new ScreenshotwithURL(driver, new Timing().timeform());
		img=test.addScreenCapture(scurl.imagepath);
	}
	else{
	ScreenshotPage sc=new ScreenshotPage(driver, new Timing().timeform());
	img = test.addScreenCapture(sc.imagepath);
	}
	switch(result.toUpperCase()){
	case "PASS":
		test.log(LogStatus.PASS, step, any+" The page screenshot: \n"+img );
	break;
	case "FAIL":
		test.log(LogStatus.FAIL, step, any+" The page screenshot: \n"+img );
	break;
	case "WARNING":
		test.log(LogStatus.WARNING, step, any+" The page screenshot: \n"+img );
	break;
	case "INFO":
		test.log(LogStatus.INFO, step, any+" The page screenshot: \n"+img );
	break;
	}
}

public void demand(WebElement element,String step,String any,String result) throws Exception{			// report and screenshot purpose
		ScreenshotPage sc=new ScreenshotPage(driver, new Timing().timeform());
		String img = test.addScreenCapture(sc.imagepath);
		AShot a=new AShot();
		Screenshot screen=a.takeScreenshot(driver, element);
		String sub="C:\\testing\\screen\\element "+new Timing().timeform()+".png";
		ImageIO.write(screen.getImage(), "PNG", new File(sub));
		String subimg = test.addScreenCapture(sub);
		switch(result.toUpperCase()){
		case "PASS":
			test.log(LogStatus.PASS, step, any+" The page screenshot: \n"+img +"\n"+subimg);
		break;
		case "FAIL":
			test.log(LogStatus.FAIL, step, any+" The page screenshot: \n"+img +"\n"+subimg);
		break;
		case "WARNING":
			test.log(LogStatus.WARNING, step, any+" The page screenshot: \n"+img +"\n"+subimg);
		break;
		case "INFO":
			test.log(LogStatus.INFO, step, any+" The page screenshot: \n"+img +"\n"+subimg);
		break;
		}
}

public void waitforloading(){
	ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete");
                }
            };
            
		WebDriverWait wait=new WebDriverWait(driver,30);
		wait.until(expectation);
}

public boolean isAlertBoxPresent(){
	try{
		driver.switchTo().alert().accept();
		return true;
	}
	catch(Exception na){
		
		return false;
	}
}

}
