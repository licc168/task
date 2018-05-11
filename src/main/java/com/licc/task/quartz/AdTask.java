
package com.licc.task.quartz;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.licc.task.Const;
import com.licc.task.service.IacAdService;
import com.licc.task.service.IpService;
import com.licc.task.vo.IacAdVo;

/**
 * 广告定时任务
 *
 * @author lichangchao
 * @version 1.0.0
 * @date 2018/5/10 13:28
 * @see
 */
@Component
public class AdTask {
    Logger       logger = LoggerFactory.getLogger(AdTask.class);
    @Resource
    IpService    ipService;
    @Resource
    IacAdService adService;

    @Scheduled(fixedRate = 10000)

    public void test() {
        if (Const.taskFlag) {
            Const.taskFlag = false;
            WebDriver driver = null;

            Set<String> ips = ipService.getUseFulProxy();

            if (!CollectionUtils.isEmpty(ips)) {
                for (String ip : ips) {
                    List<IacAdVo> list = adService.listByStatus();
                    if (!CollectionUtils.isEmpty(list)) {

                        try {
                            Thread.sleep(10000);
                            String host = ip.split(":")[0];
                            String port = ip.split(":")[1];
                            FirefoxOptions options = new FirefoxOptions();
                            options.setHeadless(true);
                            options.addPreference("permissions.default.image", 2);// 禁止加载图片
                            options.addPreference("network.proxy.http", host);
                            options.addPreference("network.proxy.http_port", port);
                            driver = new FirefoxDriver(options);
                            driver.get("http://cn.clowndreams.net/api.php/Home/Taskdetail/index/if_id/826/sharefrom/117945");
                            WebDriverWait wait = new WebDriverWait(driver, 10);
                            wait.until(ExpectedConditions.presenceOfElementLocated(By.ById.id("praisebg")));
                            ipService.setUseIp(ip);
                            logger.info("IP:" + ip + " 有效");
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.info("IP:" + ip + " 无效");
                            ipService.deleteUseProxy(ip);
                        } finally {
                            if (driver != null) {
                                driver.quit();
                            }
                        }
                    }

                }
            }

            Const.taskFlag = true;
        }
    }
}
