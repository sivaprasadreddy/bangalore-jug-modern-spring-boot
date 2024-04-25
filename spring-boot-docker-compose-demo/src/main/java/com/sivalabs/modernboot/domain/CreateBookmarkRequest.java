package com.sivalabs.modernboot.domain;

public record CreateBookmarkRequest(
        String title,
        String url) {
}
