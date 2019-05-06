import io.javalin.Javalin;

public class App {
    public static void main(String[] args) {
        Javalin server = Javalin.create();
        server.enableStaticFiles("/public");

        server.start(80);
    }
}
