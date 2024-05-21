package application;

public class The_City {

	private String City_Name;
	private int Petrol_Cost;
	private int Hotel_Cost;
	
	public The_City(String City_Name,int Petrol_Cost,int Hotel_Cost)
	{
		this.City_Name=City_Name;
		this.Petrol_Cost=Petrol_Cost;
		this.Hotel_Cost=Hotel_Cost;
	}

	public String getCity_Name() {
		return City_Name;
	}

	public void setCity_Name(String city_Name) {
		City_Name = city_Name;
	}

	public int getPetrol_Cost() {
		return Petrol_Cost;
	}

	public void setPetrol_Cost(int petrol_Cost) {
		Petrol_Cost = petrol_Cost;
	}

	public int getHotel_Cost() {
		return Hotel_Cost;
	}

	public void setHotel_Cost(int hotel_Cost) {
		Hotel_Cost = hotel_Cost;
	}

	@Override
	public String toString() {
		return "The_City [City_Name=" + City_Name + ", Petrol_Cost=" + Petrol_Cost + ", Hotel_Cost=" + Hotel_Cost + "]";
	}
	
	
	
}
