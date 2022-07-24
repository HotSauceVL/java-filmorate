package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.mpa.MPARateDao;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MPARatingService {

    private final MPARateDao mpaRate;
    public Collection<MPA> getAllMPA() {
        return mpaRate.getAllMPA();
    }
    public MPA getMPAById(int id) {
        return mpaRate.getMPAById(id);
    }
}
