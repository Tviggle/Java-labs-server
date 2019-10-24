package MyServer;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MiniServer extends Thread{
    private Socket client;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    MiniServer(Socket client) throws IOException {
        this.client = client;
        MyHost.fb.append(client.getRemoteSocketAddress().toString() + " клиент подключился"+'\n');
        MyHost.fb.append("Онлайн " + (MyHost.servers.size()+1) + " клиентов"+'\n');
        out = new ObjectOutputStream(client.getOutputStream());
        in = new ObjectInputStream(client.getInputStream());
        start();
    }

    public void run(){
        try{

            while (true){
                Object tmp = in.readObject();
                if(tmp.equals("Exit")){
                    close();
                    break;
                }else if(tmp.equals("get list of users")){
                    sendIPs();
                }else if(tmp.equals("add objs")){
                    String toClient =  (String) in.readObject();
                    for(MiniServer sv : MyHost.servers) {
                        if (toClient.equals(sv.client.getRemoteSocketAddress().toString())) {
                            sv.sending("sending objs");
                            sv.sending(in.readObject());
                        }
                    }
                }

            }
        }catch(EOFException e1){}
         catch(IOException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    void close()
    {
        MyHost.fb.append(client.getRemoteSocketAddress().toString() + " Клиент отключился"+'\n');
        MyHost.servers.remove(this);
        MyHost.fb.append("Онлайн " + MyHost.servers.size() + " клиентов"+'\n');
        try {
            in.close();
            out.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendIPs(){
        sending("sending list");
        String users[] = new String[MyHost.servers.size()];
        int ct = 0;
        for(MiniServer sv : MyHost.servers){
            if(!sv.client.getRemoteSocketAddress().equals(client.getRemoteSocketAddress()))
                users[ct++] = sv.client.getRemoteSocketAddress().toString();
        }
        sending(users);
    }

    void sending(Object obj){
        try {
            out.writeObject(obj);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
