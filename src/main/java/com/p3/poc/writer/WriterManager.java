package com.p3.poc.writer;

import com.p3.poc.bean.RequestBean;
import com.p3.poc.export.CommonWriter;
import lombok.extern.slf4j.Slf4j;
import com.p3.poc.util.FileUtil;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;


@Slf4j
@Component
public class WriterManager {
    private String outputPath;
    private String jobFolder;
    private String fileName;
    private String blobFolderPath;

    public CommonWriter getWriter(RequestBean inputBean, String schemaPath) throws IOException {
        outputPath = getOutputPath(inputBean, schemaPath);
        return getCommonWriter(inputBean);
    }

    private CommonWriter getCommonWriter(RequestBean inputBean) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));
            return switch (inputBean.getOutputFormat()) {
                case CSV -> new CSVFileWriter(writer);
                case TXT, XLSX, JSON -> null;
            };
        } catch (IOException e) {
            log.error("unable to create a writer");
        }
        return null;
    }

    private String getOutputPath(RequestBean inputBean, String schemaPath) throws IOException {
        jobFolder = schemaPath + File.separator + inputBean.getTableName();
        FileUtil.checkCreateDirectory(jobFolder);
        blobFolderPath = jobFolder + File.separator + "blobs";
        fileName = inputBean.getTableName() + "_" + UUID.randomUUID() + ".csv";
        return jobFolder + File.separator + fileName;
    }

    public boolean checkFileSize() throws IOException {
        return Files.size(Path.of(outputPath)) > 1000000;
    }

    public CommonWriter getFiles(RequestBean inputBean) {
        fileName = inputBean.getTableName() + "_" + UUID.randomUUID() + ".csv";
        outputPath = jobFolder + File.separator + fileName;
        return getCommonWriter(inputBean);
    }
}
