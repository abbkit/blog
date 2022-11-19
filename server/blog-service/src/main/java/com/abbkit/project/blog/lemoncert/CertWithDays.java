package com.abbkit.project.blog.lemoncert;

import com.abbkit.kernel.json.JJSON;
import com.abbkit.module.cluster.ak.master.auth.AKDESedeCipher;
import com.abbkit.module.cluster.cert.Cert;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

/**
 * Created by J on 2020/3/21.
 */
public class CertWithDays implements CertGenerate{

    private final int day;

    public CertWithDays(int day) {
        if(day>30) throw new IllegalArgumentException("larger than max day:30");
        this.day = day;
    }

    @Override
    public String encrypt(String ak) throws Exception {
        Cert cert=new Cert();
        cert.setStart(System.currentTimeMillis());
        cert.setExpired(TimeUnit.SECONDS.toMillis(3600*24*day));
        cert.setAkDelete(true);
        cert.setAkEndTime(LocalDateTime.now()
                .plusSeconds(TimeUnit.MILLISECONDS.toSeconds(cert.getExpired()))
                .plusDays(1)
                .toInstant(ZoneOffset.UTC).toEpochMilli()
        );
        return AKDESedeCipher.get(ak).encrypt(JJSON.get().format(cert));
    }

    @Override
    public Cert decrypt(String finalCertString, String ak) throws Exception {
        String certJSON=AKDESedeCipher.get(ak).decrypt(finalCertString);
        return JJSON.get().parse(certJSON,Cert.class);
    }
}
