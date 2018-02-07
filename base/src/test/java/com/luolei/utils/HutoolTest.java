package com.luolei.utils;

import cn.hutool.core.lang.Console;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.dfa.SensitiveUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author 罗雷
 * @date 2018/2/6 0006
 * @time 9:10
 */
public class HutoolTest {

    public static void testSeneitive() {
        String[] sensitiveWords = {"傻逼", "王八蛋"};
        String[] toValidWords = {"你是傻逼", "小王八蛋", "你好"};

        SensitiveUtil.init(Arrays.asList(sensitiveWords));
        for (String word: toValidWords) {
            System.out.println(SensitiveUtil.containsSensitive(word));
        }
    }

    public static void main(String[] args) throws Exception {
    }
}
