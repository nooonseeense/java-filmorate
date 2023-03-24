package ru.application.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.application.filmorate.storage.impl.MpaDao;
import ru.application.filmorate.model.Mpa;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaDao mpaDao;

    /**
     * Метод получения списка всех MPA
     *
     * @return Список всех MPA
     */
    public List<Mpa> get() {
        return mpaDao.get();
    }

    /**
     * Метод получения MPA по ID
     *
     * @param id id MPA
     * @return Объект MPA
     */
    public Mpa get(int id) {
        return mpaDao.get(id);
    }
}
