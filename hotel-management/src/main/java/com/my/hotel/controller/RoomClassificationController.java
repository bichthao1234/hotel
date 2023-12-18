package com.my.hotel.controller;

import com.my.hotel.common.Routes;
import com.my.hotel.dto.ImageRoomClassificationDto;
import com.my.hotel.dto.RequestNewRoomClassification;
import com.my.hotel.dto.RoomClassificationDto;
import com.my.hotel.dto.request.CreateNewRoomClassPriceRequestDto;
import com.my.hotel.service.ImageRoomClassificationService;
import com.my.hotel.service.RoomClassificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = Routes.URI_REST_ROOM_CLASSIFICATION)
public class RoomClassificationController {
    @Autowired
    private RoomClassificationService roomClassificationService;
    @Autowired
    private ImageRoomClassificationService imageRoomClassificationService;

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") String id) {
        RoomClassificationDto roomClassificationDto = this.roomClassificationService.getById(id);
        return new ResponseEntity<>(roomClassificationDto, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        List<RoomClassificationDto> roomClassificationDtos = this.roomClassificationService.getAll();
        return new ResponseEntity<>(roomClassificationDtos, HttpStatus.OK);
    }

    @GetMapping("/getAllPrice")
    public ResponseEntity<?> getAllPrice() {
        List<?> roomClassificationDtos = this.roomClassificationService.getAllPrice();
        return new ResponseEntity<>(roomClassificationDtos, HttpStatus.OK);
    }

    @PostMapping("/add-new-price")
    public ResponseEntity<?> createNewPrice(@RequestBody CreateNewRoomClassPriceRequestDto requestDto) {
        log.info("API createNewPrice STARTED with INPUT requestDto={}", requestDto);
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = roomClassificationService.createNewPrice(requestDto);
            response.put("result", result);
            log.info("API createNewPrice END with OUTPUT response={}", response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.info("API createNewPrice END with OUTPUT error={}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create-new")
    public ResponseEntity<?> createNewRoomClassification(@RequestBody RequestNewRoomClassification requestNewRoomClassification) {
        Map<String, Object> response = new HashMap<>();
        try {
            String result = roomClassificationService.createNewRoomClassification(requestNewRoomClassification);
            response.put("result", result);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    @PostMapping("/edit-room-classification")
    public ResponseEntity<?> editRoomClassification(@RequestBody RequestNewRoomClassification requestNewRoomClassification) {
        Map<String, Object> response = new HashMap<>();
        try {
            String result = roomClassificationService.editRoomClassification(requestNewRoomClassification);
            response.put("result", result);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    @Transactional
    @PostMapping("/add-image")
//    @PreAuthorize("hasAuthority(T(com.my.hotel.common.Authority).SYSTEM_MANAGEMENT)")
    public ResponseEntity<?> addImageForRoomClassification(@RequestParam("image") MultipartFile image,
                                                           @RequestParam("roomClassificationId") String roomClassificationId) throws IOException {
        if (!image.isEmpty()) {
            String uploadDir = "src/main/resources/images/public";

            String originalFilename = image.getOriginalFilename();

            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            String filename = "ROOM_CLASSIFICATION_" + roomClassificationId.toUpperCase() + extension;

            Path uploadPath = Paths.get(uploadDir, filename);

            if (!Files.exists(uploadPath.getParent())) {
                Files.createDirectories(uploadPath.getParent());
            }
            if (Files.exists(uploadPath)) {
                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, uploadPath, StandardCopyOption.REPLACE_EXISTING);
                    ImageRoomClassificationDto imageRoomClassificationDto = new ImageRoomClassificationDto();
                    imageRoomClassificationDto.setUrl(filename);
                    RoomClassificationDto roomClassificationDto = new RoomClassificationDto();
                    roomClassificationDto.setId(roomClassificationId);
                    imageRoomClassificationDto.setRoomClassificationDto(roomClassificationDto);
                    imageRoomClassificationService.save(imageRoomClassificationDto);
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
            } else {
                try (InputStream inputStream = image.getInputStream()) {
                    Files.copy(inputStream, uploadPath);
                    ImageRoomClassificationDto imageRoomClassificationDto = new ImageRoomClassificationDto();
                    imageRoomClassificationDto.setUrl(filename);
                    RoomClassificationDto roomClassificationDto = new RoomClassificationDto();
                    roomClassificationDto.setId(roomClassificationId);
                    imageRoomClassificationDto.setRoomClassificationDto(roomClassificationDto);
                    imageRoomClassificationService.save(imageRoomClassificationDto);
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
            }
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.badRequest().body("File is empty!");
        }
    }
}
