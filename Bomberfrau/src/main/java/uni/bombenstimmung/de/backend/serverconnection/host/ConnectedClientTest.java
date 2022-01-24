package uni.bombenstimmung.de.backend.serverconnection.host;

import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.net.SocketAddress;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConnectedClientTest {
    ConnectedClient server;
    ConnectedClient client1;
    ConnectedClient client2;
    ConnectedClient client3;

    @BeforeEach
    void setUp() throws Exception {
    }

    @Test
    void serverCreationTest() {
	server = new ConnectedClient(true, null);
	assumeTrue(server.isHost());
    }
    
    @Test
    void serverCreationTest2() {
	server = new ConnectedClient(true, "127.0.0.1");
	assumeTrue(server.isHost());
    }
    
    @Test
    void clientCreationTest() {
	client1 = new ConnectedClient(false, null);
	assumeFalse(client1.isHost());
    }
    
    @Test 
    void clientCreationTest2 () {
	client1 = new ConnectedClient(false, "127.0.0.1");
	assumeFalse(client1.isHost());
    }
    
    @Test
    void serverStackTest () {
	server = new ConnectedClient(true, null);
	Stack<Integer> serverIdStack = server.getIdStack();
	assertEquals(3, serverIdStack.size());
    }
    
    @Test
    void serverStackTest2 () {
	server = new ConnectedClient(true, null);
	client1 = new ConnectedClient(true, "127.0.0.1");
	Stack<Integer> serverIdStack = server.getIdStack();
	assertNotEquals(2, serverIdStack.size());
    }
    @Test
    void serverStackTest3 () {
	server = new ConnectedClient(true, null);
	client1 = new ConnectedClient(true, "127.0.0.1");
	assumeFalse(server.isIdStackEmpty());
    }
    
    @Test
    void serverHashMapTest () {
	server = new ConnectedClient(true, null);
	ConcurrentHashMap<SocketAddress, Integer> connectedClients =  server.getConnectedClients();
	assertEquals(0, connectedClients.size());
    }
    
   

}
