
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    static final Logger LOG = Logger.getLogger(Main.class.getName());

    final static int BUFFER_SIZE = 1024;


    public void sendHttpRequest(Operation op) {
        Socket clientSocket = null;
        OutputStream os = null;
        InputStream is = null;

        try {
            clientSocket = new Socket("10.192.105.244", 8888);
            os = clientSocket.getOutputStream();
            is = clientSocket.getInputStream();

            Gson gson = new Gson();
            
            String httpString = gson.toJson(op);
            os.write(httpString.getBytes());

            ByteArrayOutputStream responseBuffer = new ByteArrayOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int newBytes;
            while ((newBytes = is.read(buffer)) != -1) {
                responseBuffer.write(buffer, 0, newBytes);
            }

            LOG.log(Level.INFO, "Response sent by the server: ");
            LOG.log(Level.INFO, responseBuffer.toString());
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                clientSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%5$s %n");

        Main client = new Main();
        client.sendHttpRequest(new Operation("2", "+", "4"));
        client.sendHttpRequest(new Operation("4", "*", "5"));
        client.sendHttpRequest(new Operation("5", "+", "1"));
    }
}


