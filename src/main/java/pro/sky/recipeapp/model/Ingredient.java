package pro.sky.recipeapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {
    private String name;
    private int count;
    private String measureUnit;
    @Override
    public String toString() {
        return name+ " - "+count+" "+measureUnit;
    }
}