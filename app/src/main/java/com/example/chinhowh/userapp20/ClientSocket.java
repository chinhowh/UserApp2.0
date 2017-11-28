package com.example.chinhowh.userapp20;


import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ClientSocket {


    private final InetAddress serverIp;
    public final InputStreamReader input;
    public final OutputStreamWriter output;
    public final Socket socket;

    public ClientSocket() throws Exception {
        //伺服器位址
        serverIp = InetAddress.getByName("220.134.134.202");
        int serverPort = 5050;
        socket = new Socket(serverIp, serverPort);
        input = new InputStreamReader(socket.getInputStream());
        output = new OutputStreamWriter(socket.getOutputStream());
        //String dataIn = "";
        String dataOut = "";

        while (true) {
            //dataIn = input.readUTF();
            output.write(dataOut);

        }
    }
}
