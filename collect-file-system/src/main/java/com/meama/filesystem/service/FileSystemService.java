package com.meama.filesystem.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileSystemService {

    String store(MultipartFile file) throws Exception;

    Resource loadFile(String filename) throws Exception;

    void deleteImage(String filename);
}
