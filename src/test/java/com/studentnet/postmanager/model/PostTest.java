package com.studentnet.postmanager.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тестовий клас для перевірки валідації моделі Post.
 */
public class PostTest {

    @Test
    public void testPostCreation() {
        Post post = new Post(1, 2, "Тестовий контент");

        assertEquals(1, post.getAuthorId());
        assertEquals(2, post.getWallOwnerId());
        assertEquals("Тестовий контент", post.getContent());
    }

    @Test
    public void testSetContent() {
        Post post = new Post(1, 1, "Старий текст");
        post.setContent("Новий текст");
        assertEquals("Новий текст", post.getContent());
    }
}