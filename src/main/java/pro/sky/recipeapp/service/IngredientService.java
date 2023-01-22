package pro.sky.recipeapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import pro.sky.recipeapp.model.Ingredient;
import pro.sky.recipeapp.model.Recipe;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class IngredientService {
    private final FilesService filesService;
    private long ingredientIdGenerator = 1;
    private Map<Long, Ingredient> ingredients = new HashMap<>();

    public IngredientService(FilesService filesService) {
        this.filesService = filesService;
    }

    @PostConstruct
    public void init() {
        readFromFileIngredients();
    }

    public Ingredient addIngredient(Ingredient ingredient) {
        ingredients.put(ingredientIdGenerator++, ingredient);
        saveToFileIngredients();
        return ingredient;
    }

    public Optional<Ingredient> getIngredientById(long ingredientId) {
        return Optional.ofNullable(ingredients.get(ingredientId));
    }

    public Optional<Ingredient> editing(long ingredientId, Ingredient ingredient) {
        saveToFileIngredients();
        return Optional.ofNullable(ingredients.replace(ingredientId, ingredient));
    }

    public Optional<Ingredient> delete(long ingredientId) {
        return Optional.ofNullable(ingredients.remove(ingredientId));
    }

    public Map<Long, Ingredient> getAll() {
        return new HashMap<>(ingredients);
    }

    private void saveToFileIngredients() {
        try {
            String json = new ObjectMapper().writeValueAsString(ingredients);
            filesService.saveToFileIngredients(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void readFromFileIngredients() {
        String json = filesService.readFromFileIngredients();
        try {
            ingredients = new ObjectMapper().readValue(json, new TypeReference<HashMap<Long, Ingredient>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
