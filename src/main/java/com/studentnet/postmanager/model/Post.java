package com.studentnet.postmanager.model;

import java.time.LocalDateTime;

/**
 * Клас-модель, що представляє сутність "Допис" (Post) у соціальній мережі.
 * Відповідає структурі таблиці posts у базі даних.
 */
public class Post {
    private int id;
    private int authorId;
    private int wallOwnerId;
    private String content;
    private int likesCount;
    private LocalDateTime createdAt;

    /**
     * Конструктор для створення нового допису перед його збереженням у БД.
     * ID, кількість лайків та дата створюються базою даних автоматично.
     *
     * @param authorId    ID автора допису
     * @param wallOwnerId ID користувача, на чиїй стіні розміщено допис
     * @param content     Текстовий вміст допису
     */
    public Post(int authorId, int wallOwnerId, String content) {
        this.authorId = authorId;
        this.wallOwnerId = wallOwnerId;
        this.content = content;
    }

    /**
     * Конструктор для ініціалізації об'єкта даними, отриманими з бази даних.
     *
     * @param id          Унікальний ідентифікатор допису
     * @param authorId    ID автора
     * @param wallOwnerId ID власника стіни
     * @param content     Текст допису
     * @param likesCount  Кількість лайків
     * @param createdAt   Дата та час створення
     */
    public Post(int id, int authorId, int wallOwnerId, String content, int likesCount, LocalDateTime createdAt) {
        this.id = id;
        this.authorId = authorId;
        this.wallOwnerId = wallOwnerId;
        this.content = content;
        this.likesCount = likesCount;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getAuthorId() { return authorId; }
    public void setAuthorId(int authorId) { this.authorId = authorId; }
    public int getWallOwnerId() { return wallOwnerId; }
    public void setWallOwnerId(int wallOwnerId) { this.wallOwnerId = wallOwnerId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public int getLikesCount() { return likesCount; }
    public void setLikesCount(int likesCount) { this.likesCount = likesCount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}