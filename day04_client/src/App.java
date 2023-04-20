import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class App {
    public static void main(String[] args) throws Exception {
        
        String serverHost = args[0];
        String serverPort = args[1];

        // establish connection to server
        // **** server must be started first
        //https://stackoverflow.com/questions/508665/different-between-parseint-and-valueof-in-java
        //valueOf(String) returns a new Integer() object whereas parseInt(String) returns a primitive int
        Socket socket = new Socket(serverHost, Integer.parseInt(serverPort));


        /*set up console input from keyboard
         variable to save keyboard inputs
         variable to save msgReceived*/

        Console con = System.console();
        String keyboardInput = "";
        String msgReceived = "";


        try(InputStream is = socket.getInputStream()) {
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);


            try(OutputStream os = socket.getOutputStream()) {
                BufferedOutputStream bos = new BufferedOutputStream(os);
                DataOutputStream dos = new DataOutputStream(bos);
                
                //while loop here
                while(!keyboardInput.equals("close")) {
                    keyboardInput = con.readLine("Enter a command: ");

                    //send message across through the communication tunnel
                    dos.writeUTF(keyboardInput);
                    dos.flush();

                    //receive message from (response) and process it
                    //Reads in a string that has been encoded using a modified UTF-8 format. 
                    //The general contract of readUTF is that it reads a representation of a Unicode character string encoded in modified UTF-8 format; 
                    //this string of characters is then returned as a String.
                    msgReceived = dis.readUTF();
                    System.out.println(msgReceived);
                }

                //close output stream in reverse order
                dos.close();
                bos.close();
                os.close();

            } catch (EOFException ex) {
                socket.close();
            }


            // closes the input stream in reverse order
            dis.close();
            bis.close();
            is.close();

        } catch (EOFException ex) {
            ex.printStackTrace();
            socket.close();
        }
    }
}
