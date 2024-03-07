package io;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class AppInitializer {

    static final Logger LOG = LoggerFactory.getLogger(AppInitializer.class);

    void onStart(@Observes StartupEvent ev) {
        LOG.info("Application started. Initializing database and tables...");
        String url = "jdbc:sqlite:./database.db";
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                LOG.info("Connected to the SQLite database successfully.");

                try (Statement stmt = conn.createStatement()) {
                    // tbl_bucket 테이블 생성 쿼리
                    String sql = "CREATE TABLE IF NOT EXISTS tbl_bucket (" +
                            "key TEXT PRIMARY KEY," +
                            "name TEXT NOT NULL," +
                            "value TEXT," +
                            "created TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                            "note TEXT)";
                    stmt.execute(sql);
                    LOG.info("Table tbl_bucket created or already exists.");

                    // 여기에 연결 테스트 로직을 추가할 수 있습니다.
                    // 예를 들어, 단순한 SELECT 쿼리를 실행할 수 있습니다.
                    LOG.info("Running a connection test...");
                    ResultSet rs = stmt.executeQuery("SELECT 1");
                    if (rs.next()) {
                        LOG.info("Connection test successful.");
                    } else {
                        LOG.error("Connection test failed.");
                    }
                }
            } else {
                LOG.error("Failed to make connection to SQLite database.");
            }
        } catch (Exception e) {
            // e.printStackTrace();
            LOG.error("Database initialization failed", e);
        }
    }
}
