package csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVFileReader {
    private BufferedReader reader = null;
    private String separator = ";";

    public CSVFileReader(String separator) {
        this.separator = separator;
    }

    public boolean open(String name) {
        try {
            File file = new File(name);
            if (!file.exists()) {
                file.createNewFile();
            }
            this.reader = new BufferedReader(new FileReader(file));
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public String unformat(String word) {
        word = word.substring(1, word.length() - 1);
        word = word.replaceAll("\\\"", "\"");
        return word;
    }

    public List<String> readLine() {
        String data = null;
        try {
            data = reader.readLine();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        if (data == null) {
            return null;
        }
        List<String> line = new ArrayList<String>(Arrays.asList(data.split(separator)));
        for (String word : line) {
            word = unformat(word);
        }
        return line;
    }
}
