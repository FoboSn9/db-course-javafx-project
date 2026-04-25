package com.studentnet.postmanager.dao;

import com.studentnet.postmanager.model.Post;
import com.studentnet.postmanager.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Клас для роботи з базою даних (Data Access Object) для сутності Post.
 * Реалізує основні CRUD-операції та пошук з використанням PreparedStatement.
 */
public class PostRepository {

    /**
     * Додає новий допис у базу даних.
     *
     * @param post Об'єкт допису з даними для збереження
     * @throws SQLException якщо виникає помилка виконання SQL-запиту
     */
    public void create(Post post) throws SQLException {
        String sql = "INSERT INTO posts (author_id, wall_owner_id, content) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, post.getAuthorId());
            pstmt.setInt(2, post.getWallOwnerId());
            pstmt.setString(3, post.getContent());
            pstmt.executeUpdate();
        }
    }

    /**
     * Отримує всі дописи з бази даних, відсортовані за датою створення (від новіших до старіших).
     *
     * @return Список об'єктів Post
     * @throws SQLException якщо виникає помилка читання з БД
     */
    public List<Post> findAll() throws SQLException {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM posts ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                posts.add(new Post(
                        rs.getInt("id"), rs.getInt("author_id"), rs.getInt("wall_owner_id"),
                        rs.getString("content"), rs.getInt("likes_count"), rs.getTimestamp("created_at").toLocalDateTime()
                ));
            }
        }
        return posts;
    }

    /**
     * Оновлює текстовий вміст існуючого допису.
     *
     * @param post Об'єкт допису з оновленим текстом та дійсним ID
     * @throws SQLException якщо виникає помилка оновлення у БД
     */
    public void update(Post post) throws SQLException {
        String sql = "UPDATE posts SET content = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, post.getContent());
            pstmt.setInt(2, post.getId());
            pstmt.executeUpdate();
        }
    }

    /**
     * Видаляє допис з бази даних за його ідентифікатором.
     *
     * @param id Унікальний ідентифікатор допису
     * @throws SQLException якщо виникає помилка видалення з БД
     */
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM posts WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    /**
     * Виконує пошук дописів за ID автора та/або ключовим словом у тексті.
     *
     * @param authorId ID автора (може бути null, якщо пошук лише за текстом)
     * @param keyword  Ключове слово для пошуку в тексті (може бути null або порожнім)
     * @return Список знайдених дописів
     * @throws SQLException якщо виникає помилка виконання пошукового запиту
     */
    public List<Post> search(Integer authorId, String keyword) throws SQLException {
        List<Post> posts = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM posts WHERE 1=1 ");

        if (authorId != null) {
            sql.append("AND author_id = ? ");
        }
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND content ILIKE ? ");
        }
        sql.append("ORDER BY created_at DESC");

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (authorId != null) {
                pstmt.setInt(paramIndex++, authorId);
            }
            if (keyword != null && !keyword.trim().isEmpty()) {
                pstmt.setString(paramIndex++, "%" + keyword + "%");
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    posts.add(new Post(
                            rs.getInt("id"), rs.getInt("author_id"), rs.getInt("wall_owner_id"),
                            rs.getString("content"), rs.getInt("likes_count"), rs.getTimestamp("created_at").toLocalDateTime()
                    ));
                }
            }
        }
        return posts;
    }
}