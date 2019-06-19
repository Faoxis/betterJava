import java.util.concurrent.CompletableFuture;

public class Calcer {
    private CompletableFuture<Integer> getCalc(int x, int y) {
        return CompletableFuture.supplyAsync(() -> x + y);
    }
}
