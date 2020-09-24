package org.finalframework.selenium.processon;

import lombok.extern.slf4j.Slf4j;
import org.finalframework.selenium.mail.Mail;
import org.finalframework.selenium.mail.TempMail;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ProcessOn微信注册器
 *
 * @author likly
 * @version 1.0
 * @date 2020/9/22 22:19:43
 * @since 1.0
 */
@Slf4j
public class ProcessOnWeChatRegister {

    private static final String HOME = "https://www.processon.com/i/566ae249e4b0add117b7c405";

    private final WebDriver driver = new ChromeDriver();


    public ProcessOnWeChatRegister() {
        driver.manage().window().setPosition(new Point(0, 0));
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    public void login() {
        logger.info("start login...");
        driver.findElement(By.linkText("登录")).click();
        sleep(1000);
    }

    public void wechatLogin() {
        logger.info("wechat login...");
        driver.findElement(By.id("weixin_login")).click();
        logger.info("please scan qrcode use wechat");
    }

    public void skipBindAccount() {

        do {
            sleep(2000);
            logger.info("wait to skip bind account...");
        } while (!driver.getCurrentUrl().startsWith("https://www.processon.com/wechat/public/bind_account")
                && !driver.getCurrentUrl().startsWith("https://www.processon.com/diagrams"));


        while (true) {
            sleep(1000);
            if (driver.getCurrentUrl().startsWith("https://www.processon.com/wechat/public/bind_account")) {
                //跳过账号绑定
                logger.info("skip account bind...");
                WebElement unbindAccount = findElement(By.id("unbind_account"));
                if (unbindAccount == null) {
                    break;
                }
                unbindAccount.click();
            } else {
                break;
            }
        }
    }

    public void goSetting() {

        while (true) {
            if (driver.getCurrentUrl().startsWith("https://www.processon.com/diagrams")) {
                logger.info("go to setting...");
                driver.navigate().to("https://www.processon.com/setting");
                sleep(1000);
                break;
            }
        }


    }

    public void home(String url) throws InterruptedException {
        logger.info("open home: {}", url);
        driver.manage().deleteAllCookies();
        driver.get(url);
    }

    public void close() {
        driver.close();
    }


    private WebElement findElement(By by) {
        try {
            return driver.findElement(by);
        } catch (Exception e) {
            return null;
        }
    }


    public void bindMail() {
        logger.info("try to bind mail...");
        WebElement bindMail = findElement(By.linkText("绑定邮箱"));

        if (bindMail != null) {
            bindMail.click();
            final Mail<String> mail = new TempMail<>();

            mail.setContentParser(new Mail.ContentParser<String>() {
                @Override
                public String doParse(WebDriver driver) {
                    while (true) {
                        try {
                            WebElement mailContent = driver.findElement(By.className("mail-content"));
                            List<WebElement> elements = mailContent.findElements(By.tagName("strong"));
                            if (elements != null && elements.size() == 2) {
                                return elements.get(1).getText().trim();
                            }
                            sleep(200);
                        } catch (Exception e) {

                        }
                    }
                }
            });

            mail.setMailListener(new Mail.MailListener<String>() {
                @Override
                public void onInit(String mail) {

                    logger.info("bind to mail: {}", mail);
                    driver.findElement(By.id("TencentCaptchaEmailBtn")).click();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                sleep(2000);
                                driver.findElement(By.id("email_txt")).sendKeys(mail);

                                WebElement frame = driver.findElement(By.id("tcaptcha_iframe"));
                                WebDriver frameDriver = driver.switchTo().frame(frame);

                                for (int offset = 200; offset < 230; offset = offset + 5) {

                                    WebElement image = frameDriver.findElement(By.id("tcaptcha_drag_button"));
                                    logger.info("try to send code: offset={}", offset);
                                    new Actions(frameDriver).dragAndDropBy(image, offset, 0).perform();
                                    sleep(1000);
                                }
                            } catch (Exception e) {
                                // ignore
                                logger.error("do vary code error", e);
                            }
                        }
                    }).start();


                }

                @Override
                public void onReceive(WebDriver driver, WebElement message) {
                    logger.info("received mail: {}", message.getAttribute("href"));
                    message.click();
                    mail.parse();
                }

                @Override
                public void onRead(String content) {
                    //验证码
                    driver.findElement(By.id("verify_email_txt")).sendKeys(content);
                    //密码
                    driver.findElement(By.id("password_txt")).sendKeys(content);
                    //确定
                    driver.findElement(By.id("btn_submit_email")).click();
                    mail.close();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    unbindWeChat();
                    sleep(500);

                }
            });

            mail.open();
        } else {
            logger.info("mail was bind...");
            unbindWeChat();
        }


    }

    public void unbindWeChat() {
        logger.info("try to unbind wechat...");
        driver.findElement(By.id("bingweixin")).click();
        sleep(100);
        driver.findElement(By.id("global_confirm_window"))
                .findElement(By.className("okbtn"))
                .click();
    }

    public void loginOut() {
        logger.info("try to login out...");
        driver.findElement(By.id("user-logo")).click();
        sleep(100);
        driver.navigate().to("https://www.processon.com/login/out");
    }

    private void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
