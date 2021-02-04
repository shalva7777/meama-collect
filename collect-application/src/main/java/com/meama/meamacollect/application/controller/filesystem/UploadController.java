package com.meama.meamacollect.application.controller.filesystem;

import com.meama.common.request.DeleteFileRequest;
import com.meama.filesystem.service.FileSystemService;
import com.meama.meamacollect.application.filesystem.DownloadController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;

@ApiIgnore
@RestController
@RequestMapping(path = "api/filesystem")
public class UploadController {

    private final FileSystemService service;

    @Autowired
    public UploadController(FileSystemService service) {
        this.service = service;
    }

    @PostMapping("/upload")
    public Map<String, String> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        Map<String, String> res = new HashMap<>();
        res.put("Name", service.store(file));
        return res;
    }

    @GetMapping(value = "/load/{filename}/{suffix}")
    public Map<String, String> getListFiles(@RequestHeader HttpHeaders headers, @PathVariable String filename, @PathVariable String suffix) {
        Map<String, String> res = new HashMap<>();
        res.put("Name", MvcUriComponentsBuilder.fromMethodName(DownloadController.class, "getFile", headers, filename + "." + suffix).build().getPath());
        return res;
    }

    @PostMapping("/delete")
    public void deleteImage(@RequestBody DeleteFileRequest request) {
        service.deleteImage(request.getFileUrl());
    }
}
