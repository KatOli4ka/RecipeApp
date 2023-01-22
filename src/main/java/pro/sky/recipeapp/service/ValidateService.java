package pro.sky.recipeapp.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pro.sky.recipeapp.model.Ingredient;
import pro.sky.recipeapp.model.Recipe;

@Service
public class ValidateService {
    public boolean isNotValid(Recipe recipe) {
        boolean result = StringUtils.isBlank(recipe.getName()) ||
                StringUtils.isBlank(recipe.getUnitTime()) ||
                CollectionUtils.isEmpty(recipe.getIngredients()) ||
                CollectionUtils.isEmpty(recipe.getSteps()) ||
                recipe.getCookingTime() < 0;
        if (!result) {
            for (Ingredient ingredient : recipe.getIngredients()) {
                result = result || isNotValid(ingredient);
            }
        }
        return result;
    }

    public boolean isNotValid(Ingredient ingredient) {
        return StringUtils.isBlank(ingredient.getName()) ||
                StringUtils.isBlank(ingredient.getMeasureUnit()) ||
                ingredient.getCount() < 0;
    }
}
