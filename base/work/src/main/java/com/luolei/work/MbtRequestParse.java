package com.luolei.work;

import lombok.ToString;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 罗雷
 * @date 2017/11/10 0010
 * @time 16:09
 */
public class MbtRequestParse {

    @ToString
    static class InvokeModel {
        String methodName;
        String requestSeq;
        LocalDateTime invokeTime;
        LocalDateTime returnTime;
        long cost;
        String logPath;
        String requestPath;
        String responsePath;
        boolean exsitException;
    }

    static class Metrics {
        long minCost;
        long maxCost;
        long avgCost;
        long count;
        long sumCost;
    }

    private static List<InvokeModel> invokeModels = new ArrayList<>();
    private static DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private static List<InvokeModel> longCostModels = new ArrayList<>();

    private static Map<String, Metrics> metricsMap = new HashMap<>();

    public static void main(String[] args) throws Exception {
        parse();
        countAll();
        longCostCount();
    }

    private static final String PATH = "E:\\kingdee\\document\\mbtsrequst.txt";

    public static void longCostCount() {
        System.out.println("统计的总业务次数：" + invokeModels.size());
        System.out.println("处理超过20秒的业务有 ：" + longCostModels.size() + "次");
        Map<String, Integer> map = new HashMap<>();
        for(InvokeModel model: longCostModels) {
            if (map.containsKey(model.methodName)) {
                map.put(model.methodName, map.get(model.methodName) + 1);
            } else {
                map.put(model.methodName, 0);
            }
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println("方法为:" + entry.getKey() + " 调用超过20s 的次数为 " + entry.getValue());
        }
    }

    public static void countAll() throws Exception {
        for (InvokeModel model : invokeModels) {
            String key = model.methodName;
            Metrics metrics;
            if (metricsMap.containsKey(key)) {
                metrics = metricsMap.get(key);
            } else {
                metrics = new Metrics();
                metrics.minCost = Integer.MAX_VALUE;
                metrics.maxCost = 0;
                metrics.count = 0;
                metrics.sumCost = 0;
                metricsMap.put(key, metrics);
            }

            long cost = model.cost;
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

    public static void test() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(PATH));
        System.out.println(lines.size());
    }

    public static void parse() throws Exception {
        for (String line : Files.readAllLines(Paths.get(PATH))) {
            String[] array = line.split(",");
            if (array.length != 9) {
                continue;
            }

            InvokeModel model = new InvokeModel();
            model.methodName = array[0].trim();
            model.requestSeq = array[1].trim();
            model.invokeTime = LocalDateTime.parse(array[2].trim(), formatter);
            model.returnTime = LocalDateTime.parse(array[3].trim(), formatter);
            model.cost = Long.parseLong(array[4].trim());
            model.logPath = array[5].trim();
            model.requestPath = array[6].trim();
            model.responsePath = array[7].trim();
            model.exsitException = Boolean.parseBoolean(array[8].trim());

            invokeModels.add(model);

            if (model.cost > 20000) {
                longCostModels.add(model);
            }
        }
    }
}
