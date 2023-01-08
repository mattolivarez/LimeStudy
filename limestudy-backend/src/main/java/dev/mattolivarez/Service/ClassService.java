package dev.mattolivarez.Service;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.ClassModel;

import java.util.List;

public interface ClassService
{
    List<ClassModel> fetchAllClasses(Integer userId);

    ClassModel fetchClassById(Integer userId, Integer classId) throws ResourceNotFoundException;

    ClassModel addClass(Integer userId, String class_name, Long class_created_on) throws BadRequestException;

    void updateClass(Integer userId, Integer classId, ClassModel classModel) throws BadRequestException;

    void removeClassWithAllDecks(Integer userId, Integer classId) throws ResourceNotFoundException;
}
