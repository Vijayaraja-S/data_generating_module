package org.example.random_data_generater.writer;

import lombok.extern.slf4j.Slf4j;
import org.example.random_data_generater.bean.RequestBean;
import org.example.random_data_generater.export.CommonWriter;
import org.example.random_data_generater.util.FileUtil;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.UUID;


@Slf4j
@Component
public class WriterManager {
    private String outputPath;
    private String jobFolder;
    public CommonWriter getWriter(RequestBean inputBean) throws IOException {
         outputPath = getOutputPath(inputBean);
        return getCommonWriter(inputBean);
    }

    private CommonWriter getCommonWriter(RequestBean inputBean) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));
            return switch (inputBean.getOutputFormat()) {
                case CSV -> new CSVFileWriter(writer);
                case TXT, XLSX, JSON -> null;
            };
        }catch (IOException e){
            log.error("unable to create a writer");
        }
        return null;
    }

    private String getOutputPath(RequestBean inputBean) throws IOException {
        Date startTime = new Date();
        jobFolder = inputBean.getOutputPath() + File.separator + "DUMMY_DATA_" + startTime.getTime();
        FileUtil.checkCreateDirectory(jobFolder);
        return jobFolder + File.separator + "DUMMY_DATA" + UUID.randomUUID() + ".csv";
    }
    public boolean checkFileSize() throws IOException {
        return Files.size(Path.of(outputPath)) > 1000000;
    }
    public CommonWriter getFiles(RequestBean requestBean){
        outputPath = jobFolder + File.separator + "DUMMY_DATA" + UUID.randomUUID() + ".csv";
        return getCommonWriter(requestBean);
    }
}
