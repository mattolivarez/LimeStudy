package dev.mattolivarez.Controller;

import dev.mattolivarez.Model.ClassModel;
import dev.mattolivarez.Service.ClassService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/classes")
public class ClassController
{

    @Autowired
    ClassService classService;

    @GetMapping("")
    public ResponseEntity<List<ClassModel>> getAllClasses(HttpServletRequest request)
    {
        int userId = (Integer) request.getAttribute("userId");
        List<ClassModel> classes = classService.fetchAllClasses(userId);
        return new ResponseEntity<>(classes, HttpStatus.OK);
    }

    @GetMapping("/{classId}")
    public ResponseEntity<ClassModel> getClassById(HttpServletRequest request, @PathVariable("classId") Integer classId)
    {
        int userId = (Integer) request.getAttribute("userId");
        ClassModel classModel = classService.fetchClassById(userId, classId);
        return new ResponseEntity<>(classModel, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ClassModel> addClass(HttpServletRequest request, @RequestBody Map<String, Object> classMap)
    {
        int userId = (Integer) request.getAttribute("userId");
        String class_name = (String) classMap.get("className");
        Long class_created_on = (Long) classMap.get("class_created_on");
        ClassModel classModel = classService.addClass(userId, class_name, class_created_on);
        return new ResponseEntity<>(classModel, HttpStatus.CREATED);
    }

    @PutMapping("/{classId}")
    public ResponseEntity<Map<String, Boolean>> updateClass(HttpServletRequest request,
                                                               @PathVariable("classId") Integer classId,
                                                               @RequestBody ClassModel classModel)
    {
        int userId = (Integer) request.getAttribute("userId");
        classService.updateClass(userId, classId, classModel);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @DeleteMapping("/{classId}")
    public ResponseEntity<Map<String, Boolean>> deleteClass(HttpServletRequest request,
                                                               @PathVariable("classId") Integer classId)
    {
        int userId = (Integer) request.getAttribute("userId");
        classService.removeClassWithAllDecks(userId, classId);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
