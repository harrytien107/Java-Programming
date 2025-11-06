package Buoi2;

public class Singleton {
    private Singleton() {}
    private static Singleton _instance;

    public static Singleton getInstance() {
        if (_instance == null) {
            _instance = new Singleton();
        }
        return _instance;
    }
}
