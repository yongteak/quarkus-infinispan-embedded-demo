package io;

import io.github.renegrob.infinispan.embedded.CacheService;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class AppInitializer {

    @Inject
    CacheService cs;

    static final Logger LOG = LoggerFactory.getLogger(AppInitializer.class);

    void onStart(@Observes StartupEvent ev) {

        // String oraclizerHttpPort = System.getenv("ORACLIZER_HTTP_PORT");
        // String oraclizerApiPort = System.getenv("ORACLIZER_API_PORT");
        String infinispanStorePersistencePath = System.getenv("INFINISPAN_STORE_PERSISTENCE_PATH");
        String infinispanStorePersistentPath = System.getenv("INFINISPAN_STORE_PERSISTENT_PATH");
        String infinispanStoreTemporaryPath = System.getenv("INFINISPAN_STORE_TEMPORARY_PATH");

        // System.out.println("ORACLIZER_HTTP_PORT: " + oraclizerHttpPort);
        // System.out.println("ORACLIZER_API_PORT: " + oraclizerApiPort);
        // System.out.println("INFINISPAN_CLUSTER_NAME: " + infinispanClusterName);
        // System.out.println("INFINISPAN_NODE_NAME: " + infinispanNodeName);
        System.out.println("INFINISPAN_STORE_PERSISTENCE_PATH: " + infinispanStorePersistencePath);
        System.out.println("INFINISPAN_STORE_PERSISTENT_PATH: " + infinispanStorePersistentPath);
        System.out.println("INFINISPAN_STORE_TEMPORARY_PATH: " + infinispanStoreTemporaryPath);

        System.setProperty("infinispan.cluster.name", System.getenv("INFINISPAN_CLUSTER_NAME"));
        System.setProperty("infinispan.node.name", System.getenv("INFINISPAN_NODE_NAME"));
        System.setProperty("infinispan.store.persistence.path", System.getenv("INFINISPAN_STORE_PERSISTENCE_PATH"));
        System.setProperty("infinispan.store.persistent.path", System.getenv("INFINISPAN_STORE_PERSISTENT_PATH"));
        System.setProperty("infinispan.store.temporary.path", System.getenv("INFINISPAN_STORE_TEMPORARY_PATH"));
        
        String home = System.getProperty("user.home");
        LOG.info("### user.home ==>>> ",home);

        cs.start();;
        

        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("infinispan-tcp-unencrypted.xml");
        if (is == null) {
            throw new RuntimeException("File not found [ infinispan-tcp-unencrypted.xml ]");
        } else {
            LOG.info("INCLUDED!! infinispan-tcp-unencrypted.xml");    
        }
        
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
