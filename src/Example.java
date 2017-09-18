import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


public class Example {
	public static 	 WebDriver driver;
public static void main(String[] args) throws Exception {

ReadProp rp=new ReadProp();
String p=rp.getPath().getProperty("path");
	for(int i=1;i<=10;i++){
		if(rp.getPath().getProperty("mailTo"+i)!=null){
		System.out.println(rp.getPath().getProperty("mailTo"+i));
		}
		else{
			break;
		}
	}
}
}
