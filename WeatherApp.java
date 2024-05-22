import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class WeatherApp {
    private JFrame frame;
    private JTextField cityField;
    private JTextArea weatherArea;

    public WeatherApp() {
        frame = new JFrame("Weather Forecast");
        cityField = new JTextField(15);
        weatherArea = new JTextArea(10, 30);
        weatherArea.setEditable(false);

        JButton getWeatherButton = new JButton("Get Weather");
        getWeatherButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String city = cityField.getText();
                String weatherInfo = getWeather(city);
                weatherArea.setText(weatherInfo);
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("City:"));
        panel.add(cityField);
        panel.add(getWeatherButton);
        panel.add(new JScrollPane(weatherArea));

        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private String getWeather(String city) {
        String apiKey = "your_api_key";  // Replace with your actual API key
        String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject json = new JSONObject(response.toString());
                String description = json.getJSONArray("weather").getJSONObject(0).getString("description");
                double temp = json.getJSONObject("main").getDouble("temp") - 273.15;  // Convert from Kelvin to Celsius
                int humidity = json.getJSONObject("main").getInt("humidity");

                return "Weather: " + description + "\nTemperature: " + String.format("%.2f", temp) + " °C\nHumidity: " + humidity + "%";
            } else {
                return "Error: Unable to get weather data.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Unable to get weather data.";
        }
    }

    public static void main(String[] args) {
        new WeatherApp();
    }
}
