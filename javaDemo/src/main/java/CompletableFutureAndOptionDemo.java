import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;

public class CompletableFutureAndOptionDemo {

    @Test
    public void testCorrect() throws Exception {
        Optional<Integer> res = calcResultOfTwoServices(
                () -> Optional.of(40),
                () -> Optional.of(2)
        ).get(1, TimeUnit.SECONDS);

        assertEquals(Optional.of(42), res);
    }

    @Test
    public void testFirstEmpty() throws Exception {
        Optional<Integer> res = calcResultOfTwoServices(
                Optional::empty,
                () -> Optional.of(2)
        ).get(1, TimeUnit.SECONDS);

        assertEquals(Optional.empty(), res);
    }

    @Test
    public void testSecondEmpty() throws Exception {
        Optional<Integer> res = calcResultOfTwoServices(
                () -> Optional.of(40),
                Optional::empty
        ).get(1, TimeUnit.SECONDS);

        assertEquals(Optional.empty(), res);
    }

    private static CompletableFuture<Optional<Integer>> calcResultOfTwoServices (
            Supplier<Optional<Integer>> getResultFromFirstService,
            Supplier<Optional<Integer>> getResultFromSecondService
    ) {
        return CompletableFuture
                .supplyAsync(getResultFromFirstService)
                .thenApplyAsync(firstResultOptional ->
                        firstResultOptional.flatMap(firstResult ->
                                getResultFromSecondService.get().map(secondResult ->
                                        firstResult + secondResult
                                )
                        )
                );
    }

}
