import ru.bmstu.matsapkov.backend.tools.Utils;

import java.security.SecureRandom;

public class UserCreator {

    // Метод для перевода байт в HEX-строку
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // Метод для генерации случайной соли в HEX формате (16 байт)
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return bytesToHex(saltBytes);
    }

    public static void main(String[] args) {
        String login = "user1";
        String password = "mypassword";
        String email = "user1@example.com";

        // Генерируем соль в HEX
        String salt = generateSalt();

        // Считаем хеш пароля с солью через твой Utils
        String hashedPassword = Utils.ComputeHash(password, salt);

        // Формируем SQL-запрос
        String sql = String.format(
                "INSERT INTO users (login, password, email, salt, token, activity) " +
                        "VALUES ('%s', '%s', '%s', '%s', '', CURRENT_DATE);",
                login, hashedPassword, email, salt
        );

        // Выводим запрос для вставки
        System.out.println(sql);
    }
}
