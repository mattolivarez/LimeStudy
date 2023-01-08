package dev.mattolivarez.Repository;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.ClassModel;

import java.util.List;

public interface ClassRepository
{
    List<ClassModel> findAll(Integer userId) throws ResourceNotFoundException;

    ClassModel findById(Integer userId, Integer classId) throws ResourceNotFoundException;

    Integer create(Integer userId, String name, Long class_created_on) throws BadRequestException;

    void update(Integer userId, Integer classId, ClassModel classModel) throws BadRequestException;

    void removeById(Integer userId, Integer classId);
}
