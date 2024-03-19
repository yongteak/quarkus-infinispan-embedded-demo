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
        String infinispanBindAddr = System.getenv("INFINISPAN_BIND_ADDR");
        String infinispanInitialHosts = System.getenv("INFINISPAN_INITIAL_HOSTS");
        String infinispanGossipRouterHosts = System.getenv("INFINISPAN_GOSSIP_ROUTER_HOSTS");
        // infinispan.gossip.router.hosts

        
        System.out.println("INFINISPAN_STORE_PERSISTENCE_PATH: " + infinispanStorePersistencePath);
        System.out.println("INFINISPAN_STORE_PERSISTENT_PATH: " + infinispanStorePersistentPath);
        System.out.println("INFINISPAN_STORE_TEMPORARY_PATH: " + infinispanStoreTemporaryPath);
        System.out.println("INFINISPAN_BIND_ADDR: " + infinispanBindAddr);
        System.out.println("INFINISPAN_INITIAL_HOSTS: " + infinispanInitialHosts);
        System.out.println("INFINISPAN_GOSSIP_ROUTER_HOSTS: " + infinispanGossipRouterHosts);
        
        System.setProperty("infinispan.gossip.router.hosts", System.getenv("INFINISPAN_GOSSIP_ROUTER_HOSTS"));
        System.setProperty("infinispan.bind_addr", System.getenv("INFINISPAN_BIND_ADDR"));
        System.setProperty("infinispan.cluster.name", System.getenv("INFINISPAN_CLUSTER_NAME"));
        System.setProperty("infinispan.node.name", System.getenv("INFINISPAN_NODE_NAME"));
        System.setProperty("infinispan.store.persistence.path", System.getenv("INFINISPAN_STORE_PERSISTENCE_PATH"));
        System.setProperty("infinispan.store.persistent.path", System.getenv("INFINISPAN_STORE_PERSISTENT_PATH"));
        System.setProperty("infinispan.store.temporary.path", System.getenv("INFINISPAN_STORE_TEMPORARY_PATH"));
        
        // String home = System.getProperty("user.home");
        // LOG.info("### user.home ==>>> ",home);
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("infinispan-tcp-unencrypted.xml");
        if (is == null) {
            throw new RuntimeException("File not found [ infinispan-tcp-unencrypted.xml ]");
        } else {
            LOG.info("INCLUDED!! infinispan-tcp-unencrypted.xml");    
        }

        if (infinispanInitialHosts == null) {
            System.setProperty("infinispan.initial.hosts", "localhost[7800]");
            System.out.println(">>> infinispan.initial.hosts: " + "localhost[7800]");
        } else {
            System.setProperty("infinispan.initial.hosts", infinispanInitialHosts);
            System.out.println(">>>? infinispan.initial.hosts: exist!! > " + infinispanInitialHosts);
        }

        LOG.info("[START EmbeddedCacheManager SERVICE!!!]");
        cs.start();;
        
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
