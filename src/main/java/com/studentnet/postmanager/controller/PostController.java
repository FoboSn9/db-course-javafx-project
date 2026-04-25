package com.studentnet.postmanager.controller;

import com.studentnet.postmanager.dao.PostRepository;
import com.studentnet.postmanager.model.Post;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Контролер інтерфейсу користувача (JavaFX).
 * Обробляє події натискання кнопок та взаємодіє з базою даних через PostRepository.
 */
public class PostController {

    @FXML private TableView<Post> postTable;
    @FXML private TableColumn<Post, Integer> idCol;
    @FXML private TableColumn<Post, Integer> authorCol;
    @FXML private TableColumn<Post, Integer> wallCol;
    @FXML private TableColumn<Post, String> contentCol;
    @FXML private TableColumn<Post, Integer> likesCol;
    @FXML private TableColumn<Post, LocalDateTime> dateCol;

    @FXML private TextField searchAuthorIdField;
    @FXML private TextField searchKeywordField;
    @FXML private TextField authorIdField;
    @FXML private TextField wallOwnerIdField;
    @FXML private TextField contentField;

    private final PostRepository repository = new PostRepository();
    private final ObservableList<Post> postData = FXCollections.observableArrayList();
    private Post selectedPost;

    /**
     * Метод ініціалізації контролера.
     * Викликається автоматично після завантаження FXML файлу.
     * Налаштовує прив'язку колонок таблиці та додає слухача подій вибору рядків.
     */
    @FXML
    public void initialize() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("authorId"));
        wallCol.setCellValueFactory(new PropertyValueFactory<>("wallOwnerId"));
        contentCol.setCellValueFactory(new PropertyValueFactory<>("content"));
        likesCol.setCellValueFactory(new PropertyValueFactory<>("likesCount"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        postTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPostDetails(newValue)
        );

        loadPosts();
    }

    /**
     * Заповнює текстові поля форми даними обраного в таблиці допису.
     * @param post об'єкт допису, обраний користувачем (або null)
     */
    private void showPostDetails(Post post) {
        if (post != null) {
            selectedPost = post;
            authorIdField.setText(String.valueOf(post.getAuthorId()));
            wallOwnerIdField.setText(String.valueOf(post.getWallOwnerId()));
            contentField.setText(post.getContent());
        } else {
            selectedPost = null;
            clearForm();
        }
    }

    /**
     * Завантажує всі дописи з бази даних та відображає їх у таблиці.
     */
    @FXML
    public void loadPosts() {
        try {
            List<Post> posts = repository.findAll();
            postData.setAll(posts);
            postTable.setItems(postData);
        } catch (SQLException e) {
            showAlert("Помилка", "Не вдалося завантажити дані: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Обробляє подію натискання кнопки "Додати новий".
     * Зчитує дані з форми, валідує їх та зберігає новий допис у БД.
     */
    @FXML
    public void handleAdd() {
        // ... (Тут залишається твій код метода) ...
        try {
            if (authorIdField.getText().isEmpty() || wallOwnerIdField.getText().isEmpty() || contentField.getText().isEmpty()) {
                showAlert("Помилка валідації", "Усі поля повинні бути заповнені!", Alert.AlertType.WARNING);
                return;
            }
            int authorId = Integer.parseInt(authorIdField.getText());
            int wallId = Integer.parseInt(wallOwnerIdField.getText());
            String content = contentField.getText();

            Post newPost = new Post(authorId, wallId, content);
            repository.create(newPost);

            loadPosts();
            clearForm();
            showAlert("Успіх", "Допис успішно додано!", Alert.AlertType.INFORMATION);

        } catch (NumberFormatException e) {
            showAlert("Помилка вводу", "ID автора та ID стіни мають бути числами!", Alert.AlertType.WARNING);
        } catch (SQLException e) {
            showAlert("Помилка БД", "Не вдалося додати допис. Переконайтеся, що користувачі з такими ID існують у базі.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Обробляє подію натискання кнопки "Зберегти зміни".
     * Оновлює вміст обраного допису.
     */
    @FXML
    public void handleUpdate() {
        // ... (Тут залишається твій код метода) ...
        if (selectedPost == null) {
            showAlert("Увага", "Спочатку оберіть допис у таблиці для редагування!", Alert.AlertType.WARNING);
            return;
        }
        try {
            selectedPost.setContent(contentField.getText());
            repository.update(selectedPost);
            loadPosts();
            clearForm();
            showAlert("Успіх", "Текст допису успішно оновлено!", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Помилка", "Не вдалося оновити допис.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Обробляє подію натискання кнопки "Видалити".
     * Видаляє обраний допис з БД.
     */
    @FXML
    public void handleDelete() {
        // ... (Тут залишається твій код метода) ...
        if (selectedPost == null) {
            showAlert("Увага", "Оберіть допис для видалення!", Alert.AlertType.WARNING);
            return;
        }
        try {
            repository.delete(selectedPost.getId());
            loadPosts();
            clearForm();
            showAlert("Успіх", "Допис видалено!", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            showAlert("Помилка", "Не вдалося видалити допис.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Обробляє подію натискання кнопки "Знайти".
     * Виконує пошук за заданими критеріями та оновлює таблицю результатами.
     */
    @FXML
    public void handleSearch() {
        // ... (Тут залишається твій код метода) ...
        try {
            Integer authorId = searchAuthorIdField.getText().isEmpty() ? null : Integer.parseInt(searchAuthorIdField.getText());
            String keyword = searchKeywordField.getText();

            List<Post> results = repository.search(authorId, keyword);
            postData.setAll(results);

            if (results.isEmpty()) {
                showAlert("Результат", "За вашим запитом нічого не знайдено.", Alert.AlertType.INFORMATION);
            }
        } catch (NumberFormatException e) {
            showAlert("Помилка вводу", "ID автора для пошуку має бути числом!", Alert.AlertType.WARNING);
        } catch (SQLException e) {
            showAlert("Помилка БД", "Помилка під час виконання пошуку.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Очищає всі текстові поля форми та скидає виділення у таблиці.
     */
    @FXML
    public void clearForm() {
        authorIdField.clear();
        wallOwnerIdField.clear();
        contentField.clear();
        searchAuthorIdField.clear();
        searchKeywordField.clear();
        selectedPost = null;
        postTable.getSelectionModel().clearSelection();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}