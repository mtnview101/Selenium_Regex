package core;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;
import java.util.regex.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class Payment {
       static String browser = "Chrome1";  // "HtmlUnit"
       public static void main(String[] args) throws InterruptedException {
              String url = "http://alex.academy/exercises/payment/indexE.html";

              Logger logger = Logger.getLogger("");
              logger.setLevel(Level.OFF);
              
              WebDriver driver;
              final long start = System.currentTimeMillis();           
              if (browser.equalsIgnoreCase("chrome")) {System.setProperty("webdriver.chrome.driver","./src/main/resources/webdrivers/pc/chromedriver.exe");
	              System.setProperty("webdriver.chrome.silentOutput", "true");
	              ChromeOptions option = new ChromeOptions();
	              option.addArguments("-start-fullscreen");
              
	              driver = new ChromeDriver(option);
	              driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); //What is a function of this method????
	              }
              else{driver = new HtmlUnitDriver();((HtmlUnitDriver) driver).setJavascriptEnabled(true);}
              
              driver.get(url);

              String string_monthly_payment = driver.findElement(By.id("id_monthly_payment")).getText();
              System.out.println("string_monthly_payment: "+string_monthly_payment);
              String regex = "^"
                      + "(?:\\$)?"
                      + "(?:\\s*)?"
                      + "((?:\\d{1,3})(?:\\,)?(?:\\d{3})?(?:\\.)?(\\d{0,2})?)" //whole group(1) and group(2)=(\\d{0,2})?
                      + "$";

              Pattern p = Pattern.compile(regex);
              Matcher m = p.matcher(string_monthly_payment);
              m.find(); // "$1,654.55"
              System.out.println("m.group(1): \t" + m.group(1)); 
              System.out.println("m.group(2): \t" + m.group(2));
              // System.out.println("m.group(3): \t" + m.group(3)); //java.lang.IndexOutOfBoundsException: No group 3
              
              double monthly_payment = Double.parseDouble(m.group(1).replaceAll(",", "")); // 1,654.55
              double annual_payment = new BigDecimal(monthly_payment * 12).setScale(2, RoundingMode.HALF_UP).doubleValue(); // 1654.55 * 12 = 19854.6
              DecimalFormat df = new DecimalFormat("0.00");// 19854.60
              String f_annual_payment = df.format(annual_payment);
              
              driver.findElement(By.id("id_annual_payment")).sendKeys(String.valueOf(f_annual_payment));
              driver.findElement(By.id("id_validate_button")).submit();
              
              String actual_result = driver.findElement(By.id("id_result")).getText();
              final long finish = System.currentTimeMillis();
              System.out.println("String: \t" + m.group(0)); 
              /*Capturing groups are indexed from left to right, starting at one. 
              Group zero denotes the entire pattern, so the expression m.group(0) is equivalent to m.group() */
              
              System.out.println("Annual Payment: " + f_annual_payment);
              System.out.println("Result: \t" + actual_result);
              System.out.println("Resonse time: \t" + (finish - start) + " milliseconds");
              Thread.sleep(5000);
              driver.quit(); }}

