package com.sivalabs.modernboot.jdbcclient;

import com.sivalabs.modernboot.domain.Bookmark;
import com.sivalabs.modernboot.domain.BookmarkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@JdbcTest(properties = {
   "spring.test.database.replace=none",
})
@Sql("classpath:/test_data.sql")
class BookmarkRepositoryTest {

    @Autowired
    JdbcClient jdbcClient;

    BookmarkRepository bookmarkRepository;

    @BeforeEach
    void setUp() {
        bookmarkRepository = new BookmarkRepository(jdbcClient);
    }

    @Test
    void shouldFindAllBookmarks() {
        List<Bookmark> bookmarks = bookmarkRepository.findAll();
        assertThat(bookmarks).isNotEmpty();
        assertThat(bookmarks).hasSize(6);
    }

    @Test
    void shouldCreateBookmark() {
        Bookmark bookmark = new Bookmark(null, "My Title", "https://sivalabs.in", Instant.now());
        Long id = bookmarkRepository.save(bookmark);
        assertThat(id).isNotNull();
    }

    @Test
    void shouldGetBookmarkById() {
        Bookmark bookmark = new Bookmark(null, "My Title", "https://sivalabs.in", Instant.now());
        Long id = bookmarkRepository.save(bookmark);

        Optional<Bookmark> bookmarkOptional = bookmarkRepository.findById(id);
        assertThat(bookmarkOptional).isPresent();
        assertThat(bookmarkOptional.get().id()).isEqualTo(id);
        assertThat(bookmarkOptional.get().title()).isEqualTo(bookmark.title());
        assertThat(bookmarkOptional.get().url()).isEqualTo(bookmark.url());
    }

    @Test
    void shouldEmptyWhenBookmarkNotFound() {
        Optional<Bookmark> bookmarkOptional = bookmarkRepository.findById(9999L);
        assertThat(bookmarkOptional).isEmpty();
    }

    @Test
    void shouldUpdateBookmark() {
        Bookmark bookmark = new Bookmark(null, "My Title", "https://sivalabs.in", Instant.now());
        Long id = bookmarkRepository.save(bookmark);

        Bookmark changedBookmark = new Bookmark(id, "My Updated Title", "https://www.sivalabs.in", bookmark.createdAt());
        bookmarkRepository.update(changedBookmark);

        Bookmark updatedBookmark = bookmarkRepository.findById(id).orElseThrow();
        assertThat(updatedBookmark.id()).isEqualTo(changedBookmark.id());
        assertThat(updatedBookmark.title()).isEqualTo(changedBookmark.title());
        assertThat(updatedBookmark.url()).isEqualTo(changedBookmark.url());
    }

    @Test
    void shouldDeleteBookmark() {
        Bookmark bookmark = new Bookmark(null, "My Title", "https://sivalabs.in", Instant.now());
        Long id = bookmarkRepository.save(bookmark);

        bookmarkRepository.delete(id);

        Optional<Bookmark> optionalBookmark = bookmarkRepository.findById(id);
        assertThat(optionalBookmark).isEmpty();
    }
}