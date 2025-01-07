Weather Forecast App 🌤️
Weather Forecast App is an advanced Android application designed to deliver real-time weather updates and detailed forecasts for any city worldwide. With an intuitive design and dynamic visuals, it provides accurate weather information, including current conditions and hourly forecasts, powered by the WeatherAPI service.

<p align="center">
<img src="https://github.com/user-attachments/assets/ef13a825-77a8-41a6-b1b2-f1758fe9f1a9" alt="Image 4" width="300"/>
</p>
Features
Real-Time Weather Updates: Displays current weather details like temperature, condition, and wind speed for the selected city.
Hourly Weather Forecast: Shows an hourly forecast for the next 24 hours using a clean and scrollable RecyclerView.
Dynamic Weather Icons: Automatically updates icons and visuals based on weather conditions (e.g., sunny, rainy, night).
Search Bar: Allows users to quickly search for weather in any city by entering its name.
Location-Based Weather: Uses GPS to detect the user's location and fetch weather details automatically.
User-Friendly Design: A modern interface with edge-to-edge UI and smooth transitions for seamless navigation.

<p align="center">
<img src="https://github.com/user-attachments/assets/d99f14db-42d6-46a6-900f-ae2b70a4f5b1" alt="Image 3" width="300"/>
</p>
Backend Workflow
API Integration with Volley
The app integrates with the WeatherAPI service using the Volley library for network requests.
A GET request is sent to fetch real-time weather data and forecasts.
Example API call:
The API Response Includes:
Current Weather:

<p align="center">
<img src="https://github.com/user-attachments/assets/8c4a2bfa-9ef7-4ef6-8b37-759db2875ce9" alt="Image 2" width="300"/>
</p>
Temperature (temp_c)
Weather condition (condition.text)
Weather icon (condition.icon)
Day/Night indicator (is_day)
Hourly Forecast:

Time (time)
Temperature (temp_c)
Wind speed (wind_kph)
Weather condition (condition.text)
Icon (condition.icon)
Data Parsing
The JSON response is parsed to extract the required data.

Example JSON snippet for the hourly forecast:

json
Copy code
{
  "time": "2025-01-07 12:00",
  "temp_c": 25.6,
  "wind_kph": 10.5,
  "condition": {
    "text": "Sunny",
    "icon": "//cdn.weatherapi.com/weather/64x64/day/113.png"
  }
}
Parsed data is used to populate forecastRVModal objects and displayed using WeatherRVAdapter.

RecyclerView for Forecast Display
The hourly weather data is dynamically loaded into a RecyclerView for a scrollable view of the forecast.
Each item in the RecyclerView displays:
Time
Temperature
Weather icon
Wind speed
Dynamic Weather Icons
Weather condition icons are fetched dynamically using Picasso.
Example:
java
Copy code
Picasso.get().load("https:" + conditionIcon).into(tempImg);
Search Bar Functionality
Users can enter a city name in the search bar, triggering a new API call for that city.
The UI updates dynamically to display the selected city's weather details.
Error handling ensures users are notified if an invalid city name is entered.
Location-Based Weather
The app uses LocationManager and Geocoder to fetch the user's current coordinates.
These coordinates are used to determine the city and fetch local weather details.
How It Works
Home Screen

<p align="center">
<img src="https://github.com/user-attachments/assets/dbe79e4f-6094-4687-ac9f-ded5d671f27d" alt="Image 1" width="300"/>
</p>

Displays the current temperature, weather condition, and an icon for the default or last-searched city.
Weather Forecast

A detailed hourly forecast for the next 24 hours is shown in a scrollable RecyclerView.
Search Functionality

Users can search for any city, and the app updates the UI with the weather data for that city.
Dynamic Weather Updates

Icons, conditions, and temperatures dynamically adapt based on the weather API's response.
Technologies Used
Android Studio: Development environment.
Java: Core programming language for app logic.
Volley: Handles API requests and responses.
RecyclerView: Displays weather forecast dynamically.
Picasso: Efficient image loading and caching for weather icons.
Geocoder API: Detects user location for weather updates.
WeatherAPI: Source for real-time weather data and forecasts.
