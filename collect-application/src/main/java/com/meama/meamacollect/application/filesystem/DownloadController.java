package com.meama.meamacollect.application.filesystem;

import com.meama.filesystem.service.FileSystemService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/application/upload-api")
public class DownloadController {

    private final FileSystemService storageService;

    public DownloadController(FileSystemService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@RequestHeader HttpHeaders headers, @PathVariable String filename) throws Exception {
        Resource file = storageService.loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
