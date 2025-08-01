package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.base.BaseStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaDbStorage extends BaseStorage<Mpa> implements MpaStorage {

    private static final String FIND_ALL_QUERY = "select * from mpa_ratings";
    private static final String FIND_BY_ID_QUERY = "select * from mpa_ratings where mpa_rating_id = ?";

    public MpaDbStorage(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Optional<Mpa> getMpaById(int mpaId) {
        return findOne(FIND_BY_ID_QUERY, mpaId);
    }

    @Override
    public List<Mpa> getAllMpa() {
        return findMany(FIND_ALL_QUERY);
    }
}
