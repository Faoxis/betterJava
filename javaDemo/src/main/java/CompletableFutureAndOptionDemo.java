import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;

public class CompletableFutureAndOptionDemo {

    @Test
    public void testCorrect() throws Exception {
        Optional<String> res = calcResultOfTwoServices(
                () -> Optional.of(40),
                number -> Optional.of(number + 2)
        ).get(1, TimeUnit.SECONDS);

        assertEquals(Optional.of("40 42"), res);
    }

    @Test
    public void testFirstEmpty() throws Exception {
        Optional<String> res = calcResultOfTwoServices(
                Optional::empty,
                number -> Optional.of(number + 2)
        ).get(1, TimeUnit.SECONDS);

        assertEquals(Optional.empty(), res);
    }

    @Test
    public void testSecondEmpty() throws Exception {
        Optional<String> res = calcResultOfTwoServices(
                () -> Optional.of(40),
                number -> Optional.empty()
        ).get(1, TimeUnit.SECONDS);

        assertEquals(Optional.empty(), res);
    }

    private static CompletableFuture<Optional<String>> calcResultOfTwoServices (
            Supplier<Optional<Integer>> getResultFromFirstService,
            Function<Integer, Optional<Integer>> getResultFromSecondService
    ) {
        return CompletableFuture
                .supplyAsync(getResultFromFirstService)
                .thenApplyAsync(firstResultOptional ->
                        firstResultOptional.flatMap(first ->
                                getResultFromSecondService.apply(first).map(second ->
                                    first + " " + second
                                )
                        )
                );
    }

}
