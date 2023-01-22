package pro.sky.recipeapp.controller;

import org.springframework.web.bind.annotation.*;
import pro.sky.recipeapp.model.Recipe;
import pro.sky.recipeapp.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import pro.sky.recipeapp.model.Recipe;
import pro.sky.recipeapp.service.RecipeService;
import pro.sky.recipeapp.service.ValidateService;

import java.util.Map;

@Tag(name = "RecipeController", description = "API для рецептов")
@RestController
@RequestMapping("/recipe")
public class RecipeController {
    private final RecipeService recipeService;
    private final ValidateService validateService;

    public RecipeController(RecipeService recipeService,
                            ValidateService validateService) {
        this.recipeService = recipeService;
        this.validateService = validateService;
    }

    @Operation(summary = "ПОЛУЧЕНИЕ РЕЦЕПТА ПО ID", description = "Получение рецепта по id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Все прошло успешно!"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры рецепта!")
    })
    @GetMapping("/{recipeId}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable long recipeId) {
        return ResponseEntity.of(recipeService.getRecipeById(recipeId));
    }

    @Operation(summary = "ДОБАВЛЕНИЕ РЕЦЕПТА", description = "Добавление рецептов")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Добавление прошло успешно!"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры рецепта!")
    })
    @PostMapping
    public ResponseEntity<Recipe> addRecipe(@RequestBody Recipe recipe) {
        if (validateService.isNotValid(recipe)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(recipeService.addRecipe(recipe));
    }

    @Operation(summary = "РЕДАКТИРОВАНИЕ РЕЦЕПТА", description = "Редактирование рецепта")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Редактирование прошло успешно!"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры рецепта!")
    })
    @PutMapping("/{recipeId}")
    public ResponseEntity<Recipe> editing(@PathVariable long recipeId,
                                          @RequestBody Recipe recipe) {
        if (validateService.isNotValid(recipe)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.of(recipeService.editing(recipeId, recipe));
    }

    @Operation(summary = "УДАЛЕНИЕ РЕЦЕПТА", description = "Удаление рецепта")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Удаление прошло успешно!"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры ввода!")
    })
    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Recipe> delete(@PathVariable long recipeId) {
        return ResponseEntity.of(recipeService.delete(recipeId));
    }

    @Operation(summary = "ПОЛУЧЕНИЕ СПИСКА РЕЦЕПТОВ", description = "Получение всех рецептов")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Получение прошло успешно!"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры ввода!")
    })
    @GetMapping
    public Map<Long, Recipe> getAll() {
        return recipeService.getAll();
    }
}