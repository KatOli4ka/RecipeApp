package pro.sky.recipeapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import pro.sky.recipeapp.model.Recipe;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class RecipeService {
    private final FilesService filesService;
    private long recipeIdGenerator = 1;
    private Map<Long, Recipe> recipes = new HashMap<>();

    public RecipeService(FilesService fileService) {
        this.filesService = fileService;
    }

    @PostConstruct
    public void init() {
        readFromFileRecipes();
    }

    public Recipe addRecipe(Recipe recipe) {
        recipes.put(recipeIdGenerator++, recipe);
        saveToFileRecipes();
        return recipe;
    }

    public Optional<Recipe> getRecipeById(long recipeId) {
        return Optional.ofNullable(recipes.get(recipeId));
    }

    public Optional<Recipe> editing(long recipeId, Recipe recipe) {
        saveToFileRecipes();
        return Optional.ofNullable(recipes.replace(recipeId, recipe));
    }

    public Optional<Recipe> delete(long recipeId) {
        return Optional.ofNullable(recipes.remove(recipeId));
    }

    public Map<Long, Recipe> getAll() {
        return new HashMap<>(recipes);
    }

    private void saveToFileRecipes() {
        try {
            String json = new ObjectMapper().writeValueAsString(recipes);
            filesService.saveToFileRecipes(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void readFromFileRecipes() {
        String json = filesService.readFromFileRecipes();
        try {
            recipes = new ObjectMapper().readValue(json, new TypeReference<HashMap<Long, Recipe>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] downloadRecipesFile() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Recipe recipe : recipes.values()) {
            stringBuilder.append(recipe).append("\r\n\n").append("*********************").append("\r\n\n").append("\r\n\n");
        }
        return stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }
}
