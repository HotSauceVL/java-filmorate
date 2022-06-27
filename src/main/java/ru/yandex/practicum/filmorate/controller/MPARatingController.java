package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.dao.interfaces.MPARateDao;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
public class MPARatingController {
    private MPARateDao mpaRate;
    @Autowired
    private MPARatingController (MPARateDao mpaRate) {
        this.mpaRate = mpaRate;
    }

    @GetMapping
    public Collection<MPA> getAll() {
        return mpaRate.getAllMPA();
    }

    @GetMapping("/{id}")
    public MPA getById(@PathVariable int id) {
        return mpaRate.getMPAById(id);
    }
}
