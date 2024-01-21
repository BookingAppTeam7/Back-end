package com.booking.BookingApp.contollers;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.copy;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/files")
public class FileController {
    public static final String UPLOAD_DIRECTORY="./src/main/resources/images/";
    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("images") List<MultipartFile> files) throws IOException{
        List<String> filenames=new ArrayList<>();

        for(MultipartFile file:files){
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            Path fileStorage = get(UPLOAD_DIRECTORY, filename).toAbsolutePath().normalize();
            copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
            filenames.add(filename);
        }

        return ResponseEntity.ok().body(filenames);
    }

    @PostMapping("/uploadMobile")
    public ResponseEntity<List<String>> uploadFilesForMobile(@RequestPart("images") List<MultipartFile> files) throws IOException{
        List<String> filenames= new ArrayList<>();

        for(MultipartFile file:files){
            String filename=StringUtils.cleanPath(file.getOriginalFilename());
            Path fileStorage = get(UPLOAD_DIRECTORY, filename).toAbsolutePath().normalize();
            copy(file.getInputStream(), fileStorage, REPLACE_EXISTING);
            filenames.add(filename);
        }

        return ResponseEntity.ok().body(filenames);
    }
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        Path filePath = Paths.get(UPLOAD_DIRECTORY).resolve(filename).normalize();
        Resource resource;

        try {
            resource = new UrlResource(filePath.toUri());
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }

        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
