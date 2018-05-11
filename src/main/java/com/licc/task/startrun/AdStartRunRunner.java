package com.licc.task.startrun;

import com.licc.task.enums.EIacAdStatus;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.licc.task.service.IacAdService;
import com.licc.task.service.IpService;
import com.licc.task.vo.IacAdVo;

/**
 * 启动初始化
 *
 * @author lichangchao
 * @version 1.0.0
 * @date 2018/4/3 18:06
 * @see
 *
 *
 */
@Order(10)
@Component

public class AdStartRunRunner implements CommandLineRunner {
    Logger       logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    IpService    ipService;
    @Resource
    IacAdService adService;

    @Override
    public void run(String... args) {
        WebDriver driver = null;
        while (true) {
            try {
                Thread.sleep(1000);

                Set<String> ips = ipService.getIp();
                if (!CollectionUtils.isEmpty(ips)) {
                    for (String ip : ips) {
                        List<IacAdVo> list = adService.listByStatus();
                        adService.updateStaus();// 更新状态为进行中
                        if (!CollectionUtils.isEmpty(list)) {
                            for (IacAdVo adVo : list) {
                                int broeseNum = adVo.getBrowseNum();
                                int praiseNum = adVo.getPraiseNum();
                                String url = adVo.getUrl();
                                if (!ipService.hasUseUrlIp(url, ip)) {
                                    try {
                                        String host = ip.split(":")[0];
                                        String port = ip.split(":")[1];
                                        FirefoxOptions options = new FirefoxOptions();
                                        options.setHeadless(true);
                                        options.addPreference("permissions.default.image", 2);// 禁止加载图片
                                        options.addPreference("network.proxy.http", host);
                                        options.addPreference("network.proxy.http_port", port);
                                        driver = new FirefoxDriver(options);
                                        driver.get(adVo.getUrl());
                                        WebDriverWait wait = new WebDriverWait(driver, 10);
                                        WebElement praisebg = wait
                                                .until(ExpectedConditions.presenceOfElementLocated(By.ById.id("praisebg")));
                                        if (adVo.getPraiseNum() < adVo.getMaxPraiseNum()) {// 刷点赞量
                                            praisebg.click();
                                            praiseNum = praiseNum + 1;
                                        }
                                        broeseNum = broeseNum + 1;
                                         String readnum =   driver.findElement(By.className("readnum")).getText();
                                         String praisenum =   driver.findElement(By.id("praisenum")).getText();
                                        logger.info("总阅读量："+readnum+"  总点赞："+praisenum+"   链接【" + adVo.getUrl() + "】浏览量【" + broeseNum + "】点赞量【" + praiseNum + "】");
                                        adService.update(adVo.getId(), broeseNum, praiseNum);
                                        ipService.setUseUrlIp(url, ip);
                                        if(broeseNum>100){
                                            adService.updateStausById(adVo.getId(), EIacAdStatus.WANCHENG.getKey());
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        if (driver != null) {
                                            driver.quit();

                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
