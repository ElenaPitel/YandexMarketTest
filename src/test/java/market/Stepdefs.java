package market;

import cucumber.api.java.ru.Дано;
import cucumber.api.java.ru.Когда;
import cucumber.api.java.ru.Тогда;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Stepdefs {

    private static final String DRIVER_NAME = "webdriver.chrome.driver";
    private static final String DRIVER_PATH = "src\\test\\resources\\chromedriver.exe";
    private String nameOfFirstPhone;
    private ChromeDriver driver;
    private WebDriverWait webDriver;

    @Дано("Открыть браузер и развернуть на весь экран")
    public void открыть_браузер_и_развернуть_на_весь_экран() {
        System.setProperty(DRIVER_NAME, DRIVER_PATH);
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Когда("Зайти на {string}")
    public void зайти_на(String url) {
        driver.get(url);
    }

    @Когда("Перейти в яндекс {string}")
    public void перейти_в_яндекс(String text) {
        driver.findElementByLinkText(text).click();
    }

    @Когда("Выбрать раздел {string}")
    public void выбрать_раздел(String section) {
        driver.findElementByLinkText(section).click();
    }

    @Когда("Выбрать раздел Мобильные телефоны")
    public void выбрать_раздел_Мобильные_телефоны(String section) {
        driver.findElementByLinkText(section).click();
    }

    @Когда("Выбрать раздел Наушники")
    public void выбрать_раздел_Наушники(String section) {
        driver.findElementByLinkText(section).click();
    }

    @Когда("Перейти в расширенный поиск {string}")
    public void перейти_в_расширенный_поиск(String filter) {
        driver.findElementByLinkText(filter).click();
    }

    @Когда("Задать параметр поиска от {string} рублей")
    public void задать_параметр_поиска_от_рублей(String fromPrice) {
        driver.findElementById("glf-pricefrom-var").sendKeys(fromPrice);
    }

    @Когда("Выбрать производителей {string}, {string}")
    public void выбрать_производителей(String first, String second) {
        if(!first.isEmpty()) {
            driver.findElementByLinkText(first).click();
        }
        if(!second.isEmpty()) {
            driver.findElementByLinkText(second).click();
        }
    }

    @Когда("Нажать кнопку {string}")
    public void нажать_кнопку(String buttonName) {
        webDriver = new WebDriverWait(driver, 5);
        webDriver.until(ExpectedConditions.elementToBeClickable(driver.findElementByLinkText(buttonName)));
        driver.findElementByLinkText(buttonName).click();
    }

    @Тогда("Проверить, что элементов на странице {int}")
    public void проверить_что_элементов_на_странице(int size) throws Throwable {
        try {
            assertEquals(size, driver.findElements(By.className("n-snippet-cell2")).size());
        } catch (AssertionError assertionError) {
            Hooks.errorList.add(assertionError);
        }
    }

    @Когда("Запомнить первый элемент в списке")
    public void запомнить_первый_элемент_в_списке() {
        nameOfFirstPhone = driver.findElementByXPath("/html/body/div[1]/div[5]/div[2]/div[1]/div[1]/div/div[1]/div[1]/div[3]/div[2]").getText();
    }

    @Когда("В поисковую строку ввести запомненное значение")
    public void в_поисковую_строку_ввести_запомненное_значение() {
        driver.findElementById("header-search").sendKeys(nameOfFirstPhone);
    }

    @Тогда("Найти и проверить, что наименование товара соответствует запомненному значению")
    public void найти_и_проверить_что_наименование_товара_соответствует_запомненному_значению() {
        assertEquals(nameOfFirstPhone, driver.findElementByXPath("/html/body/div[1]/div[5]/div[2]/div[1]/div[1]/div/div[1]/div[1]/div[3]/div[2]").getText());
    }

    @Когда("Выбрать сортировку {string}")
    public void выбрать_сортировку(String param) throws InterruptedException {
        driver.findElementByLinkText(param).click();
    }

    @Тогда("Проверить, что элементы на странице отсортированы верно")
    public void проверить_что_элементы_на_странице_отсортированы_верно(){
        String selector = "div.n-snippet-cell2__body > div.n-snippet-cell2__price > div > div > a > div";
        String regex = "\\D+";
        WebElement spinner = null;
        try {
            spinner = driver.findElement(By.className("spin2"));
            webDriver = new WebDriverWait(driver, 3);
            webDriver.until(ExpectedConditions.visibilityOf(spinner));
        } catch (org.openqa.selenium.TimeoutException ex){
        }
        webDriver.until(ExpectedConditions.invisibilityOf(spinner));
        List<WebElement> phones = webDriver.until(ExpectedConditions.visibilityOfAllElements(driver.findElementsByCssSelector(selector)));
        Integer lowerPrice = Integer.parseInt(phones.get(0).getText().replaceAll(regex,""));
        for (int i = 1; i < phones.size(); i++) {
            Integer currentPrice = Integer.parseInt(phones.get(i).getText().replaceAll(regex,""));
            assertTrue(lowerPrice <= currentPrice);
            lowerPrice = currentPrice;
        }
    }
}
