package org.example;

import org.example.util.Util;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 현재 작업 디렉토리 경로 출력
        System.out.println(System.getProperty("user.dir"));

        Path path = Paths.get(System.getProperty("user.dir"), "src/main/resources/chromedriver.exe");
        // WebDriver 경로 설정
        System.setProperty("webdriver.chrome.driver",path.toString());


        // Chrome 브라우저 실행
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");  // 브라우저를 최대화하여 실행
        options.addArguments("--disable-popup-blocking"); // 팝업 무시
        options.addArguments("--disable-default-apps"); // 기본앱 사용안함
        options.addArguments("--disable-blink-features=AutomationControlled"); // 봇으로 감지 방지

        // Webdriver 객체 생성
        WebDriver driver = new ChromeDriver(options);

        // 빈 탭 생성
        String newTab = "window.open('about:blank','_blank');";
        ((JavascriptExecutor) driver).executeScript(newTab);

        List<String> tabs = new ArrayList<String>(driver.getWindowHandles());

        //첫 번째 탭으로 전환
        driver.switchTo().window(tabs.get(0));
        driver.get("https://velog.io/");

        Util.sleep(1000);

        // 두 번째 탭으로 전환
        driver.switchTo().window(tabs.get(1)); // 0번 탭이 첫 번째 탭, 1번 탭이 두 번째 탭
        driver.get("https://velog.io/");

        File downloadsFolder = new File("downloads");

        if ( downloadsFolder.exists() == false) {
            downloadsFolder.mkdir();
        }

        // 페이지가 로드될 때까지 대기
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));

        // JavaScript를 사용하여 페이지에 HTML 및 스타일을 추가
        String addScriptToPage = "var div = document.createElement('div');" +
                "div.style.width = '100px';" +
                "div.style.height = '100px';" +
                "div.style.backgroundColor = 'red';" +
                "div.style.position = 'fixed';" + // 상단에 고정
                "div.style.top = '0';" + // 상단에 위치
                "div.style.left = '50%';" + // 가운데 정렬을 위해 왼쪽 위치 조정
                "div.style.transform = 'translateX(-50%)';" + // 가운데 정렬
                "div.style.zIndex = '9999';" + // z-index 설정
                "document.body.appendChild(div);" +
                "console.log('스크립트가 실행되었습니다.');";
        ((JavascriptExecutor) driver).executeScript(addScriptToPage);

        // 스크린샷 캡처
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        // 스크린샷 파일 이름 생성
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String screenshotFileName = "screenshot_" + timestamp + ".png";

        // 스크린샷을 저장할 경로 설정 (원하는 경로로 변경하세요)
        File outputImageFile = new File("downloads/" + screenshotFileName);

        // 스크린샷 파일을 원하는 경로로 복사
        screenshotFile.renameTo(outputImageFile);

        // 브라우저 종료
//        driver.quit();

    }
}