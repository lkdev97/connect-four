import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        Javalin server = Javalin.create();
        server.enableStaticFiles("/public");

        server.start(80);
        
        Game g1 = new Game("1111", "2222");
        g1.put(0);
        g1.put(0);
        g1.put(6);
        g1.put(7);
        g1.put(7);
        g1.put(7);
        g1.put(7);
        g1.printField();
        
    }
}
