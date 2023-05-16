/*
Matthew Olivarez
Spring 2023
Senior Project
Limestudy Backend
Class Service Implementation file
*/

package dev.mattolivarez.Service;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.ClassModel;
import dev.mattolivarez.Repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ClassServiceImpl implements ClassService
{

    @Autowired
    ClassRepository classRepository;

    @Override
    public List<ClassModel> fetchAllClasses(Integer userId) {
        return classRepository.findAll(userId);
    }

    @Override
    public ClassModel fetchClassById(Integer userId, Integer classId) throws ResourceNotFoundException {
        return classRepository.findById(userId, classId);
    }

    @Override
    public ClassModel addClass(Integer userId, String class_name, String class_created_on) throws BadRequestException {
        int classId = classRepository.create(userId, class_name, class_created_on);
        return classRepository.findById(userId, classId);
    }

    @Override
    public void updateClass(Integer userId, Integer classId, ClassModel classModel) throws BadRequestException {
        classRepository.update(userId, classId, classModel);
    }

    @Override
    public void removeClassWithAllDecks(Integer userId, Integer classId) throws ResourceNotFoundException {
        //this.fetchClassById(userId, classId);
        classRepository.removeById(userId, classId);
    }
}
