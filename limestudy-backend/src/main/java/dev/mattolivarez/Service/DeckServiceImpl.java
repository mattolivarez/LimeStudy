package dev.mattolivarez.Service;

import dev.mattolivarez.Exception.BadRequestException;
import dev.mattolivarez.Exception.ResourceNotFoundException;
import dev.mattolivarez.Model.DeckModel;
import dev.mattolivarez.Repository.DeckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class DeckServiceImpl implements DeckService
{

    @Autowired
    DeckRepository deckRepository;

    @Override
    public List<DeckModel> fetchAllDecks(Integer userId, Integer classId) {
        return deckRepository.findAll(userId, classId);
    }

    @Override
    public DeckModel fetchDeckById(Integer userId, Integer classId, Integer deckId) throws ResourceNotFoundException {
        return deckRepository.findById(userId, classId, deckId);
    }

    @Override
    public DeckModel addDeck(Integer userId, Integer classId, String deck_name, Long deck_created_on) throws BadRequestException {
        int deckId = deckRepository.create(userId, classId, deck_name, deck_created_on);
        return deckRepository.findById(userId, classId, deckId);
    }

    @Override
    public void updateDeck(Integer userId, Integer classId, Integer deckId, DeckModel deckModel) throws BadRequestException {
        deckRepository.update(userId, classId, deckId, deckModel);
    }

    @Override
    public void removeDeck(Integer userId, Integer classId, Integer deckId) throws ResourceNotFoundException {
        deckRepository.removeById(userId, classId, deckId);
    }
}
