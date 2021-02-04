package com.meama.filesystem.service;

import com.meama.meamacollect.filesystem.messages.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.transaction.NotSupportedException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileSystemImpl implements FileSystemService {

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final Path rootLocation = Paths.get("upload-dir");
//    private final Path rootLocation = Paths.get("upload-dir");


    @PostConstruct
    public void init() {
        try {
            if (!Files.exists(rootLocation)) {
                Path directory = Files.createDirectory(rootLocation);
                logger.info("create directory " + directory.toString());

            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize file system storage!");
        }
    }

    @Override
    public String store(MultipartFile file) throws Exception {
        String fileExtension;
        String extension = file.getContentType();
        System.out.println(extension);
        switch (extension) {
            case "image/jpeg":
                fileExtension = "jpg";
                break;
            case "image/png":
                fileExtension = "png";
                break;
            case "application/pdf":
                fileExtension = "pdf";
                break;
            case "application/msword":
                fileExtension = "doc";
                break;
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                fileExtension = "docx";
                break;
            case "application/vnd.ms-excel":
                fileExtension = "xls";
                break;
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                fileExtension = "xlsx";
                break;
            case "text/html":
                fileExtension = "html";
                break;
            case "audio/mpeg":
                fileExtension = "mp3";
                break;
            default:
                throw new NotSupportedException(Messages.get("NotSupportedFileType"));
        }
        String name = file.getOriginalFilename().split("\\.")[0];
        String modifiedFileName = name + "_" + System.currentTimeMillis() + "." + fileExtension;
        Files.copy(file.getInputStream(), this.rootLocation.resolve(modifiedFileName));
        logger.info("File saved successfully filename : {}", modifiedFileName);
        return modifiedFileName;
    }


    @Override
    public Resource loadFile(String filename) throws Exception {
        Path file = rootLocation.resolve(filename);
        Resource resource = new UrlResource(file.toUri());
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new FileNotFoundException();
        }
    }

    @Override
    public void deleteImage(String filename) {
        if (filename == null) {
            return;
        }
        File file = new File(filename);
        Path path = rootLocation.resolve(file.getName());
        FileSystemUtils.deleteRecursively(path.toFile());
        logger.info("File deleted successfully filename : {}", filename);
    }
}
