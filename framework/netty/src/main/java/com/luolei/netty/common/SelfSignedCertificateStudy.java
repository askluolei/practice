package com.luolei.netty.common;

import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.io.File;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;

/**
 * @author 罗雷
 * @date 2018/2/8 0008
 * @time 10:22
 */
public class SelfSignedCertificateStudy {

    public static void main(String[] args) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SelfSignedCertificate selfSignedCertificate = new SelfSignedCertificate();
        X509Certificate cert = selfSignedCertificate.cert();
        PrivateKey key = selfSignedCertificate.key();
        System.out.println(cert.getVersion());
        System.out.println(cert.getIssuerDN().getName());
        System.out.println(format.format(cert.getNotBefore()));
        System.out.println(format.format(cert.getNotAfter()));
        System.out.println(cert.getSubjectDN().getName());

        System.out.println(key.getAlgorithm());
        System.out.println(key.getFormat());

    }
}
