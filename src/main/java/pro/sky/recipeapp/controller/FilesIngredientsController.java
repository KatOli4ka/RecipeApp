package pro.sky.recipeapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.recipeapp.service.FilesService;

import java.io.*;

@Tag(name = "FilesIngredientsController", description = "API для файла с ингредиентами")
@RestController
@RequestMapping("/files")
public class FilesIngredientsController {
    private final FilesService filesService;

    public FilesIngredientsController(FilesService filesService) {
        this.filesService = filesService;
    }

    @Operation(summary = "СКАЧИВАНИЕ ФАЙЛА С ИНГРЕДИЕНТАМИ", description = "Скачивание файла с ингредиентами")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Скачивание прошло успешно!"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры!")
    })

    @GetMapping(value = "/export/ingredients")
    public ResponseEntity<InputStreamResource> exportIngredientsFile() throws FileNotFoundException {
        File ingredientsFile = filesService.getIngredientsFile();
        if (ingredientsFile.exists()) {
            InputStreamResource resourceI = new InputStreamResource(new FileInputStream(ingredientsFile));
            return ResponseEntity.ok().
                    contentType(MediaType.APPLICATION_JSON).
                    contentLength(ingredientsFile.length()).
                    header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"IngredientsLog.json\"").
                    body(resourceI);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @Operation(summary = "ЗАГРУЗКА ФАЙЛА С ИНГРЕДИЕНТАМИ", description = "Загрузка файла с ингредиентами")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Загрузка прошло успешно!"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры рецепта!")
    })
    @PostMapping(value = "/import/ingredients", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadIngredientsFile(@RequestParam MultipartFile fileIngredients) {
        filesService.cleanDataFileIngredients();
        File fileI = filesService.getIngredientsFile();
        try (FileOutputStream fos = new FileOutputStream(fileI)) {
            IOUtils.copy(fileIngredients.getInputStream(), fos);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

}
