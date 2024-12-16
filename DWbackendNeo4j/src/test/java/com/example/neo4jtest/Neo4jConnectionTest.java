package com.example.neo4jtest;

import org.neo4j.driver.*;

public class Neo4jConnectionTest {
    public static void main(String[] args) {
        // 连接信息，包括 Neo4j 数据库的 URI、用户名和密码
        String uri = "bolt://localhost:7687"; // Neo4j 的 URI
        String username = "neo4j"; // Neo4j 用户名
        String password = "lrb2251657"; // Neo4j 密码

        // 创建一个 Neo4j 驱动程序实例
        try (Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password))) {
            // 创建一个会话
            try (Session session = driver.session()) {
                // 执行一个简单的查询来测试连接
                String query = "RETURN 1 AS result";
                Result result = session.run(query);

                // 输出查询结果
                if (result.hasNext()) {
                    System.out.println("连接成功，查询结果：" + result.next().get("result").asInt());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

