package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPARatingService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MPARatingController {

    private final MPARatingService mpaRatingService;

    @GetMapping
    public Collection<MPA> getAll() {
        return mpaRatingService.getAllMPA();
    }

    @GetMapping("/{id}")
    public MPA getById(@PathVariable int id) {
        return mpaRatingService.getMPAById(id);
    }
}
