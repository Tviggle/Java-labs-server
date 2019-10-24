package MyServer;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import Tutorial.*;

public class MyHost {

        public static JTextArea fb = new JTextArea();
        private ServerSocket server;
        private Socket client;
        public static LinkedList<MiniServer> servers = new LinkedList<>();
        JFrame console= new JFrame("Консоль сервера");

        public MyHost(){
            console.setSize(600, 400);
            console.setDefaultCloseOperation(console.EXIT_ON_CLOSE);
            fb.setLineWrap(true);
            console.add(new JScrollPane(fb));
            console.setVisible(true);
            console.requestFocus();
            this.run();
        }

        public void run() {
            try {
                server = new ServerSocket(1212);

                while(true){
                    client = server.accept();
                    servers.add(new MiniServer(client));
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

