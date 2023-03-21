package ru.application.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.application.filmorate.storage.mpa.MpaDao;
import ru.application.filmorate.model.Mpa;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaDao mpaDao;

    public List<Mpa> get() {
        return mpaDao.get();
    }

    public Mpa getById(int id) {
        return mpaDao.getById(id);
    }
}
