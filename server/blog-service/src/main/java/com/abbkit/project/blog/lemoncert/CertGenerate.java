package com.abbkit.project.blog.lemoncert;

import com.abbkit.module.cluster.cert.Cert;

/**
 * Created by J on 2020/3/21.
 */
public interface CertGenerate{

    String encrypt(String ak)throws Exception;

    Cert decrypt(String finalCertString, String ak) throws Exception;
}
