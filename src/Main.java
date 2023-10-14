import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

class Composition {
    private String name;
    private String genre;
    private String artist;
    private String lyrics;
    private String creationDate;
    private String duration;
    private String format;
    private double rating;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return String.format("Composition(Ім'я='%s', Жанр='%s', Артист='%s', Текст='%s', Дата створення='%s', " +
                        "Тривалість='%s', Формат='%s', Рейтинг='%s')", name, genre, artist, lyrics, creationDate, duration,
                format, rating);
    }
}

class CompositionContainer<T> {
    private List<T> compositions;

    public CompositionContainer() {
        compositions = new ArrayList<>();
    }

    public void addComposition(T composition) {
        compositions.add(composition);
    }

    public List<T> getCompositions() {
        return compositions;
    }
}

class Program {
    private static final String FileName = "compositions.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) {
        CompositionContainer<Composition> compositionContainer = loadCompositions();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Меню:");
            System.out.println("1. Добавити нову композицію");
            System.out.println("2. Переглянути список композиций");
            System.out.println("3. Сортування композиції по імені");
            System.out.println("4. Сортування композиції по виконавцю");
            System.out.println("5. Сортування композиції по рейтингу");
            System.out.println("6. Видалити композицію за іменем");
            System.out.println("7. Вихід");
            System.out.print("Виберіть функцію: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    Composition composition = createComposition();
                    compositionContainer.addComposition(composition);
                    System.out.println("Композиція добавлена");
                    break;

                case 2:
                    displayCompositions(compositionContainer.getCompositions());
                    break;

                case 3:
                    Collections.sort(compositionContainer.getCompositions(), Comparator.comparing(Composition::getName));
                    System.out.println("Композиції відсортовані по імені.");
                    break;

                case 4:
                    Collections.sort(compositionContainer.getCompositions(), Comparator.comparing(Composition::getArtist));
                    System.out.println("Композиції відсортовані по виконавцю.");
                    break;

                case 5:
                    Collections.sort(compositionContainer.getCompositions(), Comparator.comparingDouble(Composition::getRating).reversed());
                    System.out.println("Композиції відсортовані по рейтингу.");
                    break;

                case 6:
                    deleteCompositionByName(compositionContainer);
                    break;

                case 7:
                    saveCompositions(compositionContainer);
                    System.out.println("Програма завершена.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Неправильний вибір.");
                    break;
            }
        }
    }

    private static Composition createComposition() {
        Composition composition = new Composition();

        Scanner scanner = new Scanner(System.in);

        System.out.print("Ім'я: ");
        composition.setName(scanner.nextLine());

        System.out.print("Жанр: ");
        composition.setGenre(scanner.nextLine());

        System.out.print("Артист: ");
        composition.setArtist(scanner.nextLine());

        System.out.print("Текст: ");
        composition.setLyrics(scanner.nextLine());

        System.out.print("Дата створення (yyyy-MM-dd): ");
        composition.setCreationDate(scanner.nextLine());

        System.out.print("Тривалість (hh:mm:ss): ");
        composition.setDuration(scanner.nextLine());

        System.out.print("Формат: ");
        composition.setFormat(scanner.nextLine());

        System.out.print("Рейтинг: ");
        composition.setRating(scanner.nextDouble());

        return composition;
    }

    private static void displayCompositions(List<Composition> compositions) {
        for (Composition composition : compositions) {
            System.out.println(composition);
        }
    }

    private static CompositionContainer<Composition> loadCompositions() {
        File file = new File(FileName);

        if (file.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuilder jsonData = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    jsonData.append(line);
                }

                Composition[] compositionArray = gson.fromJson(jsonData.toString(), Composition[].class);
                CompositionContainer<Composition> compositionContainer = new CompositionContainer<>();
                Collections.addAll(compositionContainer.getCompositions(), compositionArray);
                System.out.println("Дані успішно завантажені із compositions.json");
                return compositionContainer;
            } catch (IOException e) {
                System.out.println("Помилка завантаження даних із compositions.json: " + e.getMessage());
            }
        } else {
            System.out.println("compositions.json не знайдено.");
        }

        return new CompositionContainer<>();
    }

    private static void saveCompositions(CompositionContainer<Composition> compositionContainer) {
        try (FileWriter writer = new FileWriter(FileName)) {
            gson.toJson(compositionContainer.getCompositions(), writer);
        } catch (IOException e) {
            System.out.println("Помилка при збереженні данних в compositions.json: " + e.getMessage());
        }
    }

    private static void deleteCompositionByName(CompositionContainer<Composition> compositionContainer) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введіть ім'я композиції, яку ви хочете видалити: ");
        String nameToDelete = scanner.nextLine();

        List<Composition> compositions = compositionContainer.getCompositions();
        boolean found = false;

        for (int i = 0; i < compositions.size(); i++) {
            if (compositions.get(i).getName().equalsIgnoreCase(nameToDelete)) {
                compositions.remove(i);
                System.out.println("Композиція з іменем '" + nameToDelete + "' видалена.");
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Композиція з іменем '" + nameToDelete + "' не знайдена.");
        }
    }

}
