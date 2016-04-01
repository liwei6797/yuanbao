package com.star.db;

import java.sql.SQLException;

import org.h2.tools.Server;

public class H2Utils {
    private static Server server;
    private static String port = "8081";
    private static String dbDir = "jdbc:h2:tcp://localhost:8081/~/stock";
    private static String user = "sa";
    private static String password = "";
    public static void startServer() {
        try {
            System.out.println("正在启动h2...");
            server = Server.createTcpServer(
                    new String[] { "-tcpPort", port }).start();
        } catch (SQLException e) {
            System.out.println("启动h2出错：" + e.toString());
            //e.printStackTrace();
            //throw new RuntimeException(e);
        }
    }


    public static void stopServer() {
        if (server != null) {
            System.out.println("正在关闭h2...");
            server.stop();
            System.out.println("关闭成功.");
        }
    }

}
