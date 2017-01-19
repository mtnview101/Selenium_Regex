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

public class PaymentTax {
       static String browser = "Chrome1";  // "HtmlUnit"
       public static void main(String[] args) throws InterruptedException {
              String url = "http://alex.academy/exercises/payment+tax/indexE.html";

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

              String string_monthly_payment_and_tax = driver.findElement(By.id("id_monthly_payment_and_tax")).getText(); 
              
              System.out.println("string_monthly_payment_and_tax: "+string_monthly_payment_and_tax);
              
              String regex = "^"
              		+ "(?:\\b[A-Z][a-z]+:\\s*)?(?:\\$)?"
              		+ "(?:\\s*)?"
              		+ "((?:\\d{1,3})(?:\\,)?(?:\\d{3})?(?:\\.)?(?:\\d{0,2}))?"//group(1) = monthly_payment
              		+ "(?:,|\\/)?"
              		+ "(?:\\s*)?"
              		+ "(?:\\b[A-Z][a-z]+:\\s*)?"
              		+ "((?:\\d{1,3})?(?:\\d{1,3})?(?:\\.)?(?:\\d{0,2})?)"//group(2) = tax
              		+ "(?:%)?"
              		+ "$";

              Pattern p = Pattern.compile(regex);
              Matcher m = p.matcher(string_monthly_payment_and_tax);
              m.find(); // "$1,654.55"
              System.out.println("m.group(1): \t" + m.group(1)); 
              System.out.println("m.group(2): \t" + m.group(2));
              // System.out.println("m.group(3): \t" + m.group(3)); //java.lang.IndexOutOfBoundsException: No group 3
              
              //=======================================
              double monthly_payment = Double.parseDouble(m.group(1));
              double tax = Double.parseDouble(m.group(2));
              
double monthly_and_tax_amount = new BigDecimal((monthly_payment * tax) / 100).setScale(2, RoundingMode.HALF_UP).doubleValue();// (91.21 * 8.25) / 100 = 7.524825
double monthly_payment_with_tax = new BigDecimal(monthly_payment + monthly_and_tax_amount).setScale(2, RoundingMode.HALF_UP).doubleValue();// 91.21 + 7.52 = 98.72999999999999
double annual_payment_with_tax = new BigDecimal(monthly_payment_with_tax * 12).setScale(2, RoundingMode.HALF_UP).doubleValue();//98.72999*12=1184.76
driver.findElement(By.id("id_annual_payment_with_tax")).sendKeys(String.valueOf(annual_payment_with_tax));

              driver.findElement(By.id("id_validate_button")).submit();
              String actual_result = driver.findElement(By.id("id_result")).getText();
              final long finish = System.currentTimeMillis();
              System.out.println("String: \t\t " + m.group(0));
              System.out.println("Annual Payment with Tax: " + annual_payment_with_tax);
              System.out.println("Result: \t\t " + actual_result);
              System.out.println("Resonse time: \t\t " + (finish - start) + " milliseconds");
              //=======================================
              
              
              Thread.sleep(5000);
              driver.quit(); }}


