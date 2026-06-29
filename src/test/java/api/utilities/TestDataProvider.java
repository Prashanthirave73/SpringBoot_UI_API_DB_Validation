package api.utilities;

import api.models.UserModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.List;

public class TestDataProvider {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @DataProvider(name = "userLifecycleData", parallel = true)
    public Object[][] userLifecycleData() {
        List<UserModel> users = readUsers();
        Object[][] data = new Object[users.size()][1];
        for (int i = 0; i < users.size(); i++) {
            UserModel user = users.get(i);
            user.setEmail(user.getEmail().replace("${timestamp}", String.valueOf(Instant.now().toEpochMilli())));
            data[i][0] = user;
        }
        return data;
    }

    private List<UserModel> readUsers() {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("testdata/users.json")) {
            if (stream == null) {
                throw new IllegalStateException("testdata/users.json not found");
            }
            return MAPPER.readValue(stream, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read user test data", e);
        }
    }
}
