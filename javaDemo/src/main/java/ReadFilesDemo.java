import reactor.core.publisher.Flux;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.BaseStream;

public class ReadFilesDemo {

    private static Flux<String> getLinesOfFile(String path) {
        return Flux.using(
                () -> Files.lines(Paths.get(path)),
                Flux::fromStream,
                BaseStream::close
        );
    }

    private static Flux<String> glueFiles(String filename1, String filename2, String filename3) {
        return getLinesOfFile(filename1).flatMap(lineFromFirstFile ->
                getLinesOfFile(filename2)
                        .filter(line -> line.equals(lineFromFirstFile))
                        .flatMap(lineFromSecondFile ->
                            getLinesOfFile(filename3)
                                .filter(line -> line.equals(lineFromSecondFile))
                                .map(lineFromThirdFile ->
                                    lineFromThirdFile
                            )
                )
        );
    }

    public static void main(String[] args) {
        glueFiles("first.txt", "second.txt", "third.txt")
                .subscribe(System.out::println);
    }

}
