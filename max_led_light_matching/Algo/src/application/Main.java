package application;
	
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;


public class Main extends Application {
	
	int arr_power[]=null;//for power 
	int arr_led[]=null; // for led 
	int res_arr[][]=null;
	ArrayList<Integer> al_led=null;
	
	ArrayList<HBox> al_left=new ArrayList<>();
	ArrayList<HBox> al_right=new ArrayList<>();
	
	
	
	@Override
	public void start(Stage primaryStage) {
		readData();
		startDynamic();
	    
		primaryStage.setMaximized(true);
		primaryStage.setTitle("Max LED Lighting");
		
		BorderPane bp=new BorderPane();
		bp.setStyle("-fx-background-color:#008000;-fx-Border-color:black;-fx-Border-width:1;");
		Scene s=new Scene(bp);
		primaryStage.setScene(s);
		
		// top
		ScrollPane sp=null;
			VBox vb=new VBox();
			vb.setPadding(new Insets(40, 0, 0, 350)); // top, right, bottom, left
			vb.setSpacing(20);
			sp=new ScrollPane(vb);
			sp.setPrefHeight(650);
			
			HBox hb=new HBox();
			
			HBox hb_Right=new HBox();
			Text t_r=new Text();
			t_r.setStyle("-fx-font-size:25;-fx-font-weight:bold;-fx-fill: white;");
			hb_Right.setAlignment(Pos.CENTER);
			hb_Right.setStyle("-fx-background-color:#006400;-fx-Border-color:black;-fx-Border-width:1;");
			hb_Right.setPrefSize(100, 40);
			
			HBox hb_Left=new HBox();
			Text t_l=new Text();
			t_l.setStyle("-fx-font-size:25;-fx-font-weight:bold;-fx-fill: white;");
			hb_Left.setAlignment(Pos.CENTER);
			hb_Left.setSpacing(15);
			hb_Left.setStyle("-fx-background-color:#006400;-fx-Border-color:black;-fx-Border-width:1;");
			hb_Left.setPrefSize(100, 40);
			
			
			for(int i=1;i<arr_led.length;i++) {
				al_left.add(hb_Left(i));
				al_right.add(hb_Right(i));
				vb.getChildren().add(hb(hb_Left(i), hb_Right(i)));
			}
		
			
		
		
		// bottom
		HBox hb_bottom=new HBox();
		hb_bottom.setSpacing(100);
		hb_bottom.setAlignment(Pos.CENTER_LEFT);
		hb_bottom.setPadding(new Insets(0, 0, 0, 10)); // top, right, bottom, left
		Text t_res=new Text();
		Text t_res1=new Text();
		t_res.setStyle("-fx-font-size:25;-fx-font-weight:bold;-fx-fill: white;");
		
		t_res.setText("The Result: ");
		
		HBox hb_bottom_right=new HBox();
		Button b_calc=new Button("Calculate");
		Button b_showTable=new Button("Show table");
		b_showTable.setVisible(false);
		hb_bottom_right.getChildren().addAll(b_calc,b_showTable);
		hb_bottom_right.setAlignment(Pos.CENTER);
		hb_bottom_right.setSpacing(20);
		hb_bottom_right.setPadding(new Insets(0, 0, 0, 900)); // top, right, bottom, left
		
		hb_bottom.getChildren().addAll(t_res,t_res1,hb_bottom_right);
		
		b_calc.setOnAction(e->{
			b_showTable.setVisible(true);
			t_res.setText("The Result:  "+al_led.size());
			
			
			
			for (int i = 0; i < al_left.size(); i++) {
			    HBox currentHBox = al_left.get(i);
			    if (currentHBox.getChildren().get(1) instanceof Text) {
			        Text textLabel = (Text) currentHBox.getChildren().get(1);
			        
			        
			        if (al_led.contains(Integer.parseInt(textLabel.getText()))) {
			            currentHBox.getChildren().clear();
			            currentHBox.getChildren().addAll(new ImageView(new Image(getClass().getResource("/application/work_led.png").toExternalForm())), textLabel);
			            al_left.set(i, currentHBox); 
			        }
			    }
			    
			}
			
			for (int i = 0; i < al_right.size(); i++) {
			    HBox currentHBox = al_right.get(i);

			    if (currentHBox.getChildren().get(1) instanceof Text) {
			        Text textLabel = (Text) currentHBox.getChildren().get(1);

			        if (al_led.contains(Integer.parseInt(textLabel.getText()))) {
			            currentHBox.getChildren().clear();
			            currentHBox.getChildren().addAll(new ImageView(new Image(getClass().getResource("/application/full-battery.png").toExternalForm())), textLabel);
			            al_right.set(i, currentHBox); 
			        }
			    }
			}
			
			vb.getChildren().clear();
			
			for(int i=0;i<arr_led.length-1;i++) {
				vb.getChildren().add(hb(al_left.get(i), al_right.get(i)));
			}
			
			
		});
		
		b_showTable.setOnAction(e->{
			Stage s_st=new Stage();
			s_st.setTitle("DP Table");
			
			TextArea ta=new TextArea();
			ta.setStyle("-fx-font-size:20;-fx-font-weight:bold;");
			ta.setEditable(false);
			ta.setText(printDPTable());
			
			Scene s_scence=new Scene(ta);
			s_st.setScene(s_scence);
			s_st.show();
		});
		
		//--------------------------------------
		bp.setTop(sp);
		bp.setCenter(hb_bottom);
		
		
		primaryStage.show();
	}
	
	
	public void readData() {
		try {
			File f=new File("src\\application\\data.txt");
			Scanner in=new Scanner(f);
			int num_line=0;
			String arr_string[]=null;
			while(in.hasNextLine()) {
				String s=in.nextLine();
				if(num_line==0) {
					arr_power=new int[(Integer.parseInt(s))+1];
					arr_led=new int[(Integer.parseInt(s))+1 ];
					for(int i=0;i<arr_power.length;i++) {
						arr_power[i]=i;
					}
				}
				else if(num_line==1) {
					arr_string=s.split(" ");
					arr_led[0]=0;
					for(int i=0;i<arr_string.length;i++) {
						arr_led[i+1]=Integer.parseInt(arr_string[i]);
					}
				}
				num_line++;
			}
			
			in.close();
		}
		catch(Exception e) {
			System.out.println("file not found");
		}
	}
	
