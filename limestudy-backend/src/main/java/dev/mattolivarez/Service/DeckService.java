package dev.mattolivarez.Service;



import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.DeckModel;

import java.util.List;

public interface DeckService
{
    List<DeckModel> fetchAllDecks(Integer userId, Integer classId);

    DeckModel fetchDeckById(Integer userId, Integer classId, Integer deckId) throws ResourceNotFoundException;

    DeckModel addDeck(Integer userId, Integer classId, String deck_name, String deck_created_on) throws BadRequestException;

    void updateDeck(Integer userId, Integer classId, Integer deckId, DeckModel deckModel) throws BadRequestException;

    void removeDeck(Integer userId, Integer classId, Integer deckId) throws ResourceNotFoundException;
}
