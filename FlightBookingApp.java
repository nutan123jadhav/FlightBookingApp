import java.util.ArrayList;
import java.util.Scanner;

// Flight class
class Flight {
    private String code;
    private String airline;
    private String source;
    private String destination;
    private int seats;
    private double fare;

    public Flight(String code, String airline, String source,
                  String destination, int seats, double fare) {
        this.code = code;
        this.airline = airline;
        this.source = source;
        this.destination = destination;
        this.seats = seats;
        this.fare = fare;
    }

    public String getCode() {
        return code;
    }

    public String getAirline() {
        return airline;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public int getSeats() {
        return seats;
    }

    public double getFare() {
        return fare;
    }

    public boolean reserveSeats(int count) {
        if (count > 0 && count <= seats) {
            seats = seats - count;
            return true;
        }
        return false;
    }

    public void displayFlight() {
        System.out.println(code + " | " + airline + " | "
                + source + " to " + destination
                + " | Seats: " + seats
                + " | Fare: Rs. " + fare);
    }
}

// Abstract Passenger class
abstract class Passenger {
    private String name;
    private int age;

    public Passenger(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public abstract double getDiscount();
    public abstract String getType();
}

// Regular passenger
class RegularPassenger extends Passenger {

    public RegularPassenger(String name, int age) {
        super(name, age);
    }

    public double getDiscount() {
        return 0;
    }

    public String getType() {
        return "Regular";
    }
}

// Student passenger
class StudentPassenger extends Passenger {

    public StudentPassenger(String name, int age) {
        super(name, age);
    }

    public double getDiscount() {
        return 10;
    }

    public String getType() {
        return "Student";
    }
}

// Booking class
class Booking {
    private String bookingId;
    private Flight flight;
    private Passenger passenger;
    private int tickets;
    private double subtotal;
    private double discount;
    private double total;

    public Booking(String bookingId, Flight flight,
                   Passenger passenger, int tickets) {
        this.bookingId = bookingId;
        this.flight = flight;
        this.passenger = passenger;
        this.tickets = tickets;

        subtotal = flight.getFare() * tickets;
        discount = subtotal * passenger.getDiscount() / 100;
        total = subtotal - discount;
    }

    public void displayBooking() {
        System.out.println("\n----- Booking Summary -----");
        System.out.println("Booking ID: " + bookingId);
        System.out.println("Flight: " + flight.getCode());
        System.out.println("Airline: " + flight.getAirline());
        System.out.println("Route: " + flight.getSource()
                + " to " + flight.getDestination());
        System.out.println("Passenger: " + passenger.getName());
        System.out.println("Age: " + passenger.getAge());
        System.out.println("Passenger Type: " + passenger.getType());
        System.out.println("Tickets: " + tickets);
        System.out.println("Base Fare per Ticket: Rs. "
                + flight.getFare());
        System.out.println("Subtotal: Rs. " + subtotal);
        System.out.println("Discount: Rs. " + discount);
        System.out.println("Final Amount: Rs. " + total);
        System.out.println("---------------------------");
    }
}

// Main booking system
class FlightBookingSystem {
    private ArrayList<Flight> flights = new ArrayList<>();
    private ArrayList<Booking> bookings = new ArrayList<>();
    private int bookingNumber = 1001;

    public FlightBookingSystem() {
        flights.add(new Flight("PN101", "Air India",
                "Pune", "Delhi", 20, 4000));

        flights.add(new Flight("MB202", "IndiGo",
                "Mumbai", "Bengaluru", 25, 3500));

        flights.add(new Flight("PN303", "Akasa Air",
                "Pune", "Hyderabad", 18, 3000));
    }

    public void displayFlights() {
        System.out.println("\n----- Available Flights -----");
        for (Flight f : flights) {
            f.displayFlight();
        }
    }

    public Flight findFlight(String code) {
        for (Flight f : flights) {
            if (f.getCode().equalsIgnoreCase(code)) {
                return f;
            }
        }
        return null;
    }

    public boolean validateBooking(Flight flight, String name,
                                   int age, int tickets) {
        if (flight == null) {
            System.out.println("Invalid flight code.");
            return false;
        }

        if (name.trim().isEmpty()) {
            System.out.println("Passenger name cannot be blank.");
            return false;
        }

        if (age <= 0) {
            System.out.println("Age must be greater than zero.");
            return false;
        }

        if (tickets <= 0) {
            System.out.println("Tickets must be greater than zero.");
            return false;
        }

        if (tickets > flight.getSeats()) {
            System.out.println("Not enough seats available.");
            return false;
        }

        return true;
    }

    public void bookFlight(Scanner sc) {
        System.out.print("Enter flight code: ");
        String code = sc.nextLine().trim();

        Flight flight = findFlight(code);

        System.out.print("Enter passenger name: ");
        String name = sc.nextLine().trim();

        int age;
        int tickets;

        try {
            System.out.print("Enter age: ");
            age = Integer.parseInt(sc.nextLine().trim());

            System.out.print("Passenger type (Regular/Student): ");
            String type = sc.nextLine().trim();

            if (!type.equalsIgnoreCase("Regular")
                    && !type.equalsIgnoreCase("Student")) {
                System.out.println("Invalid passenger type.");
                return;
            }

            System.out.print("Enter number of tickets: ");
            tickets = Integer.parseInt(sc.nextLine().trim());

            if (!validateBooking(flight, name, age, tickets)) {
                return;
            }

            Passenger passenger;

            if (type.equalsIgnoreCase("Student")) {
                passenger = new StudentPassenger(name, age);
            } else {
                passenger = new RegularPassenger(name, age);
            }

            // Update seats only after all validations pass
            if (flight.reserveSeats(tickets)) {
                String id = "BK" + bookingNumber++;

                Booking booking = new Booking(
                        id, flight, passenger, tickets);

                bookings.add(booking);

                System.out.println("\nBooking confirmed!");
                booking.displayBooking();
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Enter a valid number.");
        }
    }

    public void viewBooking() {
        if (bookings.isEmpty()) {
            System.out.println("No confirmed booking available.");
        } else {
            bookings.get(bookings.size() - 1).displayBooking();
        }
    }
}

// Main class
public class FlightBookingApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        FlightBookingSystem system = new FlightBookingSystem();

        while (true) {
            System.out.println("\nWelcome to Kiran Air Booking");
            System.out.println("1. View Flights");
            System.out.println("2. Book Flight");
            System.out.println("3. View Booking");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1":
                    system.displayFlights();
                    break;

                case "2":
                    system.bookFlight(sc);
                    break;

                case "3":
                    system.viewBooking();
                    break;

                case "4":
                    System.out.println(
                            "Thank you for using Kiran Air Booking!");
                    sc.close();
                    return;

                default:
                    System.out.println(
                            "Invalid choice. Please select 1 to 4.");
            }
        }
    }
}