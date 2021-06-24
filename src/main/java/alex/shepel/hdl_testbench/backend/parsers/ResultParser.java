package alex.shepel.hdl_testbench.backend.parsers;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ResultParser extends Parser {

    String folderPath;

    /**
     * The class constructor.
     *
     * @param folderPath The String object that contains a path to specified folder.
     */
    public ResultParser(String folderPath) {
        this.folderPath = folderPath;
    }

    public Map<String, Integer> getResultStats() throws IOException {
        final File folder = new File(folderPath);
        final File[] files = listFilesForFolder(folder);
        final Map<String, Integer> result = new HashMap<>();

        for (final File file: files)
            if (file.getName().contains("_mismatch")) {
                final String portName = file.getName().substring(0, file.getName().indexOf("_"));
                result.put(portName, countMismatches(file));
            }

        putCommonInfo(result, files);
        return result;
    }

    private void putCommonInfo(Map<String, Integer> result, File[] files) throws IOException {
        result.put("total_mismatches", countTotalMismatches(result));

        final File anyFile = files[0];
        final int portsCount = files.length / 2;
        final int framesPerPort = parseFile(anyFile).size();

        result.put("port_frames", framesPerPort);
        result.put("total_frames", framesPerPort * portsCount);
    }

    private int countTotalMismatches(final Map<String, Integer> mismatchesMap) {
        int totalCount = 0;

        for (final int mismatchesCount: mismatchesMap.values())
            totalCount += mismatchesCount;

        return totalCount;
    }

    private int countMismatches(File file) throws IOException {
        int mismatches = 0;

        for (String line: parseFile(file))
            if (line.equals("1"))
                mismatches++;

        return mismatches;
    }

    public File[] listFilesForFolder(final File folder) {
        ArrayList<File> files = new ArrayList<>();

        for (final File fileEntry : Objects.requireNonNull(folder.listFiles()))
            if (fileEntry.isDirectory())
                files.addAll(Arrays.asList(listFilesForFolder(fileEntry)));
            else
                files.add(fileEntry);

        return files.toArray(new File[0]);
    }
}
