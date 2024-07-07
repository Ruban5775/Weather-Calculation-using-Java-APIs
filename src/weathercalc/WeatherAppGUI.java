package weathercalc;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

import org.json.simple.JSONObject;

public class WeatherAppGUI extends JFrame {
	
	private JSONObject weatherData;

	
	private static final long serialVersionUID = 1L;

	public WeatherAppGUI() {
		
		super("Weather App");
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(450, 650);
		
		setLocationRelativeTo(null);
		
		setLayout(null);
		
		setResizable(false);
		
		addGuiComponent();
		
		
	}
	private void addGuiComponent() {
		
		JTextField searchTextField = new JTextField();
		
		// set the location and size of our component
		
		searchTextField.setBounds(15, 15, 351, 45);
		
		//change the font style and size
		searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));
		add(searchTextField);
		
		
		
		//weather image
		
		JLabel weatherConditonImage = new JLabel(LoadImage("assests/cloudy.png"));
		weatherConditonImage.setBounds(0, 125, 450, 217);
		add(weatherConditonImage);
		
		// Temperature text
		
		JLabel Temp = new JLabel("10 C");
		Temp.setBounds(0, 350, 450, 54);
		Temp.setFont(new Font("Dialog", Font.BOLD, 50));
		
		// Center the text
		Temp.setHorizontalAlignment(SwingConstants.CENTER);
		add(Temp);
		
		// Weather condition description
		
		JLabel weathercondition = new JLabel("Cloudy");
		weathercondition.setBounds(0, 405, 450, 36);
		weathercondition.setFont(new Font("Dialog", Font.PLAIN, 32));
		add(weathercondition);
		
		//Humidity image
		
		JLabel humidityImg = new JLabel(LoadImage("assests/humidity.png"));
		humidityImg.setBounds(15, 500, 74, 66);
		add(humidityImg);
		
		//Humidity text
		
		JLabel humidityTest = new JLabel("<html><b>Humidity</b> 100%</html>");
		humidityTest.setBounds(90, 500, 85, 55);
		humidityTest.setFont(new Font("Dialog", Font.PLAIN, 20));
		add(humidityTest);
		
		//Windspeed image
		
		JLabel windimg = new JLabel(LoadImage("assests/windspeed.png"));
		windimg.setBounds(220, 500, 74, 66);
		add(windimg);
		
		// windspeed text
		
		JLabel windspeedtext = new JLabel("<html><b>WindSpeed</b> 15km/h</html>");
		windspeedtext.setBounds(310, 500, 85, 55);
		windspeedtext.setFont(new Font("Dialog", Font.PLAIN, 20));	
		add(windspeedtext);
		
		//search button
		
				JButton searchButton = new JButton(LoadImage("assests/search.png"));
				
				//chage the cursor to a hand cursor when hovering over this button
				
				searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				searchButton.setBounds(375, 13, 47, 45);
				searchButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						//get location from user
						
						String userInput = searchTextField.getText();
						
						//validate input - remove whitespace to ensure non-empty text
						
						if(userInput.replaceAll("\\s", "").length() <= 0) {
							return;
						}
						
						//retrieve weather date
						
						weatherData = WeatherAppLogic.getWeatherData(userInput);
						if (weatherData != null) {
							updateGui(weatherConditonImage, Temp, weathercondition, humidityTest, windspeedtext);
						}
						
						//update gui
						
						
						// update weather image
						
					}
				});
				add(searchButton);
		
		
		
	}
	
	private void updateGui(JLabel weatherConditonImage, JLabel Temp, JLabel weathercondition, JLabel humidityTest, JLabel windspeedtext) {
//		String weatherCondition = (String) weatherData.get("Weather_Condition");
//		weathercondition.setText(weatherCondition);
//		
		
		
		String weathercon = (String) weatherData.get("weather_Condition");
		weathercondition.setText(weathercon);
		switch (weathercon) {
		case "Clear":
			weatherConditonImage.setIcon(LoadImage("assests/clear.png"));
			break;
		case "Cloudy":
			weatherConditonImage.setIcon(LoadImage("assests/cloudy.png"));
			break;
		case "Rain":
			weatherConditonImage.setIcon(LoadImage("assests/rain.png"));
			break;
		case "Snow":
			weatherConditonImage.setIcon(LoadImage("assests/snow.png"));
			break;
	}
		
		double temperature = (double) weatherData.get("temperature");
		Temp.setText(temperature + " C");

		

		long humidity = (long) weatherData.get("Humidity");
		humidityTest.setText("<html><b>Humidity</b> " + humidity + "%</html>");

		double windspeed = (double) weatherData.get("windSpeed");
		windspeedtext.setText("<html><b>WindSpeed</b> " + windspeed + " km/h</html>");
	}

	
	 private ImageIcon LoadImage(String resourcepath) {
		 try {
			 BufferedImage image = ImageIO.read(new File(resourcepath));
			 
			 return new ImageIcon(image);
		 }catch (IOException e) {
			 e.printStackTrace();
		 }
		 System.out.println("Count not find resource");
		 return null;
	 }
	
	

}
