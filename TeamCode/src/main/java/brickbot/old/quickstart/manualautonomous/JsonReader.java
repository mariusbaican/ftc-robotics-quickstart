package brickbot.old.quickstart.manualautonomous;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

public class JsonReader<T> {
    public LinkedList<T> readFile(String filename, Class<T> clazz) {
        File file = new File(filename);

        LinkedList<T> output = new LinkedList<>();

        try (InputStream input = new FileInputStream(file)) {
            // Create JSON parser
            ObjectMapper mapper = new ObjectMapper();
            JsonParser parser = mapper.createParser(input);

            // Check if file content is a JSON Array Node
            if (parser.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Expected start array.");
            }

            // Iterate over the gamepad state history
            while (parser.nextToken() == JsonToken.START_OBJECT) {
                output.add(mapper.readValue(parser, clazz));
            }
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(
                    "File " + file.getAbsolutePath() + " does not exist.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output;
    }
}
