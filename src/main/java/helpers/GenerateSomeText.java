package helpers;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.stream.IntStream;

public class GenerateSomeText {
    public static void main(String [] args){
        SecureRandom secureRandom = new SecureRandom();

        for(int i=0;i<5;i++) {
            IntStream intStream = secureRandom.ints(1024*1024*30, 0, 256);
            try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(
                Paths.get(String.format("file%d.txt", i))))) {
                intStream.map(x -> (byte) x).forEach(pw::write);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
