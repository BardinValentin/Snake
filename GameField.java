import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener{ // наследуем класс, который позволит создать поле, где будем отображать игру
    private final int SIZE = 320;       //Размер поля
    private final int DOT_SIZE = 16;    //Размер кусочка в px
    private final int ALL_DOTS = 400;   // СКолько всего может поместиться на игровом поле
    private Image dot;                  // Картинка точки
    private Image apple;                // Картинка яблока
    private int appleX;                 //Координата x яблока
    private int appleY;                 //Координата y яблока
    private int[] x = new int[ALL_DOTS];//Сохранеям положения змейки по x
    private int[] y = new int[ALL_DOTS];//Сохранеям положения змейки по y
    private int dots;                   //Размер змейки в данный момент
    private Timer timer;
    private boolean left = false;       //Отвечают за текущее направление змейки
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;      //Отвечает играем мы еще или нет


    public GameField(){                 //Создаем конструктор
        setBackground(Color.black);     //Устанавливаем фон игрового поля
        loadImages();                   //Загружаем картинки
        initGame();                     //Задаем начальные данные, змейку, яблоко и таймер
        addKeyListener(new FieldKeyListener()); // Создаем обработчик нажатия клавиш
        setFocusable(true);                     // Создаем фокус на игровом поле
    }

    public void initGame(){ // Метод инициализирующий начало игры
        dots = 3;
        for(int i = 0; i < dots; i++){ //Создаем начальные значения для змейки
            x[i] = 48 - i * DOT_SIZE;
            y[i] = 48;
        }
        timer = new Timer(250,this);  //Создаем таймер, this - говорим что наше поле GameField будет отвечать за обработку таймера каждые 250 мс
        timer.start();                              // Запускаем
        createApple();
    }

    public void createApple(){ // Создаем яблоко
        appleX = new Random().nextInt(20)*DOT_SIZE; // рандомно создаем координаты яблока
        appleY = new Random().nextInt(20)*DOT_SIZE;
    }

    public void loadImages(){           //Метод загрузки картинок
        ImageIcon iiApple = new ImageIcon("apple.png");
        apple = iiApple.getImage();
        ImageIcon iiDot = new ImageIcon("dot.png");
        dot = iiDot.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) { // Перерисовываем поле
        super.paintComponent(g);
        if(inGame){ // Если играем
            g.drawImage(apple,appleX,appleY,this); // рисуем яблоко
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot,x[i],y[i],this);
            }
        }
        else{
            String str = "Game Over";
            Font f = new Font("Arial",Font.BOLD,14);
            g.setColor(Color.white);
            g.setFont(f);
            g.drawString(str,125,SIZE/2);
        }
    }

    public void move(){ // Двигаем змейку
        for (int i = dots; i > 0; i--) { //Делаем движение точки, кроме головы
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if(left){  //Движение для головы
            x[0] -= DOT_SIZE;
        }
        if(right){
            x[0] += DOT_SIZE;
        } if(up){
            y[0] -= DOT_SIZE;
        } if(down){
            y[0] += DOT_SIZE;
        }
    }

    public void checkApple(){ // Проверяем съели ли мы яблоко
        if(x[0] == appleX && y[0] == appleY){  //Если координата головы змейки и яблока совпали то увеличили змею и добавили яблоко
            dots++;
            if (dots % 5 == 0) {  // Увеличиваем скорость при съедании 5 яблок
                timer.setDelay(timer.getDelay() - 50);
            }
            createApple();
        }
    }

    public void checkCollisions(){ //Проверяем не врезались ли мы в бордюр или в самого себя
        for (int i = dots; i > 0 ; i--) { //
            if(i > 4 && x[0] == x[i] && y[0] == y[i]){  //Проверяем на врезание в самого себя
                inGame = false;
            }
        }
        //Проверка за выход за границу
        if(x[0] > SIZE){
            inGame = false;
        }
        if(x[0] < 0){
            inGame = false;
        }
        if(y[0] > SIZE){
            inGame = false;
        }
        if(y[0] < 0){
            inGame = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) { //Вызывается каждый раз когда тикает таймер
        if(inGame){ // Проверяем играем ли мы
            checkApple();
            checkCollisions();
            move();

        }
        repaint(); // Перерисовываем поле
    }

    class FieldKeyListener extends KeyAdapter { //Обработка нажатия клавиш
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT && !right){ //Если я жму налево и не двигаюсь вправо
                left = true; //то движемся влево
                up = false;
                down = false;
            }
            if(key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT && !left){
                right = true;
                up = false;
                down = false;
            }

            if(key == KeyEvent.VK_W || key == KeyEvent.VK_UP && !down){
                right = false;
                up = true;
                left = false;
            }
            if(key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN && !up){
                right = false;
                down = true;
                left = false;
            }
            if (key == KeyEvent.VK_P) {
                timer.stop();
            }

            if (key == KeyEvent.VK_O) {
                timer.start();
            }

            if (key == KeyEvent.VK_ENTER) {
                new Window();
            }
        }
    }
}
