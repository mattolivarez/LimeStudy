package dev.mattolivarez.Repository;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.Category;

import java.util.List;

public interface CategoryRepository
{
    List<Category> findAll(Integer userId) throws ResourceNotFoundException;

    Category findById(Integer userId, Integer categoryId) throws ResourceNotFoundException;

    Integer create(Integer userId, String title, String description) throws BadRequestException;

    void update(Integer userId, Integer categoryId, Category category) throws BadRequestException;

    void removeById(Integer userId, Integer categoryId);
}
