import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Cookie {
    List<String> cookies = null;

    public void readCookieFile(String fileName) throws IOException{
            //instantiate cookies collection object
            this.cookies = new ArrayList<>();

            //use File object to pass fileName
            File filesName = new File(fileName);
            //File file = new File(File parent, String child);
            // doesn't create the file on disk until calling createNewFile() method

            if (!filesName.exists()) {
                System.out.println("Wheres the cookies?");
                System.exit(0);
            }

            //use FileReader and BufferReader for reading cookie file
            FileReader fr = new FileReader(filesName);
            BufferedReader br = new BufferedReader(fr);
            String input = "";

            //while loop to loop through the file
            //read each time and add into the cookies collection object
            while ((input = br.readLine()) != null) {
                this.cookies.add(input);
            }

            //close br and fr in reverse order
            br.close();
            fr.close();
    }

    public String getRandomCookie() {
        if (this.cookies == null) {
            return "Cookie. None left.";
        }
        Random rand = new Random();
        int cookieNo = rand.nextInt(this.cookies.size()-1);
        return cookies.get(cookieNo);
    }
}
