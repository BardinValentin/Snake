import javax.swing.*; // Подключаем библиотеку для создания графического интерфейса

public class Window extends JFrame { // наследуем класс, позвооляющий создать графическое окно
    public Window(){                //Описываем конструктор
        setTitle("Змейка");         // Задаем заголовок
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // кнопка закрытия окна
        setSize(320,320);   // Задаем размеры окна
        setLocation(400,400);       // Устанавливаем местоположение нашего окна
        add(new GameField());              //Создаем игрвое поле
        setVisible(true);                  // Скрывает или показывает компонент
    }

    public static void main(String[] args) {
        Window window = new Window(); //Создам экземпляр класса
    }
}
