package pro.sky.recipeapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.recipeapp.model.Ingredient;
import pro.sky.recipeapp.service.IngredientService;
import pro.sky.recipeapp.service.ValidateService;

import java.util.Map;

@Tag(name = "IngredientController", description = "API для ингредиентов")
@RestController
@RequestMapping("/ingredient")
public class IngredientController {
    private final IngredientService ingredientService;
    private final ValidateService validateService;

    public IngredientController(IngredientService ingredientService,
                                ValidateService validateService) {
        this.ingredientService = ingredientService;
        this.validateService = validateService;
    }

    @Operation(summary = "ПОЛУЧЕНИЕ ИНГРЕДИЕНТА ПО ID", description = "Получение ингредиентов по id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Все прошло успешно!"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры ингредиента!")
    })
    @GetMapping("/{ingredientId}")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable long ingredientId) {
        return ResponseEntity.of(ingredientService.getIngredientById(ingredientId));
    }

    @Operation(summary = "ДОБАВЛЕНИЕ ИНГРЕДИЕНТА", description = "Добавление ингредиентов")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Добавление прошло успешно!"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры рецепта!")
    })
    @PostMapping
    public ResponseEntity<Ingredient> addIngredient(@RequestBody Ingredient ingredient) {
        if (validateService.isNotValid(ingredient)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(ingredientService.addIngredient(ingredient));
    }

    @Operation(summary = "РЕДАКТИРОВАНИЕ ИНГРЕДИЕНТА", description = "Редактирование ингредиентов")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Все прошло успешно!"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры игредиента!")
    })
    @PutMapping("/{ingredientId}")
    public ResponseEntity<Ingredient> editing(@PathVariable long ingredientId,
                                              @RequestBody Ingredient ingredient) {
        if (validateService.isNotValid(ingredient)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.of(ingredientService.editing(ingredientId, ingredient));
    }

    @Operation(summary = "УДАЛЕНИЕ ИНГРЕДИЕНТА", description = "Удаление ингредиента")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Удаление прошло успешно!"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры игредиента!")
    })
    @DeleteMapping("/{ingredientId}")
    public ResponseEntity<Ingredient> delete(@PathVariable long ingredientId) {
        return ResponseEntity.of(ingredientService.delete(ingredientId));
    }

    @Operation(summary = "ПОЛУЧЕНИЕ СПИСКА ИНГРЕДИЕНТОВ", description = "Получение всех ингредиентов")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Получение прошло успешно!"),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры игредиента!")
    })
    @GetMapping
    public Map<Long, Ingredient> getAll() {
        return ingredientService.getAll();
    }


}