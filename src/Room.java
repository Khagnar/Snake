import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Room {
    private int width;                // ширина
    private int height;               // высота
    private Snake snake;
    private Mouse mouse;

    public Room(int width, int height, Snake snake) {
        this.width = width;
        this.height = height;
        this.snake = snake;
        game = this;
    }

    public Snake getSnake() {
        return snake;
    }

    public Mouse getMouse() {
        return mouse;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    public void setMouse(Mouse mouse) {
        this.mouse = mouse;
    }

    /**
     * Основной цикл программы.
     * Тут происходят все важные действия
     */
    public void run() {
        KeyboardObserver keyboardObserver = new KeyboardObserver();       //Создаем объект "наблюдатель за клавиатурой" и стартуем его.
        keyboardObserver.start();

        while (snake.isAlive()) {
            if (keyboardObserver.hasKeyEvents()) {                        //"наблюдатель" содержит события о нажатии клавиш?
                KeyEvent event = keyboardObserver.getEventFromTop();
                if (event.getKeyChar() == 'q') return;                    //Если равно символу 'q' - выйти из игры.


                if (event.getKeyCode() == KeyEvent.VK_LEFT)               //Если "стрелка влево" - сдвинуть фигурку влево
                    snake.setDirection(SnakeDirection.LEFT);

                else if (event.getKeyCode() == KeyEvent.VK_RIGHT)         //Если "стрелка вправо" - сдвинуть фигурку вправо
                    snake.setDirection(SnakeDirection.RIGHT);

                else if (event.getKeyCode() == KeyEvent.VK_UP)            //Если "стрелка вверх" - сдвинуть фигурку вверх
                    snake.setDirection(SnakeDirection.UP);

                else if (event.getKeyCode() == KeyEvent.VK_DOWN)          //Если "стрелка вниз" - сдвинуть фигурку вниз
                    snake.setDirection(SnakeDirection.DOWN);
            }

            snake.move();   //двигаем змею
            print();        //отображаем текущее состояние игры
            sleep();        //пауза между ходами
        }

        System.out.println("Game Over!");                                 //Выводим сообщение "Game Over"
    }

    /**
     * Выводим на экран текущее состояние игры
     */
    public void print() {
        int[][] matrix = new int[height][width];                          //Создаем массив, куда будем "рисовать" текущее состояние игры

        ArrayList<SnakeSection> sections = new ArrayList<SnakeSection>(snake.getSections());               //Рисуем все кусочки змеи
        for (SnakeSection snakeSection : sections) {
            matrix[snakeSection.getY()][snakeSection.getX()] = 1;
        }

        matrix[snake.getY()][snake.getX()] = snake.isAlive() ? 2 : 4;                   //Рисуем голову змеи (4 - если змея мертвая)

        matrix[mouse.getY()][mouse.getX()] = 3;                                         //Рисуем мышь

        String[] symbols = {" . ", " x ", " X ", "^_^", "RIP"};                         //Выводим все это на экран
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(symbols[matrix[y][x]]);
            }
            System.out.println();
        }
        System.out.println();                  // делаем пустые строки для лучшего восприятия
        System.out.println();
        System.out.println();
    }

    /**
     * Метод вызывается, когда мышь съели
     */
    public void eatMouse() {
        createMouse();
    }

    /**
     * Создает новую мышь
     */
    public void createMouse() {                        // метод создания мыши в случайном месте комнаты
        int x = (int) (Math.random() * width);
        int y = (int) (Math.random() * height);

        mouse = new Mouse(x, y);
    }


    public static Room game;

    public static void main(String[] args) {
        game = new Room(20, 20, new Snake(10, 10));           // создаем комнату размером 20х20 со змеёй в центре комнаты
        game.snake.setDirection(SnakeDirection.DOWN);                             // начальное направление змеи - вниз
        game.createMouse();                                                       // создаем мышь в случайном месте комнаты
        game.run();                                                               // запускаем игру
    }

    /**
     * Программа делает паузу, длинна которой зависит от длинны змеи.
     */
    private int initialDelay = 520;
    private int delayStep = 20;

    public void sleep() {
        try {
            int level = snake.getSections().size();                               // чем выше уровень, тем быстрее двигается змея
            int delay = level < 15 ? (initialDelay - delayStep * level) : 200;    // после 15лвл задержка постоянна и равняется 200
            Thread.sleep(delay);
        } catch (InterruptedException e) {
        }
    }
}