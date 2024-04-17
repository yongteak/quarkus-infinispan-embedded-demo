package io.app;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import org.web3j.abi.EventEncoder;
// import org.web3j.abi.TypeReference;
// import org.web3j.abi.datatypes.Address;
// import org.web3j.abi.datatypes.Event;
// import org.web3j.abi.datatypes.generated.Uint256;
// import org.web3j.protocol.Web3j;
// import org.web3j.protocol.core.DefaultBlockParameterNumber;
// import org.web3j.protocol.core.methods.request.EthFilter;
// import org.web3j.protocol.core.methods.response.EthBlockNumber;
// import org.web3j.protocol.core.methods.response.EthLog;
// import org.web3j.protocol.core.methods.response.Log;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class OnchainApp {

    static final Logger LOG = LoggerFactory.getLogger(OnchainApp.class);
    @Inject
    @ConfigProperty(name = "blockchain.rpc-url", defaultValue = "https://go.getblock.io/82cb819aa8b14b9abd1d0c76e0ab1c68")
    String rpcUrl;

    // @ConfigProperty(name = "ORACLIZER_API_PORT", defaultValue = "8090")
    // rpc-url: "https://go.getblock.io/82cb819aa8b14b9abd1d0c76e0ab1c68"
    // contract-address: "0x0733618ab62eeec815f2d1739b7a50bf9e74d8a2"

    @Inject
    @ConfigProperty(name = "blockchain.contract-address", defaultValue = "0x0733618ab62eeec815f2d1739b7a50bf9e74d8a2")
    String contractAddress;

    // @Inject
    // @ConfigProperty(name = "blockchain.contract-abi-path")
    // String contractAbiPath;

    // private Web3j web3j;

    // @PostConstruct
    // void init() {
    void onStart(@Observes StartupEvent ev) {
        LOG.info("### Start Blockchain Service!");
        // try {
        //     this.web3j = Web3j.build(new HttpService(rpcUrl));
        //     // 연결 테스트를 위해 최신 블록 번호 조회

        //     BigInteger latestBlockNumber = getLatestBlockNumber();
        //     LOG.info("#### Latest block number: " + latestBlockNumber);
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
    }

    public BigInteger getLatestBlockNumber() throws Exception {
        return null;
        // EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().send();
        // return ethBlockNumber.getBlockNumber();
    }

    // public List<Log> getEvents(BigInteger blockNumber) throws Exception {
    //     String contractAddress = "0x0733618ab62eeec815f2d1739b7a50bf9e74d8a2";

    //     Event event = new Event("Transfer",
    //             Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {
    //             },
    //                     new TypeReference<Address>(true) {
    //                     },
    //                     new TypeReference<Uint256>(false) {
    //                     }));

    //     String encodedEventSignature = EventEncoder.encode(event);

    //     EthFilter filter = new EthFilter(
    //             new DefaultBlockParameterNumber(blockNumber),
    //             new DefaultBlockParameterNumber(blockNumber),
    //             contractAddress).addSingleTopic(encodedEventSignature);

    //     List<EthLog.LogResult> results = web3j.ethGetLogs(filter).send().getLogs();
    //     List<Log> logs = new ArrayList<>();
    //     for (EthLog.LogResult result : results) {
    //         logs.add((Log) result.get());
    //     }
    //     return logs;
    // }
}
