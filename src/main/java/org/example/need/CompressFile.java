package org.example.need;

import org.apache.commons.compress.archivers.zip.*;
import org.apache.commons.lang3.time.StopWatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

public class CompressFile {

    public static final String FILE_NAME = "file%d.txt";

    public static void main(String [] args) throws IOException, ExecutionException, InterruptedException {
        int threadPoolSize = Integer.parseInt(System.getenv().get("THREAD_POOL_SIZE"));

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ParallelScatterZipCreator scatterZipCreator = new ParallelScatterZipCreator(Executors.newFixedThreadPool(threadPoolSize));
        ScatterZipOutputStream dirs = ScatterZipOutputStream.fileBased(File.createTempFile("zip-dirs", "tmp"));

        System.out.println("compressing files using threadPoolSize="+threadPoolSize);

        for(int i=0;i<4;i++){
            addEntry(scatterZipCreator, FILE_NAME.formatted(i));
        }

        ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(new File("compressed-file.zip"));
        dirs.writeTo(zipArchiveOutputStream);
        dirs.close();
        scatterZipCreator.writeTo(zipArchiveOutputStream);

        stopWatch.stop();
        System.out.println("finished compressing file in :" + stopWatch);
    }

    private static void addEntry(ParallelScatterZipCreator scatterZipCreator, String filename) throws FileNotFoundException {
        ZipArchiveEntry zipArchiveEntry= new ZipArchiveEntry(filename);
        zipArchiveEntry.setMethod(ZipMethod.DEFLATED.getCode());
        FileInputStream fileInputStream = new FileInputStream(filename);


        scatterZipCreator.addArchiveEntry(zipArchiveEntry, () -> fileInputStream);
    }
}
