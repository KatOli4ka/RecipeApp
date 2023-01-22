package pro.sky.recipeapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {
    private String name;
    private int cookingTime;
    private String unitTime;
    private List<Ingredient> ingredients;
    private List<String> steps;
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name).append("\r\n\n").append("\r\n\n").
                append("Время приготовления: ").append(cookingTime).append(" ").append(unitTime).append("\r\n\n").
                append("Ингредиенты: ").append("\r\n\n");
        for (Ingredient ingredient : ingredients) {
            stringBuilder.append("•").append(ingredient).append("\r\n\n");
        }
        stringBuilder.append("Инструкция приготовления: ").append("\r\n\n");
        for (String step : steps) {
            stringBuilder.append(step).append("\r\n\n");
        }
        return stringBuilder.toString();
    }
}
