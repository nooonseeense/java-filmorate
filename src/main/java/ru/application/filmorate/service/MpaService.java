package ru.application.filmorate.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import ru.application.filmorate.dao.MpaDao;
import ru.application.filmorate.model.Mpa;

import java.util.List;

@Service
public class MpaService {
    MpaDao mpaDao;

    public List<Mpa> get() {
        return mpaDao;
    }

    public Mpa getById(@PathVariable int id) {
        return mpaDao;
    }
}
