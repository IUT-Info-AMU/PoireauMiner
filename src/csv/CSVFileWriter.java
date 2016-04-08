package csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVFileWriter {
    private BufferedWriter writer = null;
    private String separator = ";";

    public CSVFileWriter(String separator) {
        this.separator = separator;
    }

    public boolean open(String name) {
        try {
            File file = new File(name);
            if (!file.exists()) {
                file.createNewFile();
            }
            this.writer = new BufferedWriter(new FileWriter(file));
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    public void close() throws IOException {
    	this.writer.close();
    }

    public String format(String word) {
        word = word.replaceAll("\"", "\\\"");
        word = word.replaceAll("[\n\r\t,.;:!]", " ");
        word = "\"" + word + "\"";
        return word;
    }

    public void writeSet(List<String> set) throws IOException {
        String data = "";
        for (int i = 0; i < set.size(); ++i) {
            String word = format(set.get(i));
            if (word.equals("")) {
            	continue;
            }
            data += word;
            if (i != set.size() - 1) {
                data += separator;
            }
        }
        writer.write(data);
        writer.newLine();
        writer.flush();
    }
}
