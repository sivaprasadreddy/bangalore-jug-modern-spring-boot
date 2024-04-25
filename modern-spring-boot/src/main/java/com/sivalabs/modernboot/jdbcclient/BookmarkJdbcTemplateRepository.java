package com.sivalabs.modernboot.jdbcclient;

import com.sivalabs.modernboot.models.Bookmark;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
class BookmarkJdbcTemplateRepository {
    private final JdbcTemplate jdbcTemplate;

    BookmarkJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Bookmark> findAll() {
        String sql = "select id, title, url, created_at from bookmarks";
        return jdbcTemplate.query(sql, new BookmarkRowMapper());
    }

    public Optional<Bookmark> findById(Long id) {
        String sql = "select id, title, url, created_at from bookmarks where id = ?";
        return jdbcTemplate.query(sql, new BookmarkRowMapper(), id).stream().findFirst();
    }

    @Transactional
    public Long save(Bookmark bookmark) {
        String sql = """
                    insert into bookmarks(title, url, created_at)
                    values(?,?,?) returning id
                    """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, bookmark.title());
            pstmt.setString(2, bookmark.url());
            pstmt.setTimestamp(3, java.sql.Timestamp.from(bookmark.createdAt()));
            return pstmt;
        }, keyHolder);
        return keyHolder.getKeyAs(Long.class);
    }

    @Transactional
    public void update(Bookmark bookmark) {
        String sql = "update bookmarks set title = ?, url = ? where id = ?";
        int count = jdbcTemplate.update(sql, bookmark.title(), bookmark.url(), bookmark.id());
        if (count == 0) {
            throw new RuntimeException("Bookmark not found");
        }
    }

    @Transactional
    public void delete(Long id) {
        String sql = "delete from bookmarks where id = ?";
        int count = jdbcTemplate.update(sql, id);
        if (count == 0) {
            throw new RuntimeException("Bookmark not found");
        }
    }
}
