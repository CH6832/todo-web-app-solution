package ch.cern.todo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Objects;

/**
 * Data Transfer Object (DTO) for category operations in the Todo application.
 * This class represents the category data received from and sent to clients,
 * providing a simplified view of category information.
 *
 * Features:
 * - Input validation
 * - Size constraints
 * - Builder pattern
 * - Immutable object creation
 */
public class CategoryDTO {

    /**
     * Name of the category.
     * Must not be blank and should be between 1 and 100 characters.
     */
    @NotBlank(message = "Category name is required")
    @Size(min = 1, max = 100, message = "Category name must be between 1 and 100 characters")
    private String categoryName;

    /**
     * Description of the category.
     * Optional field with maximum length of 500 characters.
     */
    @Size(max = 500, message = "Category description cannot exceed 500 characters")
    private String categoryDescription;

    /**
     * Default constructor required for JSON deserialization.
     */
    public CategoryDTO() {
    }

    /**
     * Constructs a CategoryDTO with all fields.
     */
    public CategoryDTO(String categoryName, String categoryDescription) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
    }

    /**
     * Gets the category name.
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Gets the category description.
     */
    public String getCategoryDescription() {
        return categoryDescription;
    }

    /**
     * Sets the category name.
     */
    public void setCategoryName(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        this.categoryName = categoryName;
    }

    /**
     * Sets the category description.
     */
    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    /**
     * Returns a string representation of the CategoryDTO.
     */
    @Override
    public String toString() {
        return "CategoryDTO{" +
                "categoryName='" + categoryName + '\'' +
                ", categoryDescription='" + categoryDescription + '\'' +
                '}';
    }

    /**
     * Checks equality between this CategoryDTO and another object.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryDTO)) return false;
        CategoryDTO that = (CategoryDTO) o;
        return Objects.equals(categoryName, that.categoryName) &&
                Objects.equals(categoryDescription, that.categoryDescription);
    }

    /**
     * Generates a hash code for this CategoryDTO.
     */
    @Override
    public int hashCode() {
        return Objects.hash(categoryName, categoryDescription);
    }

    /**
     * Creates a builder for CategoryDTO.
     */
    public static CategoryDTOBuilder builder() {
        return new CategoryDTOBuilder();
    }

    /**
     * Builder class for creating CategoryDTO instances.
     */
    public static class CategoryDTOBuilder {
        private String categoryName;
        private String categoryDescription;

        /**
         * Private constructor to enforce the builder pattern.
         */
        private CategoryDTOBuilder() {
        }

        /**
         * Sets the category name.
         */
        public CategoryDTOBuilder categoryName(String categoryName) {
            this.categoryName = categoryName;
            return this;
        }

        /**
         * Sets the category description.
         */
        public CategoryDTOBuilder categoryDescription(String categoryDescription) {
            this.categoryDescription = categoryDescription;
            return this;
        }

        /**
         * Builds the CategoryDTO instance.
         */
        public CategoryDTO build() {
            return new CategoryDTO(categoryName, categoryDescription);
        }
    }

    /**
     * Validates the CategoryDTO instance.
     */
    public boolean isValid() {
        return categoryName != null &&
                !categoryName.trim().isEmpty() &&
                categoryName.length() <= 100 &&
                (categoryDescription == null || categoryDescription.length() <= 500);
    }
}