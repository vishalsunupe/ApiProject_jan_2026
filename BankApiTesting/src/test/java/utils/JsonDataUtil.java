package utils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonDataUtil {

    // Generic method to read JSON array file
    public static Object[][] getTestData(String jsonFilePath) throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        List<Map<String, Object>> dataList =
                mapper.readValue(
                        new File(jsonFilePath),
                        new TypeReference<List<Map<String, Object>>>() {}
                );

        Object[][] data = new Object[dataList.size()][1];

        for (int i = 0; i < dataList.size(); i++) {
            data[i][0] = dataList.get(i);
        }
        return data;
    }
}
