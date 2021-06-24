package alex.shepel.hdl_testbench.backend.parsers;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

abstract class Parser {

    /**
     * Parses file and saves it as ArrayList<String> object.
     *
     * @throws IOException Covers problems with reading of a specified file.
     */
    protected ArrayList<String> parseFile(String filePath) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
        ArrayList<String> result = toArrayList(inputStream);
        System.out.println("File parsed: " + filePath);
        return result;
    }

    /**
     * Parses file and saves it as ArrayList<String> object.
     *
     * @throws IOException Covers problems with reading of a specified file.
     */
    protected ArrayList<String> parseFile(File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        ArrayList<String> result = toArrayList(inputStream);
        System.out.println("File parsed: " + file);
        return result;
    }

    private ArrayList<String> toArrayList(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(inputStream)));
        ArrayList<String> parsedFile = new ArrayList<>();

        String newLine;
        while ((newLine = bufferedReader.readLine()) != null)
            parsedFile.add(newLine);

        inputStream.close();
        bufferedReader.close();

        return parsedFile;
    }

}
