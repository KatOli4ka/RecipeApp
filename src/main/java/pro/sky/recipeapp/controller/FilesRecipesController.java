package pro.sky.recipeapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
import pro.sky.recipeapp.model.Recipe;
import pro.sky.recipeapp.service.FilesService;
import pro.sky.recipeapp.service.RecipeService;

import java.io.*;

@Tag(name = "FilesRecipesController", description = "API для файла с рецептами")
@RestController
@RequestMapping("/files")
public class FilesRecipesController {
    private final FilesService filesService;
    private final RecipeService recipeService;

    public FilesRecipesController(FilesService filesService, RecipeService recipeService) {
        this.filesService = filesService;
        this.recipeService = recipeService;
    }

    @Operation(summary = "СКАЧИВАНИЕ ФАЙЛА С РЕЦЕПТАМИ", description = "Скачивание файла с рецептами")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Скачивание прошло успешно!"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры!")
    })

    @GetMapping("/export/recipes")
    public ResponseEntity<InputStreamResource> exportRecipesFile() throws FileNotFoundException {
        File recipesFile = filesService.getRecipesFile();
        if (recipesFile.exists()) {
            InputStreamResource resourceR = new InputStreamResource(new FileInputStream(recipesFile));
            return ResponseEntity.ok().
                    contentType(MediaType.APPLICATION_JSON).
                    contentLength(recipesFile.length()).
                    header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"RecipesLog.json\"").
                    body(resourceR);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @Operation(summary = "ЗАГРУЗКА ФАЙЛА С РЕЦЕПТАМИ", description = "Загрузка файла с рецептами")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Загрузка прошла успешно!"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры рецепта!")
    })
    @PostMapping(value = "/import/recipes", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadRecipesFile(@RequestParam MultipartFile fileRecipes) {
        filesService.cleanDataFileRecipes();
        File fileR = filesService.getRecipesFile();
        try (FileOutputStream fos = new FileOutputStream(fileR)) {
            IOUtils.copy(fileRecipes.getInputStream(), fos);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @Operation(summary = "СКАЧИВАНИЕ ФАЙЛА С РЕЦЕПТАМИ.TXT", description = "Скачивание файла с рецептами в формате txt")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Скачивание прошло успешно!", content = {
                    @Content(mediaType = "application/txt", array =
                    @ArraySchema(schema =
                    @Schema(implementation = Recipe.class)))
            }
            ),
            @ApiResponse(responseCode = "400", description = "Ошибка в параметрах запроса", content = {
                    @Content(mediaType = "application/txt", array =
                    @ArraySchema(schema =
                    @Schema(implementation = Recipe.class)))
            }
            ),
            @ApiResponse(responseCode = "404", description = "URL неверный или такого действия нет в веб-приложении",
                    content = {
                            @Content(mediaType = "application/txt", array =
                            @ArraySchema(schema =
                            @Schema(implementation = Recipe.class)))
                    }
            ),
            @ApiResponse(responseCode = "500", description = "Ошибка на сервере",
                    content = {
                            @Content(mediaType = "application/txt", array =
                            @ArraySchema(schema =
                            @Schema(implementation = Recipe.class)))
                    }
            )
    }
    )
    @GetMapping(value = "/download/recipes")
    public ResponseEntity<byte[]> downloadRecipesFile() {
        byte[] resourceR = recipeService.downloadRecipesFile();
        if (resourceR == null) {
            return ResponseEntity.badRequest().build();
        } else {
            return ResponseEntity.ok().
                    contentType(MediaType.TEXT_PLAIN).
                    contentLength(resourceR.length).
                    header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"RecipesLog.txt\"").
                    body(resourceR);
        }
    }

}
