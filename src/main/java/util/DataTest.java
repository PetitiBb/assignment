package util;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;

public final class DataTest {

    public static final String HOST = "http://localhost:8085";
    public static final String PATH_TRAIN = "/train/food";
    public static final String PATH_REC = "/recognize/food";

    public static final String FAILING_MSG = "invalid input, supply at least 10 training sentences";
    public static final String NOK_MSG = "Invalid status";

    public static final Map<String, String> PARAMS = ImmutableMap.of("text", "This new place at Time Square serves "
            + "awesome soup!");

}
