import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPUDPClient2 {

    public static void main(String[] args) throws UnknownHostException {

        Socket socket = null;
        PrintWriter out = null;
        String address = "127.0.0.1";
        int port = 5005;

        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(address, port), 500);
            out = new PrintWriter(socket.getOutputStream(), true);
        }
        catch (UnknownHostException e) {
            System.out.println("Unknown host");
            System.exit(-1);
        }
        catch  (IOException e) {
            System.out.println("No I/O");
            System.exit(-1);
        }

        out.println("105425");

        int port1 = 80;
        UDPClient client = new UDPClient(port1);

        out.println(address + ":" + port1);

        String num1 = client.receiveMsg();
        String num2 = client.receiveMsg();
        String num3 = client.receiveMsg();

        System.out.println("Number 1 " + num1);
        System.out.println("Number 2 " + num2);
        System.out.println("Number 3 " + num3);

        int suma = Integer.parseInt(num1) + Integer.parseInt(num2) + Integer.parseInt(num3);

        client.sendMsg(String.valueOf(suma), UDPClient.address, UDPClient.port);

        String getRidOfTwo = client.receiveMsg();
        System.out.println("getRidOfTwo "+ getRidOfTwo);
        String no2 = getRidOfTwo.replace("2", "");
        client.sendMsg(no2, UDPClient.address, UDPClient.port);

        String tripleTrouble = client.receiveMsg();
        System.out.println("tripleTrouble " + tripleTrouble);

        String tOut = tripleTrouble + tripleTrouble + tripleTrouble;
        client.sendMsg(tOut, UDPClient.address, UDPClient.port);

        client.sendMsg(String.valueOf(port1), UDPClient.address, UDPClient.port);

        String flag = client.receiveMsg();
        System.out.println(flag);

        client.close();

        try {
            socket.close();
        }
        catch (IOException e) {
            System.out.println("Cannot close the socket");
            System.exit(-1);
        }

    }
}
