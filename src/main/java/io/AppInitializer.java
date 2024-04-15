package io;

import io.github.renegrob.infinispan.embedded.CacheService;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Comparator;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class AppInitializer {

    @Inject
    CacheService cs;

    static final Logger LOG = LoggerFactory.getLogger(AppInitializer.class);

    private static final String TEMP_STATE_FILE = System.getProperty("java.io.tmpdir") + "/appstate.tmp";

    void onStart(@Observes StartupEvent ev) {

        // String oraclizerHttpPort = System.getenv("ORACLIZER_HTTP_PORT");
        // String oraclizerApiPort = System.getenv("ORACLIZER_API_PORT");
        String infinispanStorePersistencePath = System.getenv("INFINISPAN_STORE_PERSISTENCE_PATH");
        String infinispanStorePersistentPath = System.getenv("INFINISPAN_STORE_PERSISTENT_PATH");
        String infinispanStoreTemporaryPath = System.getenv("INFINISPAN_STORE_TEMPORARY_PATH");
        // INFINISPAN_BIND_ADDR값이 없는경우 내 IP 주소 검색후 적용
        // [2024-03-26 17:20:58]
        String infinispanBindAddr = System.getenv("INFINISPAN_BIND_ADDR");
        if (infinispanBindAddr == null || infinispanBindAddr.isEmpty()) {
            try {
                NetworkInterface networkInterface = NetworkInterface.getByName("en0");
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (inetAddress instanceof Inet4Address) {
                        infinispanBindAddr = inetAddress.getHostAddress();
                        System.out.println("Detected local IP address for INFINISPAN_BIND_ADDR: " + infinispanBindAddr);
                        break;
                    }
                }
            } catch (Exception e) {
                LOG.error("Failed to detect local IP address", e);
                throw new RuntimeException("Failed to detect local IP address", e);
            }
        }

        String infinispanInitialHosts = System.getenv("INFINISPAN_INITIAL_HOSTS");
        // String infinispanGossipRouterHosts = System.getenv("INFINISPAN_GOSSIP_ROUTER_HOSTS");
        // infinispan.gossip.router.hosts

        // System.out.println("INFINISPAN_STORE_PERSISTENCE_PATH: " + infinispanStorePersistencePath);
        // System.out.println("INFINISPAN_STORE_PERSISTENT_PATH: " + infinispanStorePersistentPath);
        // System.out.println("INFINISPAN_STORE_TEMPORARY_PATH: " + infinispanStoreTemporaryPath);
        // System.out.println("INFINISPAN_BIND_ADDR: " + infinispanBindAddr);
        // System.out.println("INFINISPAN_INITIAL_HOSTS: " + infinispanInitialHosts);
        // System.out.println("INFINISPAN_GOSSIP_ROUTER_HOSTS: " + infinispanGossipRouterHosts);

        System.setProperty("infinispan.gossip.router.hosts", System.getenv("INFINISPAN_GOSSIP_ROUTER_HOSTS"));
        System.setProperty("infinispan.bind_addr", System.getenv("INFINISPAN_BIND_ADDR"));
        System.setProperty("infinispan.cluster.name", System.getenv("INFINISPAN_CLUSTER_NAME"));
        System.setProperty("infinispan.node.name", System.getenv("INFINISPAN_NODE_NAME"));
        System.setProperty("infinispan.store.persistence.path", System.getenv("INFINISPAN_STORE_PERSISTENCE_PATH"));
        System.setProperty("infinispan.store.persistent.path", System.getenv("INFINISPAN_STORE_PERSISTENT_PATH"));
        System.setProperty("infinispan.store.temporary.path", System.getenv("INFINISPAN_STORE_TEMPORARY_PATH"));


        System.out.println("#infinispan.store.persistence.path: " + System.getenv("INFINISPAN_STORE_PERSISTENCE_PATH"));
        System.out.println("#infinispan.store.persistent.path: " + System.getenv("INFINISPAN_STORE_PERSISTENT_PATH"));
        System.out.println("#infinispan.store.temporary.path: " + System.getenv("INFINISPAN_STORE_TEMPORARY_PATH"));
        System.out.println("#infinispan.initial.hosts: " + System.getenv("INFINISPAN_INITIAL_HOSTS"));
        System.out.println("#infinispan.gossip.router.hosts: " + System.getenv("INFINISPAN_GOSSIP_ROUTER_HOSTS"));
        System.out.println("#infinispan.bind_addr: " + System.getenv("INFINISPAN_BIND_ADDR"));
        System.out.println("#infinispan.cluster.name: " + System.getenv("INFINISPAN_CLUSTER_NAME"));
        System.out.println("#infinispan.node.name: " + System.getenv("INFINISPAN_NODE_NAME"));

        String home = System.getProperty("user.home");
        LOG.info("### user.home ==>>> ",home);
        InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("infinispan-tcp-unencrypted.xml");
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

        File tempFile = new File(TEMP_STATE_FILE);
        // 재시작 로직
        if (tempFile.exists()) {
            System.out.println("This is restart..");

            String oraclizerFolderPath = "./oraclizer/" + System.getenv("INFINISPAN_NODE_NAME");
            File oraclizerFolder = new File(oraclizerFolderPath);
            
            if (oraclizerFolder.exists() && oraclizerFolder.isDirectory()) {
                // boolean deleted = oraclizerFolder.delete();
                // if (deleted) {
                //     LOG.info("폴더가 성공적으로 삭제되었습니다: " + oraclizerFolderPath);
                // } else {
                //     LOG.warn("폴더 삭제에 실패했습니다: " + oraclizerFolderPath);
                // }
                try {
                    Files.walk(oraclizerFolder.toPath())
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
                    LOG.info("폴더 및 하위 파일이 성공적으로 삭제되었습니다: " + oraclizerFolderPath);
                } catch (IOException e) {
                    LOG.error("폴더 및 하위 파일 삭제 중 오류 발생: " + oraclizerFolderPath, e);
                }
            } else {
                LOG.warn("폴더가 존재하지 않습니다: " + oraclizerFolderPath);
            }

        } else {
            // 처음 시작 로직
            System.out.println("This is the first start.");
            try {
                tempFile.createNewFile();
                tempFile.deleteOnExit(); // 애플리케이션 종료 시 파일 삭제
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        LOG.info("[START EmbeddedCacheManager SERVICE!!!]");
        cs.start();
        ;

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
