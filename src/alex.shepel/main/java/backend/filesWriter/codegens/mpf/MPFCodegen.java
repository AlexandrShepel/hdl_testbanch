package backend.filesWriter.codegens.mpf;

import backend.parsers.Parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MPFCodegen extends ArrayList<String> {

    private int filesCount = 0;
    private String prjDir;
    private File dutFile;
    private final String mpfName;

    private static final String[] FILES_COUNT = {
            "Project_Files_Count = <files_count>",
    };

    private static final String[] FILE_DECLARATION = {
            "Project_File_<file_num> = <absolute_path>",
            "Project_File_P_<file_num> = cover_toggle 0 file_type systemverilog group_id 0 cover_exttoggle 0 " +
            "cover_nofec 0 cover_cond 0 vlog_1995compat SV vlog_nodebug 0 vlog_noload 0 folder <folder_name> " +
            "last_compile 0 cover_fsm 0 cover_branch 0 cover_excludedefault 0 vlog_enable0In 0 vlog_disableopt 0 " +
            "cover_covercells 0 voptflow 1 vlog_showsource 0 vlog_hazard 0 cover_optlevel 3 toggle - " +
            "vlog_0InOptions {} ood 1 cover_noshort 0 vlog_upper 0 compile_to work vlog_options {} " +
            "compile_order 0 cover_expr 0 dont_compile 0 cover_stmt 0",
    };

    public MPFCodegen(final String filePath) throws IOException {
        final ArrayList<String> pathElements = new ArrayList<>(Arrays.asList(filePath.split("/")));
        mpfName = pathElements.get(pathElements.size() - 1);

        final Parser parser = new Parser(filePath);
        addAll(parser.fileToArrayList());
    }

    public void setDutFile(File dutFile) {
        this.dutFile = dutFile;
    }

    public void setDirectory(File dir) {
        prjDir = dir.getAbsolutePath().replace("\\", "/");
    }

    public void setFiles(String[] filesNames) {
        addFilesCountDeclaration(filesNames.length + 1);

        addFileDeclaration(dutFile.getAbsolutePath().replace("\\", "/"), "{Top Level}");

        for (String fileName : filesNames)
            addFileDeclaration(prjDir + "/" + fileName, "tb");
    }

    private void addFileDeclaration(String path, String folder) {
        for (String line : FILE_DECLARATION) {
            line = line.replace("<file_num>", filesCount + "");
            line = line.replace("<absolute_path>", path);
            line = line.replace("<folder_name>", folder);
            add(size(), line);
        }

        add(size(), "");
        filesCount++;
    }

    private void addFilesCountDeclaration(int count) {
        for (final String line: FILES_COUNT)
            add(size(), line.replace("<files_count>", count + ""));
    }

    /**
     * Returns a name of the parsed file.
     *
     * @return The String value of parsed file name.
     */
    public String getName() {
        return mpfName;
    }

    /**
     * Returns a parsed file as ArrayList object
     * that contains all code lines of the file.
     *
     * @return The ArrayList object
     *         that contains parsed code.
     */
    public ArrayList<String> getParsedFile() {
        return this;
    }
}
