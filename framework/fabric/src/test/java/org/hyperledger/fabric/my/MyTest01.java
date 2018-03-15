package org.hyperledger.fabric.my;

import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric.sdkintegration.SampleStore;
import org.hyperledger.fabric.sdkintegration.SampleUser;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static org.junit.Assert.fail;

/**
 * @author 罗雷
 * @date 2018/3/7 0007
 * @time 11:15
 */
public class MyTest01 {

    private static final String CHAIN_CODE_NAME = "mycc";
    private static final String CHAIN_CODE_PATH = "github.com/hyperledger/fabric/examples/chaincode/go/chaincode_example02";
    private static final String CHAIN_CODE_VERSION = "1.0";

    public static void main(String[] args) throws Exception {
        File tempFile = File.createTempFile("teststore", "properties");
        tempFile.deleteOnExit();

        File sampleStoreFile = new File(System.getProperty("user.home") + "/test.properties");
        if (sampleStoreFile.exists()) { //For testing start fresh
            sampleStoreFile.delete();
        }
        final SampleStore sampleStore = new SampleStore(sampleStoreFile);

        SampleUser someTestUSER = sampleStore.getMember("admin", "org1", "Org1MSP",
                findFileSk("framework/fabric/src/main/resources/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/keystore"),
                new File("framework/fabric/src/main/resources/crypto-config/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp/signcerts/Admin@org1.example.com-cert.pem"));
        someTestUSER.setMspId("Org1MSP");
        HFClient hfclient = HFClient.createNewInstance();
        hfclient.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        hfclient.setUserContext(someTestUSER);
        final ChaincodeID chaincodeID;
        chaincodeID = ChaincodeID.newBuilder().setName(CHAIN_CODE_NAME)
                .setVersion(CHAIN_CODE_VERSION)
                .setPath(CHAIN_CODE_PATH).build();

        QueryByChaincodeRequest queryByChaincodeRequest = hfclient.newQueryProposalRequest();
        queryByChaincodeRequest.setArgs(new String[] { "query", "a"} );
        queryByChaincodeRequest.setFcn("query");
        queryByChaincodeRequest.setChaincodeID(chaincodeID);
        Map<String, byte[]> map = new HashMap<>();
        queryByChaincodeRequest.setTransientMap(map);

        Peer peer = hfclient.newPeer("Org1MSP", "grpc://172.20.69.145:7051");
        Channel mychannel = hfclient.newChannel("mychannel");
        mychannel.addPeer(peer);
        Collection<ProposalResponse> proposalResponses = mychannel.queryByChaincode(queryByChaincodeRequest);
        for (ProposalResponse proposalResponse : proposalResponses) {
            if (!proposalResponse.isVerified() || proposalResponse.getStatus() != ProposalResponse.Status.SUCCESS) {
                fail("Failed query proposal from peer " + proposalResponse.getPeer().getName() + " status: " + proposalResponse.getStatus() +
                        ". Messages: " + proposalResponse.getMessage()
                        + ". Was verified : " + proposalResponse.isVerified());
            } else {
                String payload = proposalResponse.getProposalResponse().getResponse().getPayload().toStringUtf8();
                System.out.println("Query payload of b from peer " + proposalResponse.getPeer().getName() + " returned " + payload);
            }
        }
    }

    static File findFileSk(String directorys) {

        File directory = new File(directorys);

        File[] matches = directory.listFiles((dir, name) -> name.endsWith("_sk"));

        if (null == matches) {
            throw new RuntimeException(format("Matches returned null does %s directory exist?", directory.getAbsoluteFile().getName()));
        }

        if (matches.length != 1) {
            throw new RuntimeException(format("Expected in %s only 1 sk file but found %d", directory.getAbsoluteFile().getName(), matches.length));
        }

        return matches[0];

    }

}
