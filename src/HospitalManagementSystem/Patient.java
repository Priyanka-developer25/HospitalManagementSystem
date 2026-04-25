package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class Patient {

    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner){
        this.connection= connection;
        this.scanner= scanner;
    }

    public void addPatient(){
        System.out.print("Enter Patient name: ");
        String name= scanner.next();
        System.out.print("Enter patient age: ");
        int age= scanner.nextInt();
        System.out.print("Enter Patient Gender: ");
        String gender= scanner.next();

        try{
            String query= "INSERT INTO patients(name, age, gender) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows>0){
                System.out.println("Patient Added Successfully!!");
            }else{
                System.out.println("Failed to add Patient!!");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void viewPatients(){
        String query = "select * from patients";
        try{
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            ResultSet resultSet= preparedStatement.executeQuery();
            System.out.println("Patients: ");
            System.out.println("+------------+--------------+---------+--------------+");
            System.out.println("| Id  Patient| Name         | Age     | Gender       | ");
            System.out.println("+------------+--------------+---------+--------------+");
            while(resultSet.next()){
                int id =resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("|%-12s|%-14s|%-9s|%-14s|\n", id, name, age, gender);
                System.out.println("+------------+--------------+---------+--------------+");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int id){
        String query = "SELECT * FROM patients WHERE id = ?";
        try{
           PreparedStatement preparedStatement = connection.prepareStatement(query);
           preparedStatement.setInt(1, id);
           ResultSet resultSet = preparedStatement.executeQuery();
           if(resultSet.next()){
               return true;
           }else{
               return false;
           }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public void deletePatient(int patientId) {
        try {
            // Step 1: Delete from appointment table
            String deleteAppointments = "DELETE FROM appointments WHERE patient_id = ?";
            PreparedStatement ps1 = connection.prepareStatement(deleteAppointments);
            ps1.setInt(1, patientId);
            ps1.executeUpdate();

            // Step 2: Delete from patient table
            String deletePatient = "DELETE FROM patients WHERE id = ?";
            PreparedStatement ps2 = connection.prepareStatement(deletePatient);
            ps2.setInt(1, patientId);

            int rows = ps2.executeUpdate();

            if (rows > 0) {
                System.out.println("Patient and related appointments deleted successfully!");
            } else {
                System.out.println("Patient not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
