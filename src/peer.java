import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class peer {
    String data = "";
    int chunk = 0;
    int portnumber=0;
    public peer(int port) {
        this.portnumber=port;
    }


    public static StringBuilder data(byte[] a) {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0)
        {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }

    void append_data(StringBuilder input) {
        String str = input.toString();
        String[] lines = str.split("=");
        if (lines[0].equals("chunk")) {
            int chunknum = Integer.valueOf(lines[1]);
            if (chunknum == chunk) {
                System.out.println("writing current chunk number :"+ chunk);
                data += str.substring(lines[0].length() + lines[1].length() + 2);
                chunk += 1;
            } else {
                System.out.println("we dont have this offset");
            }
        }
    }

    public void receive(String file) {

        try {

            DatagramSocket socket = new DatagramSocket();
            //System.out.println("file voorodi tabe " +file);
            String req = "request file name:\n" + file;//line 1 and line 2
            byte[] buf = req.getBytes();

            DatagramPacket packet = new DatagramPacket(buf,0,buf.length, InetAddress.getByName("255.255.255.255"), portnumber);
            socket.send(packet);

            byte[] receive_bytes = new byte[100];
            DatagramPacket DpReceive;
            while (!data(receive_bytes).toString().equals("-End Of File-"))
            {
                System.out.println("oomad dakhel while");
                receive_bytes = new byte[100];
                DpReceive = new DatagramPacket(receive_bytes, receive_bytes.length);

                socket.receive(DpReceive);

                System.out.println("peer sends: " + data(receive_bytes));
                append_data(data(receive_bytes));

            }
            System.out.println("reached end of file");
            System.out.println("writing on disk");
            FileWriter fw = new FileWriter("received_file_" + file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data);
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
//         "p2p -receive [name]\n" +
//                 "p2p –serve -name [name] -path [path]"
//    java main -serve -name test2 -path test2.rtf