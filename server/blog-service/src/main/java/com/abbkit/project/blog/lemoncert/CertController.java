package com.abbkit.project.blog.lemoncert;

import com.abbkit.kernel.json.JJSON;
import com.abbkit.kernel.model.ResponseModel;
import com.abbkit.kernel.security.RSA;
import com.abbkit.module.cluster.ak.master.auth.AKFromStreamGetter;
import com.abbkit.module.cluster.cert.SignCert;
import com.abbkit.project.blog.BlogCons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.util.Map;

/**
 * Created by J on 2020/3/21.
 */
@Slf4j
@RestController
@RequestMapping(BlogCons.PATH_PREFIX+"/cert")
public class CertController {
    @Value("${abbkit.module.lemon.akFilePath}")
    private String akFilePath;

    @Value("#{${abbkit.module.lemon.authKey}}")
    private Map<String,String> authMap;


    private String privateKey="MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAIlkeevDx+/85Q3WYw9ZMYkUXHjtFOf+AZFeyqWUVEFuv4JOnSVY5f3hcoYIW8szJSe/0S1ak8fiPqOUH/doZIj/c9qO33fIR7g9rwaQvvMkBHBVNe2ivorlDEptqby4tw4PGwU8FQ19TWjy3D/B2mA7emYIATXtf7lWsPtLJ5NVAgMBAAECgYEAgYBofza55VZdXI6gLp5m14uOohjt5MICHlDX9/x48nf+IUgSTVqe5o+zD+0fAMhDlFT0SKigtP5nBm6fNud6IlXgxZybQnEwMX0jZ/FJCrVOJbO7uGtFIXsm5ohIPFUNN3ak8Ht9sMyZtVxHC6tfeVBIfBYHVAcePS+DglP9MJ0CQQDaeykKR5vJg5CYBJ0bj8tlMJobsX+ROqeP3sCUbeC2zgSsaAz9wPRTZAvKlEb7QUYCo0FFhOM0Ocom4aBWUr0nAkEAoPyCGj7BLRmWyPmIZDFp86mdmUekYeXnVbkstPCZHEo/0fxlbddSx4aqaNePeEOBUpS14A6Vl7/lo7h0f8rxIwJAV4Q2MFP254W4CezvMfcrtmD9mlJDk103eb30zoAlH5Tu8lDLw2njdPQCdSdoN4UyEiRrJPnSOT1c6K5Al9OUyQJBAIWJz/YrVhyuHEjrzLqnzqajbSqHphDbH3EZYL9a0PcJlDUAc7aQpYcC6vp6zPTKskypNND96BLvyHJtgJ283xcCQQCXbH3/EUaVBH9zmiFvCq7cS51e5nyz06d+c7gtyNSWy85yS4rCQRx9uNksU+Ont9xL2LrX4FccVpGkdz5Y9xBX";


    @ResponseBody
    @GetMapping(path = "auth")
    public ResponseModel auth(@RequestParam("userName") String userName,
                             @RequestParam("password") String password
                             )throws Exception{
        log.info(userName+" , user try to be authorized.");
        if(authMap.containsKey(userName) &&password.equals(authMap.get(userName))){

            String ak=new AKFromStreamGetter(new FileInputStream(akFilePath)).ak();
            CertGenerate certGenerate=new CertWithDays(30);
            String finalCertString=certGenerate.encrypt(ak);

            SignCert signCert=new SignCert();
            signCert.setCert(finalCertString);
            signCert.setSign(RSA.sign(finalCertString,RSA.getPrivateKey(privateKey)));
            return ResponseModel.newSuccess(JJSON.get().format(signCert));
        }

        return ResponseModel.newError("invalid");

    }

}
