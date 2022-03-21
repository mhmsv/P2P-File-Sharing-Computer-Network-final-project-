import javax.imageio.IIOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;

public class transmitor {

    public transmitor() {
        System.out.println("transmitor made");
    }

    public void serving(String file,String path) throws SocketException, UnknownHostException {
        try {
            //waiting for someone to ask 0.0.0.0
            var serverSC = new DatagramSocket(5555, InetAddress.getByName("0.0.0.0"));
            byte[] buffer = new byte[100];
            DatagramPacket packet = new DatagramPacket(buffer, 0, buffer.length);
            serverSC.receive(packet);

            //adding buffer to a string builder char by char
            String requested_file = getRequestedFileName(data(buffer));

            //String requested_file = getRequestedFileName(sb);
//            System.out.println("");
            System.out.println("Request:" + requested_file);

            //checking that the file is available or not
            if (file.equals(requested_file)) {
                System.out.println("yes there is peer available that has your file");

                //UNIVERSAL PLUG AND PLAY PORTS 1900 -5000
                var ssocket = new DatagramSocket(1900);
                var f = new FileReader(path);
                var br = new BufferedReader(f);
                var ff = new File(path);
                long l = ff.length();

                for (int j = 0; j < Math.ceil(l / 8) + 1; j++) {
                    char[] chr = new char[8];
                    String line = "chunk=" + (j) + "=" + String.valueOf(chr);
                    byte[] buf2 = line.getBytes();
                    DatagramPacket ppacket = new DatagramPacket(buf2,0,buf2.length, packet.getAddress(), packet.getPort());
                    ssocket.send(ppacket);
                }
                String line = "-End Of File-";
                byte[] buf2 = line.getBytes();
                DatagramPacket ppacket = new DatagramPacket(buf2,0,buf2.length, packet.getAddress(), packet.getPort());
                ssocket.send(ppacket);
                ssocket.send(ppacket);
                //ssocket.close();
                br.close();
                f.close();
            } else {
                System.out.println("---UNFORTUNATLY its invalid file---");
            }


        }
        catch (IOException e){
         e.printStackTrace();
        }
    }
    String getRequestedFileName(StringBuilder req) {
        String t = req.toString();
        //spliting by crlf
        String[] lines = t.split("\\r?\\n");
        if (lines.length == 2 && lines[0].equals("request file name:")) {
            return lines[1];
        }
        return ":(((( nothing";
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


}
