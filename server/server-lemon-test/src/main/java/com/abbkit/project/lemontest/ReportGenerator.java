package com.abbkit.project.lemontest;

import com.abbkit.kernel.util.IOUtils;
import com.abbkit.kernel.util.StringUtils;
import com.abbkit.lemon.server.test.LemonTestCaseLauncher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
public class ReportGenerator {

    @Value("${abbkit.module.lemonTest.cmd:/allure-commandline-2.20.1/allure-2.20.1/bin/allure}")
    private String cmd;

    @Value("${abbkit.module.lemonTest.dataDir:/var/lib/abbkit/allure-data}")
    private String dataDir;

    @Value("${abbkit.module.lemonTest.htmlDir:/var/lib/abbkit/allure-report}")
    private String htmlDir;


    private static AtomicLong count = new AtomicLong(0L);

    @Scheduled(cron="0/60 * * * * ?")
    public void generateData(){
        try {
            LemonTestCaseLauncher lemonTestCaseLauncher=new LemonTestCaseLauncher();
            lemonTestCaseLauncher.start(count);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

    }
    @Scheduled(cron="0 0/5 * * * ?")
    public void generateHtml(){
        try {
            //allure generate -c dataDir -o reportDir
            log.info("==========allure generate开始执行==========");
            String command = cmd+" generate -c "+dataDir+" -o "+htmlDir;
            Process process = Runtime.getRuntime().exec(command);
            InputStream inputStream = process.getInputStream();
            log.info(StringUtils.utf8(IOUtils.getBytes(inputStream)));
            int exitCode = process.waitFor();
            log.info("==========allure generate完成:"+exitCode+"==========");
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

    }


}
