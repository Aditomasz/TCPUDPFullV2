import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class TCPUDPServer2 {

    public static class ServerThread extends Thread {
        private final Socket socket;

        public ServerThread(Socket socket) {
            super();
            this.socket = socket;
        }


        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                int initFlag = Integer.parseInt(in.readLine());
                if (initFlag == 105425){
                    String client=in.readLine();
                    String ip = client.split(":")[0];
                    int port = Integer.parseInt(client.split(":")[1]);
                    System.out.println(client);

                    int number, num_big, num_sm;
                    int sum_num=0;
                    String noTwoString, concatenationString;

                    UDPClient client1 = new UDPClient(5555);
                    Random rand = new Random();
                    for (int i=0; i < 3; i++) {
                        number = rand.nextInt(999999 - 100000) + 100000;
                        client1.sendMsg(String.valueOf(number), InetAddress.getByName(ip), port);
                        sum_num += number;
                    }

                    String odp = client1.receiveMsg();

                    String sum = String.valueOf(sum_num);

                    if (sum.equals(odp)){
                        num_big = rand.nextInt(999999999 - 100000000) + 100000000;
                        client1.sendMsg(String.valueOf(num_big),UDPClient.address, UDPClient.port);
                        noTwoString = String.valueOf(num_big).replace("2","");
                        String odp2 = client1.receiveMsg();
                        if (noTwoString.equals(odp2)){
                            num_sm = rand.nextInt(999 - 100) + 100;
                            client1.sendMsg(String.valueOf(num_sm),UDPClient.address, UDPClient.port);
                            concatenationString = num_sm + String.valueOf(num_sm) + num_sm;
                            String odp3 = client1.receiveMsg();
                            if (concatenationString.equals(odp3)) {
                                String odp4 = client1.receiveMsg();
                                if (odp4.equals(String.valueOf(UDPClient.port)))
                                    client1.sendMsg("Flag: " + 51515, UDPClient.address, UDPClient.port);
                                else
                                    client1.sendMsg("Game Over",UDPClient.address,UDPClient.port);
                            }
                            else
                                client1.sendMsg("Wrong" + odp3,UDPClient.address,UDPClient.port);
                        }
                        else
                            client1.sendMsg("Wrong: " + odp2,UDPClient.address,UDPClient.port);
                    }else
                         client1.sendMsg("Wrong: " + odp,UDPClient.address,UDPClient.port);


                    client1.close();
                }
                
            } catch (IOException e1) {
                // do nothing
                System.out.println('1');
            }

            try {
                socket.close();
            } catch (IOException e) {
                // do nothing
                System.out.println('2');
            }
        }
    }
    public void listenSocket(int port) {
        ServerSocket server = null;
        Socket client = null;
        try {
            server = new ServerSocket(port);
        }
        catch (IOException e) {
            System.out.println("Could not listen");
            System.exit(-1);
        }

        System.out.println("Server listens on port: " + server.getLocalPort());

        while(true) {
            try {
                client = server.accept();
            }
            catch (IOException e) {
                System.out.println("Accept failed");
                System.exit(-1);
            }

            (new ServerThread(client)).start();
        }

    }
    public static void main(String[] args) {
        if(args.length < 1)
        {
            System.out.println("Too few parameters: got " + args.length + ", expected 1");
            return;
        }
        int port = Integer.parseInt(args[0]);

        TCPUDPServer2 server = new TCPUDPServer2();
        server.listenSocket(port);
    }

}
