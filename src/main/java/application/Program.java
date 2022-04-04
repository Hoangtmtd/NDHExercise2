package application;

import entity.Customer;
import entity.Rental;
import entity.Vehicle;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.hibernate.Session;
import service.CustomerService;
import service.RentalService;
import service.VehicleService;
import service.impl.CustomerServiceImpl;
import service.impl.RentalServiceImpl;
import service.impl.VehicleServiceImpl;
import util.HibernateUtils;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Program {
    private static Scanner sc;
    private static final VehicleService vehicleService = new VehicleServiceImpl();
    private static final CustomerService customerService = new CustomerServiceImpl();
    private static final RentalService rentalService = new RentalServiceImpl();

    static {
        HibernateUtils.getSession().close();
    }

    /**
     * @return option type int
     */
    public static int option() {
        while (true) {
            System.out.println("------------------------------------------------");
            System.out.println("             Car Rental Management              ");
            System.out.println("1.  Add new customer");
            System.out.println("2.  Record data");
            System.out.println("3.  Return vehicle");
            System.out.println("4.  Display all vehicles");
            System.out.println("5.  Display all available vehicles");
            System.out.println("6.  Add new vehicle");
            System.out.println("7.  Delete exist vehicle");
            System.out.println("8.  Import Csv file");
            System.out.println("9.  Export csv file");
            System.out.println("\n0.  Exit program");
            System.out.println("-----------------------------------------------");
            System.out.print("Your option: ");
            try {
                int op = Integer.parseInt(sc.nextLine());
                if (op >= 0 && op <= 9) {
                    return op;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option!");
            }
        }
    }

    public static void main(String[] args) {
        sc = new Scanner(System.in);
        int op = option();
        while (op != 0) {
            switch (op) {
                case 1:
                    addNewCustomer();
                    break;
                case 2:
                    recordRentalData();
                    break;
                case 3:
                    returnVehicle();
                    break;
                case 4:
                    displayAllVehicle();
                    break;
                case 5:
                    displayAllAvailableVehicle();
                    break;
                case 6:
                    addNewVehicle();
                    break;
                case 7:
                    deleteVehicle();
                    break;
                case 8:
                    importData();
                    break;
                case 9:
                    exportData();
                    break;
            }
            op = option();
        }
        exitProgram();
    }


    public static void addNewCustomer() {
        System.out.println("Enter customer name:");
        String name = sc.nextLine();
        System.out.println("Enter date of birth(yyyy/MM/dd):");
        LocalDate dob = LocalDate.parse(sc.nextLine().trim(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        Customer customer = Customer.builder()
                .name(name)
                .dob(dob)
                .build();
        customerService.insert(customer);
        System.out.println("Add successfully!");
    }

    public static void recordRentalData() {
        System.out.println("Enter customer id:");
        Long customerId = Long.parseLong(sc.nextLine());
        displayAllAvailableVehicle();
        System.out.println("Enter vehicle id as below:");
        Long vehicleId = Long.parseLong(sc.nextLine());
        Customer customer = customerService.getById(customerId);
        Vehicle vehicle = vehicleService.getById(vehicleId);
        Rental rental = Rental.builder()
                .vehicle(vehicle)
                .customer(customer)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(7L))
                .build();
        if (rentalService.insert(rental)) {
            System.out.println("Save rental successfully!");
        } else {
            System.out.println("Fail to save rental!");
        }
    }

    public static void returnVehicle() {
        System.out.println("Enter the vehicle id to return:");
        Long id = Long.parseLong(sc.nextLine());
        if (vehicleService.returnVehicle(id)) {
            System.out.println("Return successfully!");
        } else {
            System.out.println("Fail to return vehicle!");
        }
    }

    public static void displayAllVehicle() {
        vehicleService.getAllVehicle().forEach(System.out::println);
    }

    public static void displayAllAvailableVehicle() {
        vehicleService.findAllAvailableVehicle().forEach(System.out::println);
    }

    public static void addNewVehicle() {
        System.out.println("Enter brand:");
        String brand = sc.nextLine();
        System.out.println("Enter model:");
        String model = sc.nextLine();
        System.out.println("Enter number of seats:");
        int numberOfSeats = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter license plate:");
        String licensePlate = sc.nextLine();
        Vehicle vehicle = Vehicle.builder()
                .brand(brand)
                .model(model)
                .numberOfSeats(numberOfSeats)
                .licensePlate(licensePlate)
                .build();
        vehicleService.insert(vehicle);
        System.out.println("Add successfully!");

    }

    public static void deleteVehicle() {
        System.out.println("Enter vehicle id to delete:");
        Long id = Long.parseLong(sc.nextLine());
        vehicleService.deleteById(id);
        System.out.println("Delete successfully!");
    }

    public static void importData() {
        String csvFilePath = "src\\main\\resources\\Data.csv";
        try {
            BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
            CSVParser records = CSVParser.parse(lineReader, CSVFormat.EXCEL.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());
            Session session = HibernateUtils.getSession();
            session.beginTransaction();
            for (CSVRecord record : records) {
                Vehicle vehicle = new Vehicle();
                vehicle.setBrand(record.get(0));
                vehicle.setModel(record.get(1));
                vehicle.setNumberOfSeats(Integer.parseInt(record.get(2)));
                vehicle.setLicensePlate(record.get(3));
                session.save(vehicle);
            }
            session.getTransaction().commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Import Successfully ");
    }

    public static void exportData() {
        String csvFilePath = "src\\main\\resources\\output.csv";
        List<Vehicle> vehicles = vehicleService.getAllVehicle();
        try {
            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(csvFilePath));
            fileWriter.write("brand,model,number_of_seat,license_plate");
            Session session = HibernateUtils.getSession();
            session.beginTransaction();
            for (Vehicle vehicle : vehicles) {
                fileWriter.append(vehicle.getBrand());
                fileWriter.append(vehicle.getModel());
                fileWriter.append(Character.highSurrogate(vehicle.getNumberOfSeats()));
                fileWriter.append(vehicle.getLicensePlate());
            }
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Export Successfully");
    }

    public static void exitProgram() {
        System.out.println("Bye!");
    }
}
