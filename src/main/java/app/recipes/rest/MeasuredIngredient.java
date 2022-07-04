package app.recipes.rest;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class MeasuredIngredient {
  private final String name;
  private final MeasurementUnitType unitType;
  private final Double value;

  @JsonCreator
  public MeasuredIngredient(
      @JsonProperty("name") String name,
      @JsonProperty("unit") MeasurementUnitType unitType,
      @JsonProperty("value") Double value) {
    this.name = name;
    this.unitType = unitType;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public MeasurementUnitType getUnitType() {
    return unitType;
  }

  public Double getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof MeasuredIngredient that)) return false;
    return Objects.equals(name, that.name)
        && unitType == that.unitType
        && Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, unitType, value);
  }
}
