package com.luolei.work;

import lombok.ToString;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 罗雷
 * @date 2017/11/10 0010
 * @time 13:57
 */
public class Application {

//    private static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    @ToString
    static class TimeModel {
        String bizType;
        String subBizType;
        String accNo;
        String requestTime;
        String responseTime;
        long cost;
    }

    static class Metrics {
        long minCost;
        long maxCost;
        long avgCost;
        long count;
        long sumCost;
    }

    private static List<TimeModel> timeModels = new ArrayList<>();
    private static List<String> allPaths = new ArrayList<>();

    private static Map<String, Metrics> metricsMap = new HashMap<>();

    private static final String ROOT_PATH = "E:\\kingdee\\document\\2017-11-10_2017-11-10.log\\mbts_invoke\\2017_11_10";

    public static void main(String[] args) throws Exception {
        listPath(ROOT_PATH);
//        test();
        parsing();
        countAll();
    }

    private static void parseComplete() {
        for (TimeModel model : timeModels) {
            System.out.println(model.toString());
        }
    }

    private static void countAll() {
        for (TimeModel model : timeModels) {
            String key = model.subBizType;
            long cost = model.cost;
            Metrics metrics;
            if (!metricsMap.containsKey(key)) {
                metrics = new Metrics();
                metrics.minCost = Integer.MAX_VALUE;
                metrics.maxCost = 0;
                metrics.count = 0;
                metrics.sumCost = 0;
                metricsMap.put(key, metrics);
            } else {
                metrics = metricsMap.get(key);
            }

            metrics.count ++;
            metrics.sumCost += cost;

            if (cost < metrics.minCost) {
                metrics.minCost = cost;
            }

            if (cost > metrics.maxCost) {
                metrics.maxCost = cost;
            }

            metrics.avgCost = metrics.sumCost / metrics.count;
        }

        for (Map.Entry<String, Metrics> entry : metricsMap.entrySet()) {
            String key = entry.getKey();
            Metrics metrics = entry.getValue();
            System.out.println("业务：" + key + " 统计数量：" +  metrics.count + " 总耗时：" + metrics.sumCost + " 最大耗时：" + metrics.maxCost + " 最小耗时：" + metrics.minCost + " 平均耗时：" + metrics.avgCost);
        }
    }


    private static void listPath(String rootPath) {
        File rootDir = new File(rootPath);
        for (File file : rootDir.listFiles()) {
            if (file.isDirectory()) {
                listPath(file.getAbsolutePath());
            } else {
                if (file.getName().equalsIgnoreCase("response")) {
                    allPaths.add(file.getAbsolutePath());
                }
            }
        }
    }

    private static void parsing() throws Exception {
        for (String path: allPaths) {
            parseResponse(path);
        }
    }

    private static void test() throws Exception {
//        String path = "E:\\kingdee\\document\\2017-11-10_2017-11-10.log\\mbts_invoke\\2017_11_10\\balance\\balance_20171110093938_buhpa043.ABC_DC-1\\response";
////        File file = new File(path);
////        List<String> lines = Files.readAllLines(Paths.get(path));
////        for (String line : lines) {
////            System.out.println(line);
////        }
//
//        parseResponse(path);
        for (String path: allPaths) {
            System.out.println(path);
        }
    }

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static void parseResponse(String path) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(path));
        TimeModel model = new TimeModel();
        for (String line: lines) {
            if (line.contains("</header>")) {
                break;
            }

            if (line.contains("<bizType>")) {
                model.bizType = line.substring(line.indexOf("<bizType>") + "<bizType>".length(), line.indexOf("</bizType>"));
            }
            if (line.contains("<subBizType>")) {
                model.subBizType = line.substring(line.indexOf("<subBizType>") + "<subBizType>".length(), line.indexOf("</subBizType>"));
            }
            if (line.contains("<accNo>")) {
                model.accNo = line.substring(line.indexOf("<accNo>") + "<accNo>".length(), line.indexOf("</accNo>"));
            }
            if (line.contains("<requestTime>")) {
                model.requestTime = line.substring(line.indexOf("<requestTime>") + "<requestTime>".length(), line.indexOf("</requestTime>"));
            }
            if (line.contains("<responseTime>")) {
                model.responseTime = line.substring(line.indexOf("<responseTime>") + "<responseTime>".length(), line.indexOf("</responseTime>"));
            }
        }
        try {
            Date startTime = simpleDateFormat.parse(model.requestTime);
            Date endTime = simpleDateFormat.parse(model.responseTime);
            model.cost = (endTime.getTime() - startTime.getTime()) / 1000;
            timeModels.add(model);
        } catch (Exception e) {
        }
    }
}
