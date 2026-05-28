package brickbot.old.quickstart.manualautonomous;

import android.os.Environment;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

public class JsonReader {

    /**
     * Reads a recorded autonomous file and returns a list of GamepadStates.
     * Note: We removed generic <T> because GamepadState requires specific
     * Base64 decoding logic for the byte arrays.
     */
    public LinkedList<GamepadState> readFile(String filename) {
        // Target the same directory used by the writer
        File path = new File(Environment.getExternalStorageDirectory(), "FIRST/recordings");
        File file = new File(path, filename);

        LinkedList<GamepadState> output = new LinkedList<>();

        try {
            // Read the file content into a String
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            // Parse the string as a JSON Array
            JSONArray jsonArray = new JSONArray(sb.toString());

            // Iterate through the array and reconstruct GamepadState objects
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);

                long timestamp = obj.getLong("timestamp");

                // Decode the Base64 strings back into byte arrays
                byte[] g1 = Base64.decode(obj.getString("gamepad1State"), Base64.NO_WRAP);
                byte[] g2 = Base64.decode(obj.getString("gamepad2State"), Base64.NO_WRAP);

                output.add(new GamepadState(timestamp, g1, g2));
            }

        } catch (Exception e) {
            // Handle file not found or parsing errors
            android.util.Log.e("JsonReader", "Error reading file: " + e.getMessage());
            e.printStackTrace();
        }

        return output;
    }
}