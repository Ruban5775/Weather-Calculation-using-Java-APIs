package weathercalc;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// Retrieve weather data from api- thin backend logic will fetch the latest weather
// data from the external api and return it. the gui will
// display the data using api

public class WeatherAppLogic {
	
	public static JSONObject getWeatherData(String location) {
		JSONArray locationData = getlocationData(location);
		
		// extract latitude and langitude data
		
		JSONObject locationn = (JSONObject) locationData.get(0);
		double latitude = (double) locationn.get("latitude");
		double langitude = (double) locationn.get("longitude");
		
		// build api request url with location coordinates
		
		//String urlString = "https://api.open-meteo.com/v1/forecast?" + "latitude=" + latitude + "&longitude=" + langitude + "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=auto";
		
		String urlString = "https://api.open-meteo.com/v1/forecast?" + "latitude=" + latitude + "&longitude=" + langitude + "&hourly=temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m&timezone=America%2FLos_Angeles";
		
		
		try {
			HttpURLConnection conn = fetchApiResponse(urlString);
			
			// check the response status
			// 22- i connection is success
			
			if(conn.getResponseCode() != 200) {
				System.out.println("Error: could not connect to API");
			}
			
			
			//store resulting json data
			
			StringBuilder resultJson = new StringBuilder();
			Scanner scan = new Scanner(conn.getInputStream());
			
			while(scan.hasNext() ) {
				resultJson.append(scan.nextLine());
			}
			
			
			scan.close();
			
			conn.disconnect();
			
			//parse through our data
			JSONParser parser = new JSONParser();
			JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));
			
			
			//retrieve hourly data

			JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");
			
			//wee want to get the current hour data
			// so we need to get the index of our current hour
			
			JSONArray time = (JSONArray) hourly.get("time");
			int index = findIndexOfCurrentTime(time);
			
			JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
			double temprature  = (double) temperatureData.get(index);
			
			
			//Get weather code
			JSONArray weathercode = (JSONArray) hourly.get("weather_code");
			String weathercon = convertWeathercode((long) weathercode.get(index));
			
			//get humanity 
			JSONArray relativehumidity = (JSONArray) hourly.get("relative_humidity_2m");
			long humidity = (long) relativehumidity.get(index);
			
			//get windspeed
			JSONArray windspeedData= (JSONArray) hourly.get("wind_speed_10m");
			double windspeed = (double) windspeedData.get(index);
			
			
			//build the weather json data object that are going to access in our frontend
			
			JSONObject weatherData = new JSONObject();
			weatherData.put("temperature", temprature);
			weatherData.put("weather_Condition", weathercon);
			weatherData.put("Humidity", humidity);
			weatherData.put("windSpeed", windspeed);
			
			return weatherData;
			
		}
		catch(Exception e4) {
			e4.printStackTrace();
		} finally {
		}
		return null;
		
	}
	// get the geographical coordination of given location
	public static JSONArray getlocationData(String locationName) {
		// replace any whitespace in location name to + to in API's request format
		
		locationName = locationName.replace(" ", "+");
		
		// build api url location parameter
		String apiurl = "https://geocoding-api.open-meteo.com/v1/search?name=" + locationName + "&count=10&language=en&format=json";
		
		try {
			//call api and get response
			
			HttpURLConnection conn = fetchApiResponse(apiurl);
			
			// check response 
			//200 means successful connection
			
			if(conn.getResponseCode() != 200) {
				System.out.println("Error: Could not connect to API");
				
			}else {
				StringBuilder resultJson = new StringBuilder();
				Scanner scan = new Scanner(conn.getInputStream());
				while(scan.hasNext()) {
					resultJson.append(scan.nextLine());
					
				}
				scan.close();
				
				
				//close url connection 
				conn.disconnect();
				
				// parse the json string into a shon obj
				
				JSONParser parse = new JSONParser();
				JSONObject resultJsonObj = (JSONObject) parse.parse(String.valueOf((resultJson)));
				
				//get the list of location data the api generated form the location name
				
				JSONArray LocationData = (JSONArray) resultJsonObj.get("results");
				return LocationData;
				
				
			}
			
		}catch(Exception e2) {
			e2.printStackTrace();
		}
		return null;
	}
	
	private static HttpURLConnection fetchApiResponse(String urlString) {
		try {
			// Attempt to create connection
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			// set request method to get
			
			conn.setRequestMethod("GET");
			
			//connect to our api
			conn.connect();
			return conn;
			
			
		}catch(IOException e3) {
			e3.printStackTrace();
		} 
		//cound not make connection
		return null;
			
		}
	
	private static int findIndexOfCurrentTime(JSONArray timeList) {
		String currentTime = getCurrentTime();
		for(int i = 0; i< timeList.size(); i++) {
			String time = (String) timeList.get(i);
			if(time.equalsIgnoreCase(currentTime)) {
				return i;
			}
			
		}
		return 0;
	}

	public static String getCurrentTime() {
		LocalDateTime currentDate = LocalDateTime.now();
		
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");
		
		String formatedDateTime = currentDate.format(format);
		
		return formatedDateTime;
		
	}
	
	private static String convertWeathercode(long weather) {
		String weathercondition = "";
		if(weather == 0L) {
			weathercondition = "Clear";
		}
		else if(weather <= 3L && weather > 0L) {
			weathercondition = "Cloudy";
		}
		else if(weather >= 51L && weather <=67L) 
			 {
			weathercondition = "Rain";
		}
		else if(weather >= 71L && weather <= 77L) {
			weathercondition = "Snow";
		}
		return weathercondition;
	}

}


