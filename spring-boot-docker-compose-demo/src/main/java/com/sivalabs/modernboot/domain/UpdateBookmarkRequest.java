package com.sivalabs.modernboot.domain;

public record UpdateBookmarkRequest(
        Long id,
        String title,
        String url) {
}