	public void startDynamic() {
		res_arr=new int[arr_led.length][arr_power.length];
		al_led=new ArrayList<>();
		
		for(int i=0;i<arr_led.length;i++) {
			for(int j=0;j<arr_power.length;j++) {
				
				if(i==0 || j==0) { // init case
					res_arr[i][j]=0;
				}
				else if(arr_led[i]==arr_power[j]) { // x=y
					
					res_arr[i][j]=res_arr[i-1][j-1]+1;
					
				}
				else if(arr_led[i]!=arr_power[j]){ // x!=y
					res_arr[i][j]=Integer.max(res_arr[i-1][j], res_arr[i][j-1]);
				}
				
			}
		}
		
		int x =res_arr[arr_led.length-1][arr_power.length-1];
		//System.out.println(x);
		//System.out.println(arr_led.length-1);

		for(int i= arr_led.length -1 ; i>=1;i--) {
			for(int j=arr_power.length-1;j>=1;j--) {
				if(arr_led[i]== arr_power[j]) {
					if(res_arr[i][j] == x) {
						al_led.add(arr_led[i]);
						x--;
					}
				}
			}
	}
	}

	public String printDPTable() {
		String s="the table: "+"\n";
		
		for(int i=0;i<arr_led.length;i++) {
			s+="   "+arr_led[i];
		}
		s+="\n";
		for (int j = 0; j < arr_power.length; j++) {
			s+=arr_power[j];
            for (int i = 0; i < arr_led.length; i++) {
            	s+="  "+res_arr[i][j]+" ";
            }
            s+=" "+"\n";
        }
		
		return s;
	}
	
	public HBox hb_Right(int index) {
		HBox hb_right=new HBox();
		Image image_power = new Image(getClass().getResource("/application/low-battery.png").toExternalForm());

		Text t=new Text();
		t.setStyle("-fx-font-size:25;-fx-font-weight:bold;-fx-fill: white;");
		t.setText(arr_power[index]+"");
		hb_right.getChildren().addAll(new ImageView(image_power),t);
		hb_right.setAlignment(Pos.CENTER);
		hb_right .setSpacing(15);
		hb_right.setStyle("-fx-background-color:#006400;-fx-Border-color:black;-fx-Border-width:1;");
		hb_right.setPrefSize(100, 40);
		
		return hb_right;
	}
	
	public HBox hb_Left(int index) {
		HBox hb_left=new HBox();
		Image image_led = new Image(getClass().getResource("/application/notwork_led.png").toExternalForm());
		
		Text t=new Text();
		t.setStyle("-fx-font-size:25;-fx-font-weight:bold;-fx-fill: white;");
		t.setText(arr_led[index]+"");
		
		hb_left.getChildren().addAll(new ImageView(image_led),t);
		hb_left.setAlignment(Pos.CENTER);
		hb_left.setSpacing(15);
		hb_left.setStyle("-fx-background-color:#006400;-fx-Border-color:black;-fx-Border-width:1;");
		hb_left.setPrefSize(100, 40);
		
		return hb_left;
	}
	
	public HBox hb(HBox hb_Left,HBox hb_Right) {
		HBox hb=new HBox();
		hb.getChildren().addAll(hb_Left,hb_Right);
		hb.setSpacing(650);
		return hb;
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
