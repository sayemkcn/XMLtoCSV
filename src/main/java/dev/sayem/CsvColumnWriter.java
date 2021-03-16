package dev.sayem;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.List;

public class CsvColumnWriter {
    private File targetFile;
    private File workFile;

    private int[] lineInBytes;
    private OutputStream targetStream;
    private RandomAccessFile workRndAccFile;
    private boolean firstColWritten;

    public void writeNextCol(List<String> colOfValues) throws IOException {
        // we are going to create a new target file so we have to first
        // create a duplicated version
        copyFile(targetFile, workFile);

        this.targetStream = new BufferedOutputStream(new FileOutputStream(targetFile));

        int lineNo = 0;

        for (String nextColValue : colOfValues) {

            String nextChunk = nextColValue + ",";

            // before we add the next chunk to the current line,
            // we must retrieve the line from the duplicated file based on its the ofset and length
            int lineOfset = findLineOfset(lineNo);

            workRndAccFile.seek(lineOfset);

            int bytesToRead = lineInBytes[lineNo];
            byte[] curLineBytes = new byte[bytesToRead];
            workRndAccFile.read(curLineBytes);

            // now, we write the previous version of the line fetched from the
            // duplicated file plus the new chunk plus a 'new line' character
            targetStream.write(curLineBytes);
            targetStream.write(nextChunk.getBytes());
            targetStream.write("\n".getBytes());

            // update the length of the line
            lineInBytes[lineNo] += nextChunk.getBytes().length;

            lineNo++;
        }

        // Though I have not done it myself but obviously some code should be added here to care for the cases where
        // less column values have been provided in this method than the total number of lines

        targetStream.flush();
        workFile.delete();

        firstColWritten = true;
    }

    // finds the byte ofset of the given line in the duplicated file
    private int findLineOfset(int lineNo) {
        int ofset = 0;
        for (int i = 0; i < lineNo; i++)
            ofset += lineInBytes[lineNo] +
                    (firstColWritten ? 1 : 0); // 1 byte is added for '\n' if at least one column has been written
        return ofset;
    }

    // helper method for file copy operation
    public static void copyFile(File from, File to) throws IOException {
        FileChannel in = new FileInputStream(from).getChannel();
        FileChannel out = new FileOutputStream(to).getChannel();
        out.transferFrom(in, 0, in.size());
    }

    public CsvColumnWriter(File targetFile, File workFile, int lines) throws Exception {
        this.targetFile = targetFile;
        this.workFile = workFile;

        workFile.createNewFile();

        this.workRndAccFile = new RandomAccessFile(workFile, "rw");

        lineInBytes = new int[lines];
        for (int i = 0; i < lines; i++)
            lineInBytes[i] = 0;

        firstColWritten = false;
    }

}