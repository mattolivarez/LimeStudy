package dev.mattolivarez.Service;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.ClassModel;
import dev.mattolivarez.Repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ClassModel addClass(Integer userId, String class_name, Long class_created_on) throws BadRequestException {
        int categoryId = classRepository.create(userId, class_name, class_created_on);
        return classRepository.findById(userId, categoryId);
    }

    @Override
    public void updateClass(Integer userId, Integer categoryId, ClassModel classModel) throws BadRequestException {
        classRepository.update(userId, categoryId, classModel);
    }

    @Override
    public void removeClassWithAllDecks(Integer userId, Integer categoryId) throws ResourceNotFoundException {
        this.fetchClassById(userId, categoryId);
        classRepository.removeById(userId, categoryId);
    }
}
