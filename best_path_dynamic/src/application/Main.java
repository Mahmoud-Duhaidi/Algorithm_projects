package application;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main extends Application {
	
    private TableView<String[]> dpTable = new TableView<>();
    private TextArea resultTextArea = new TextArea();
    private int number_of_cities;
    private String[] city;
    private int[][] dp;
    private String[][] Citydp;
    private String str;

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();

        root.getChildren().add(dpTable);

        // Set up TextArea for result
        resultTextArea.setEditable(false);
        root.getChildren().add(resultTextArea);

        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

        // Load data and 
        loadData();
    }

    

    private void loadData() {
        File data = new File("data.txt");
        try {
            Scanner in = new Scanner(data);

            // Reading the number of cities from the first line of the file
            number_of_cities = Integer.parseInt(in.nextLine());//14

            // Reading the start and end cities from the second line of the file
            String[] start_End = in.nextLine().split(",");
            String City_Start = start_End[0].trim();//start
            String City_End = start_End[1].trim();//End

            int n = 0; 
            city = new String[number_of_cities]; // Array to store city names
            int[] stage = new int[number_of_cities]; // Array to store stage numbers for cities

            stage[0] = 0; // Setting the stage of the first city to 0

            int currentstage = 0; 
            int nextStage = 0;

            // Creating a HashMap to store city names as keys and their nearby cities as values
            Map<String, ArrayList<The_City>> lists = new HashMap<>();

            // Loop to read city data from the file
            for (int i = 0; i < number_of_cities - 1; i++) {
                String[] line = in.nextLine().split(", "); // Splitting the line to get city data
                //Start [A,22,70] [B,8,80] [C,12,80]
                String cityName = line[0]; 
                ArrayList<The_City> Nearby_cities = new ArrayList<>(); // List to store nearby cities

                city[i] = cityName; // Storing the city name in the array

                if (i == currentstage) { // Checking if the current city belongs to a new stage
                	nextStage++; // Incrementing the stage counter
                    for (int count = 1; count < line.length; count++) {
                    	//System.out.println(line.length);
                    	//System.out.println(line[count]);
                        stage[++n] = nextStage; // Setting the stage for the next city
                        //System.out.println(stage[n]);
                        currentstage++; // Incrementing the stage counter
                    }
                }

                // Loop to read nearby cities and their costs
                for (int j = 1; j < line.length; j++) {
                	//System.out.println(line[j]);
                    String[] cityInfo = line[j].substring(1, line[j].length() - 1).split(",");
                    String Nearby_Cities = cityInfo[0].trim();
                    //System.out.println(Nearby_Cities = cityInfo[0].trim());
                    int petrol_Cost = Integer.parseInt(cityInfo[1].trim());
                    int hotel_Cost = Integer.parseInt(cityInfo[2].trim());
                    Nearby_cities.add(new The_City(Nearby_Cities, petrol_Cost, hotel_Cost));
                }

                lists.put(cityName, Nearby_cities); // Storing the city and its nearby cities in the HashMap
            }

            city[n] = City_End; // Setting the last city as the end city/end
            stage[n] = nextStage; // Setting the stage for the end city//5

            dp = new int[number_of_cities][number_of_cities]; // 2D array to store costs
            Citydp = new String[number_of_cities][number_of_cities]; // 2D array to store city paths

            //Citydp[number_of_cities - 1][number_of_cities - 1] = City_End; // Setting the end city in the path

            dp[number_of_cities - 1][number_of_cities - 1] = 0; // Setting the cost of reaching the end city
            // Loop to fill the DP table
            
//            for (int i = 0; i < number_of_cities; i++) {
//                for (int j = 0; j < number_of_cities; j++) {
//                    dp[i][j] = Integer.MAX_VALUE;
//                    Citydp[i][j] = ""; 
//                }
//            }
            for (int i = number_of_cities - 2; i >= 0; i--) {
            	ArrayList<The_City> LI = lists.get(city[i]); 
            	//System.out.println(LI);

                for (int j = i + 1; j < number_of_cities; j++) {
                    if (stage[i] + 1 == stage[number_of_cities - 1]) { 
                    	

                        if (stage[i] != stage[j]) {
                            dp[i][j] = LI.get(0).getHotel_Cost() + LI.get(0).getPetrol_Cost();
                        }
                    } 
                    else {
                        if (stage[i] + 1 == stage[j]) { // for prev_stage and stage
                            for (int c = 0; c < LI.size(); c++) {
                                The_City city1 = LI.get(c);
                                dp[i][j] = city1.getHotel_Cost() + city1.getPetrol_Cost();
                                j++;
                            }
                            j--; 
                        } else { // Formula
                            for (int k = j; stage[k] > stage[i]; k--) {
                                if (stage[k] != stage[j] && stage[k] + 1 == stage[j]) {
                                    if (dp[i][j] > 0) {
                                        if (dp[i][j] > dp[i][k] + dp[k][j]) {
                                            Citydp[i][j] = city[k];
                                        } else if (dp[i][j] == dp[i][k] + dp[k][j]) {
                                            Citydp[i][j] = city[k] + "," + Citydp[i][j];
                                        }
                                        dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k][j]);
                                    } else {
                                        dp[i][j] = dp[i][k] + dp[k][j];
                                        Citydp[i][j] = city[k];
                                    }

                                }
                            }
                        }
                    }
                }
            }

            String[] s = Citydp[0][Citydp.length - 1].trim().split(",");
//            for(int i=0;i<s.length;i++) {
//            	System.out.println(s[i]);
//            }
            
            for (int h = 0; h < s.length; h++) {
                String str = s[h] + "," + City_End;
                for (int j = Citydp.length - 2; j > 0; j--) {
                    if (city[j].equals(s[h])) {
                        if (Citydp[0][j] != null) {
                            String st = str;
                            str = Citydp[0][j] + "," + st;
                            s[h] = Citydp[0][j];
                        }
                    }
                }
                String st = str;
                str = City_Start + "," + st;
                
                resultTextArea.appendText("The expected result:  " + str + " Cost:" + dp[0][number_of_cities - 1] + "\n\n");
            }
            
            for(int i=0;i<Citydp.length;i++) {
            	for(int j=0;j<Citydp.length;j++) {
            		if(Citydp[i][j]==null) {
            			Citydp[i][j]="";
            		}
            	}
            }

            for (int i = 0; i < number_of_cities; i++) {
                String[] row = new String[number_of_cities];

                row[0] = city[i];

                for (int j = 1; j < number_of_cities; j++) {
                    row[j] = String.valueOf(dp[i][j])+" "+Citydp[i][j];
                }
                dpTable.getItems().add(row);
            }

            for (int i = 0; i < number_of_cities; i++) {
                TableColumn<String[], String> column = new TableColumn<>(city[i]);
                final int columnIndex = i;
                column.setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue()[columnIndex]));
                dpTable.getColumns().add(column);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
