package com.star.strategy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class EvaluateStrategy {
    private static Logger LOGGER = Logger.getLogger(EvaluateStrategy.class);
    public void run(String fileName, String outFile) {
        try {
            List<String> lines = FileUtils.readLines(new File(fileName));

            Map<Integer, List<Double>> map = new TreeMap<Integer, List<Double>>();

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] tmp = line.split(",");
                int buyDate = Integer.parseInt(tmp[1]);
                double profit = Double.parseDouble(tmp[5]);

                List<Double> list = map.get(buyDate);
                if (list == null) {
                    list = new ArrayList<Double>();
                    map.put(buyDate, list);
                }
                list.add(profit);
            }

            Map<Integer, Double> buyDayAvgProfit = new TreeMap<Integer, Double>();

            PrintStream out = new PrintStream(new FileOutputStream(outFile),
                    false, "GBK");
            for (Map.Entry<Integer, List<Double>> e : map.entrySet()) {
                double sum = 0;
                for (double v : e.getValue()) {
                    sum += v;
                }
                double avg = sum / e.getValue().size();

                buyDayAvgProfit.put(e.getKey(), avg);
                out.println(e.getKey() + "," + avg);
            }

            BigDecimal yearReturn = BigDecimal.ONE;
            BigDecimal totalReturn = BigDecimal.ONE;

            int preYear = 1999;
            LOGGER.info("开始计算yearReturn");
            for (Map.Entry<Integer, Double> e : buyDayAvgProfit.entrySet()) {
                int year = e.getKey() / 10000;
                if (year != preYear && preYear != 1999) {
                    out.println(preYear + "," + yearReturn.doubleValue());
                    yearReturn = BigDecimal.ONE;
                }

                yearReturn = yearReturn.multiply(new BigDecimal(1 + e
                        .getValue()));

                totalReturn = totalReturn.multiply(new BigDecimal(1 + e
                        .getValue()));

                preYear = year;
            }
            LOGGER.info("开始计算yearReturn");
            out.println(preYear + "," + yearReturn.doubleValue());
            out.println("total," + totalReturn.doubleValue());

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception {
        File root = new File("single");
        FileUtils.forceMkdir(new File(root.getAbsolutePath() + "/eval"));
        EvaluateStrategy strategy = new EvaluateStrategy();
        for (File f : root.listFiles()) {
            if (f.isDirectory())
                continue;
            String evalName = f.getName();
            if (!evalName.toLowerCase().endsWith(".csv"))
                continue;

            int pos = evalName.lastIndexOf(".");
            evalName = root.getAbsolutePath() + "/eval/"
                    + evalName.substring(0, pos) + "-eval.csv";
            strategy.run(f.getAbsolutePath(), evalName);
        }

    }
}
