import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.server.ServerCloneException;

public class App {
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
         String fileName = args[0];
         String port = args[1];

         File cookieFile = new File(fileName);
         if (!cookieFile.exists()) {
            System.out.println("Where's the cookie?");
            System.exit(0);
         }

         //slide 8
         ServerSocket ss = new ServerSocket(Integer.parseInt(port));
         Socket socket = ss.accept();

         //slide 9 - allow server to read and write
         try(InputStream is = socket.getInputStream()) {
            /*The two classes are not mutually exclusive - you can use both of them if your needs suit. As you picked up, BufferedInputStream is about reading in blocks of data rather than a single byte at a time. It also provides the convenience method of readLine(). However, it's also used for peeking at data further in the stream then rolling back to a previous part of the stream if required (see the mark() and reset() methods).
            DataInputStream/DataOutputStream provides convenience methods for reading/writing certain data types. For example, it has a method to write/read a UTF String. If you were to do this yourself, you'd have to decide on how to determine the end of the String (i.e. with a terminator byte or by specifying the length of the string).
            This is different from BufferedInputStream's readLine() which, as the method sounds like, only returns a single line. writeUTF()/readUTF() deal with Strings - that string can have as many lines it it as it wants.
            BufferedInputStream is suitable for most text processing purposes. If you're doing something special like trying to serialize the fields of a class to a file, you'd want to use DataInput/OutputStream as it offers greater control of the data at a binary level. */
            
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);

            String msgReceived = "";

            try(OutputStream os = socket.getOutputStream()) {
                BufferedOutputStream bos = new BufferedOutputStream(os);
                DataOutputStream dos = new DataOutputStream(bos);

                //write our logic to receive and send
                while (!msgReceived.equals("close")) {
                    //slide 9
                    msgReceived = dis.readUTF();

                    if (msgReceived.equals("get-cookie")) {
                        //instantiate cookie.java
                        //generate random cookie
                        //send the random cookie out using DataOutputStream (dos.writeUTF())
                        Cookie cookie = new Cookie();
                        cookie.readCookieFile(fileName);
                        String randomCookie = cookie.getRandomCookie();
                        dos.writeUTF(randomCookie);
                        dos.flush();

                    }
                }

                //close all output streams in reverse order
                dos.close();
                bos.close();
                os.close();
            }  catch (EOFException ex) {
                ex.printStackTrace();
            }

            //close all input streams in reverse order
            dis.close();
            bis.close();
            is.close();
         } catch (EOFException ex) {
            socket.close();
            ss.close();
         }

         Cookie cookie = new Cookie();
         cookie.readCookieFile(fileName);
         String myCookie = cookie.getRandomCookie();
         System.out.println(myCookie);
         String myCookie2 = cookie.getRandomCookie();
         System.out.println(myCookie2);
    }
}
