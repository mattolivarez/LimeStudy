package dev.mattolivarez.Repository;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.DeckModel;

import java.util.List;

public interface DeckRepository
{
    List<DeckModel> findAll(Integer userId, Integer classId);

    DeckModel findById(Integer userId, Integer classId, Integer deckId) throws ResourceNotFoundException;

    Integer create(Integer userId, Integer classId, String deck_name, Long deck_created_on) throws BadRequestException;

    void update(Integer userId, Integer classId, Integer deckId, DeckModel deckModel) throws BadRequestException;

    void removeById(Integer userId, Integer classId, Integer deckId) throws ResourceNotFoundException;
}
