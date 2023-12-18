package com.my.hotel.controller;

import com.my.hotel.common.Constants;
import com.my.hotel.common.Routes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = Routes.URI_REST_IMAGE)
public class ImageController {

    @GetMapping("/{path}")
    public byte[] getPublicImage(@PathVariable("path") String path) throws IOException {
        String pathFile = Constants.PUBLIC_IMAGES_PATH + path;
        return Files.readAllBytes(Paths.get(pathFile));
    }

//    @PreAuthorize("hasAuthority('SYSTEM_MANAGEMENT')")
    @GetMapping("private/{path}")
    public byte[] getPrivateImage(@PathVariable("path") String path) throws IOException {
        String pathFile = Constants.PRIVATE_IMAGES_PATH + path;
        return Files.readAllBytes(Paths.get(pathFile));
    }

}
