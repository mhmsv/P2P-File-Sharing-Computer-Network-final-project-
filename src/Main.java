import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) {

        //System.out.println("salalal");
        var transmitor = new transmitor();
        var peer =new peer(5555);
        var peer2 = new peer(5556);

        if (args[0].equals("-search")) {
            //downloading
            System.out.println("search: " + args[1]);
            peer.receive(args[1]);


        } else if ( args[0].equals("-upload")) {
            //uploading
            System.out.println("waiting for any peer to ask for this file with name: " + args[1] + " with path" + args[2]);
            try {
                transmitor.serving(args[1], args[2]);
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("enter in this format\n-torrent -upload filename path\n" +
                    " -search filename");
            System.out.println("WRONG ARGUMENTS");
        }
    }

}

 //  /Users/mohammad/a.txt
///Users/mohammad/Desktop/a.txt

//torrent -upload filename path
//torrent -search filename