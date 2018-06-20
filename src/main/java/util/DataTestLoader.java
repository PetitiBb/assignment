package util;

import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.google.common.base.Charsets.UTF_8;
import static java.util.Locale.ENGLISH;

public class DataTestLoader {

    public static String readJsonFromFile(String fileName) throws IOException {
        InputStream inputStream =
                DataTestLoader.class.getClassLoader()
                        .getResourceAsStream(fileName.toLowerCase(ENGLISH) + ".json");
        return CharStreams.toString(new InputStreamReader(inputStream, UTF_8));
    }

}
