package jdbc;

/* CISC332 Project Part 4 (1 File - Project.java)
*  Tony Wang
*  7tdw
*/

//package jdbc;

import java.sql.*;
import java.math.*;
import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;

public class Project {
	static {
		try {
			Class.forName("COM.ibm.db2.jdbc.app.DB2Driver").newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		Connection con = null;
		String url = "jdbc:db2:cisc332g";

		// setup DB2 console for input
		// Prompt for userID & passwd - pw masked
		while (true) {

			try {
				Console c = System.console();
				if (c == null) {
					System.err.println("No console.");
					System.exit(1);
				}
				String login = c.readLine("Enter your login ID: ");
				char[] password = c.readPassword("Enter your password: \n");
				String pw = new String(password);

				con = DriverManager.getConnection(url, login, pw);

				while (true) {

					String actionPrompt = "\nSelect an action by entering its number: \n"
							+ "\n1. Add an animal."
							+ "\n2. Delete an animal."
							+ "\n3. Adopt an animal."
							+ "\n4. Delete an employee."
							+ "\n5. Display Database." 
							+ "\n6. Exit.\n";
					// + "\n6. Vets
					// + "\n7. More employee options

					String actionSelection = c.readLine(actionPrompt);
					char response = actionSelection.charAt(0);

					if (Character.isDigit(actionSelection.charAt(0))) {
						switch (Integer.parseInt(actionSelection)) {
						case 1:
							addAnimal(con);
							break;
						case 2:
							deleteAnimal(con);
							break;
						case 3:
							adoptAnimal(con, c);
							break;
						case 4:
							deleteEmployee(con, c);
							break;
						case 5:
							displayDatabase(con);
							break;
						case 6:
							con.close();
							System.out.println("Program Exiting.");
							System.exit(0);
						default:
							System.out.println("Invalid selection.");
							break;
						}
					} else
						System.out.println("Invalid selection.");
				}

			} catch (Exception e) {
				System.out.println("Console setup failed.");
				e.printStackTrace();
			}
		}
	}

	private static void addAnimal(Connection con2) throws SQLException {
		Statement s = con2.createStatement();
		int A_ID, PrimeCare_ID, Vet_ID;
		String A_Name, Species, Colour, Gender, DoB, DoA, Breed, Comment, ArrivalNotes;

		Console c2 = System.console();
		
		A_Name = c2.readLine("Give the new animal a name: ");
		Species = c2.readLine("What species is " + A_Name + "?");
		Colour = c2.readLine("What colour is " + A_Name + "?");
		Gender = c2.readLine("Enter " + A_Name + "'s gender (M/F): ");
		Gender = Gender.toUpperCase();
		Breed = c2.readLine("What's " + A_Name + "'s breed?");
		DoB = c2.readLine("Enter " + A_Name
				+ "'s date of birth in YYYY-MM-DD format: ");
		DoA = c2.readLine("Enter " + A_Name
				+ "'s date of arrival in YYYY-MM-DD format: ");
		Comment = c2.readLine("Any special considerations for " + A_Name + "?");
		ArrivalNotes = c2.readLine("What's the condition in which " + A_Name
				+ " arrived in?");

		int lastAnimalID = fetchLastRow(s, "a_id", "animal");
		A_ID = lastAnimalID + 1;

		int[] Employee = fetchTableCol(s, "E_ID", "Employee");
		if (Employee[0] == 0)
			PrimeCare_ID = 1001;
		else {
			PrimeCare_ID = Employee[(int)((double)Math.random() * (double)(Employee.length - 1))];
		}
		
		int AdditionCare_ID = Employee[(int)((double)Math.random() * (double)(Employee.length - 1))];
		
		while (AdditionCare_ID == PrimeCare_ID)
			AdditionCare_ID = Employee[(int)((double)Math.random() * (double)(Employee.length - 1))];

		Vet_ID = (int) (Math.random() * (double) 4) + 3000;

		boolean temp = s.execute("insert into animal values " + "(" + A_ID
				+ ",\'" + A_Name + "\',\'" + Species + "\',\'" + Colour
				+ "\',\'" + Gender + "\',\'" + DoB + "\',\'" + DoA + "\',\'"
				+ Breed + "\',\'" + Comment + "\',\'" + ArrivalNotes + "\',"
				+ "NULL, NULL," + PrimeCare_ID + "," + Vet_ID + ");");

		s.execute("insert into responsible_for values (" + A_ID
				+ "," + Vet_ID + ");");
		
		s.execute("insert into additional_care values (" + A_ID
				+ "," + AdditionCare_ID + ");");
		
		s.close();
		System.out.println("\nNew animal added.\n");
	}

	private static int fetchLastRow(Statement s2, String colName, String tableName) throws SQLException {
		int[] column = fetchTableCol(s2, colName, tableName);
		String tn = tableName.toLowerCase();
		if (column.length == 0) {
			if (tn == "animal")
				return 4000;
			else if (tn == "employee")
				return 1000;
		} 
		
		return column[column.length - 1];
	}

	private static int[] fetchTableCol(Statement s2, String colName, String tableName) throws SQLException {
		int[] column;

		ResultSet r2;
		r2 = s2.executeQuery("select " + colName + " from " + tableName);
		
		int i = 0;
		while (r2.next()) {
			i++;
		}
		if (i > 0) {
			column = new int[i];
			r2 = s2.executeQuery("select " + colName + " from " + tableName);
			
			i = 0;
			while (r2.next()) {
				column[i++] = r2.getInt(colName);
			}
			r2.close();
			return column;
		}
		r2.close();
		column = new int[1];
		column[0] = 0;
		return column;
	}
	
	private static void adoptAnimal(Connection con2, Console c2) throws SQLException {
		Statement s2 = con2.createStatement();
		int a_id, ad_id;
		String ad_ids, ad_date;
		while (true) {
			try {
				a_id = Integer.parseInt(c2.readLine("Enter the Animal ID of the animal to be adopted: "));
				break;
			}
			catch (Exception e) {
				System.out.println("no number !!!!!!!!!!!!!");//
			}
		}
		
		
		ad_ids = c2.readLine("Enter the Adopter's ID (Leave blank if adopter is new): ");
		ad_date = c2.readLine("Enter the date of adoption in YYYY-MM-DD format: ");
			
		if (ad_ids.length() > 0)
			ad_id = Integer.parseInt(ad_ids);
		else
			ad_id = fetchLastRow(s2, "AF_ID", "Adoptive_Family") + 1;
		
		if (ad_ids.length() == 0) {
			String address = c2.readLine("Enter the new adoptive family's current address: ");
			String phone = c2.readLine("Enter the new adoptive family's 11-digit phone number: ");
			s2.execute("insert into adoptive_family values ("
						+ ad_id + ",\'" + address + "\',\'" + phone + "\')");
		}
		
		s2.executeUpdate("update animal " +
						 "set (adopter_id, adoption_date) = (" + ad_id + ",\'" + ad_date + "\') " +
						 "where a_id = " + a_id);		

		s2.close();
		System.out.println("\nAnimal has been adopted.\n");
	}

	private static void deleteEmployee(Connection con2, Console c2) throws SQLException {
				
		String e_ids = c2.readLine("\nEnter the ID of the employee to be deleted: ");
		int e_id = Integer.parseInt(e_ids);
		
		Statement s2 = con2.createStatement();
		
		int a_id[] = fetchTableCol(s2, "a_id", "animal");
		int a_id_2ndCare[] = fetchTableCol(s2, "animal_id", "Additional_Care");
		int PIDa[] = fetchTableCol(s2, "E_ID", "Employee");
		
		for (int i = 0; i < a_id.length; i++) {
			int newPID = PIDa[(int)((double)Math.random() * (double)(PIDa.length - 1))];
			while (newPID == e_id)
				newPID = PIDa[(int)((double)Math.random() * (double)(PIDa.length - 1))];
			
			s2.executeUpdate("update animal set Prime_Care_ID = " + newPID 
					   	   + " where a_id = " + a_id[i]);
		}

		deleteTuple(con2, "employee", "E_ID", e_ids);
		s2.close();
		System.out.println("Employee successfully deleted.");
	}
	
	private static void displayDatabase(Connection con2) throws SQLException {
		Statement s2 = con2.createStatement();
		ResultSet r2 = s2.executeQuery("select * from vet");
		
		displayEmployee(s2, r2);
		displayVet(s2, r2);
		displayAnimal(s2, r2);
		displayRespFor(s2, r2);
		display2ndCare(s2, r2);
		displayAppt(s2, r2);
		displayAF(s2, r2);
		displayAFName(s2, r2);
		
		r2.close();
		s2.close();
	}
	
	private static void displayEmployee(Statement s3, ResultSet r3) throws SQLException {
		r3 = s3.executeQuery("select * from Employee");
		System.out.printf(
				"%n<Employee>%n%n%11s\t %-8s\t      %-8s\t\t   %-11s\t%n",
				"Employee ID", "First Name", "Last Name", "Phone Number");
		System.out
				.println("-------------------------------------------------------"
						+ "------------------------");
		while (r3.next()) {
			int E_ID = r3.getInt(1);
			String fname = r3.getString(2);
			String lname = r3.getString(3);
			String phone = r3.getString(4);
			System.out.printf("%4d\t\t %-8s %-8s %-11s%n", E_ID, fname, lname,
					phone);
		}
		System.out.println();		
	}
	
	private static void displayAF(Statement s3, ResultSet r3) throws SQLException {
		r3 = s3.executeQuery("select * from adoptive_family");
		System.out.printf(
				"%n<Adoptive Family>%n%n%18s\t  %-18s\t\t\t %-11s\t%n",
				"Adoptive Family ID", "Address", "Phone Number");
		System.out
				.println("-------------------------------------------------------"
						+ "------------------------");
		while (r3.next()) {
			int AF_ID = r3.getInt(1);
			String addr = r3.getString(2);
			String phone = r3.getString(3);
			System.out.printf("%4d\t\t\t  %-30s\t %11s%n", AF_ID, addr, phone);
		}
		System.out.println();
	}
	
	private static void displayAFName(Statement s3, ResultSet r3) throws SQLException {
		r3 = s3.executeQuery("select * from AF_Name");
		System.out.printf("%n<Adoptive Family Names>%n%n%-10s\t %-15s%n",
				"AF_ID", "Name");
		System.out
				.println("-------------------------------------------------------");
		while (r3.next()) {
			int AF_ID = r3.getInt(1);
			String name = r3.getString(2);
			System.out.printf("%4d\t\t %4s\t\t %n", AF_ID, name);
		}
		System.out.println();
	}
	
	private static void displayAnimal(Statement s3, ResultSet r3) throws SQLException {
		r3 = s3.executeQuery("select * from Animal");
		System.out
				.printf("%n<Animal>%n%n%9s   %-20s %-20s %-20s%-7s%-16s%-16s%-23s\t%-19s   %18s\t%8s\t%13s\t%15s\t%8s\t%n",
						"Animal ID", "Name", "Species", "Colour", "Gender",
						"Birth Date", "Arrival Date", "Breed",
						"Special Considerations", "Arrival Condition",
						"Adopter ID", "Adoption Date", "Primary Care ID",
						"Vet ID");
		System.out
				.println("-------------------------------------------------------"
						+ "-------------------------------------------------------"
						+ "------------------------");
		while (r3.next()) {
			int A_ID = r3.getInt(1);
			String name = r3.getString(2);
			String species = r3.getString(3);
			String colour = r3.getString(4);
			String gender = r3.getString(5);
			String dob = r3.getString(6);
			String doa = r3.getString(7);
			String breed = r3.getString(8);
			String comment = r3.getString(9);
			String notes = r3.getString(10);
			int AF_ID = r3.getInt(11);
			String AF_IDS = "NULL";
			if (AF_ID != 0)
				AF_IDS = Integer.toString(AF_ID);
			String adate = r3.getString(12);
			int primeCare = r3.getInt(13);
			int vet_ID = r3.getInt(14);
			System.out
					.printf("%4d\t    %s %s %s %2s\t %s\t %s\t %-10s%-15s\t %-15s\t %4s\t  %10s\t\t %4d\t\t %4d%n%n",
							A_ID, name, species, colour, gender, dob, doa,
							breed, comment, notes, AF_IDS, adate, primeCare,
							vet_ID);
		}
		System.out.println();
	}
	
	private static void displayVet(Statement s3, ResultSet r3) throws SQLException {
		r3 = s3.executeQuery("select * from vet");
		System.out
				.printf("%n<Veterinarian>%n%n%-7s\t %-10s\t     %-10s\t         %-11s\t\t\t\t          %11s%n",
						"Vet ID", "First Name", "Last Name", "Address",
						"Phone Number");
		System.out
				.println("-------------------------------------------------------"
						+ "-------------------------------------------------------");
		while (r3.next()) {
			int V_ID = r3.getInt(1);
			String fname = r3.getString(2);
			String lname = r3.getString(3);
			String addr = r3.getString(4);
			String phone = r3.getString(5);
			System.out.printf("%4d\t %6s%6s%-40s\t  %11s%n", V_ID, fname,
					lname, addr, phone);
		}
		System.out.println();
	}
	
	private static void display2ndCare(Statement s3, ResultSet r3) throws SQLException {
		r3 = s3.executeQuery("select * from additional_care");
		System.out.printf("%n<Secondary Care Giver>%n%n%-10s\t %-15s%n",
				"Animal ID", "Employee ID");
		System.out
				.println("-------------------------------------------------------");
		while (r3.next()) {
			int A_ID = r3.getInt(1);
			int E_ID = r3.getInt(2);
			System.out.printf("%4d\t\t %4d\t\t %n", A_ID, E_ID);
		}
		System.out.println();
	}
	
	private static void displayAppt(Statement s3, ResultSet r3) throws SQLException {
		r3 = s3.executeQuery("select * from appointment");
		System.out.printf(
				"%n<Appointment>%n%n%-10s\t%-15s  %-15s %-7s\t %-15s%n",
				"Animal ID", "Vet ID", "Date of Visit", "Weight (lb)",
				"Health Concern");
		System.out
				.println("-------------------------------------------------------"
						+ "-------------------------------------------------------");
		while (r3.next()) {
			int A_ID = r3.getInt(1);
			int V_ID = r3.getInt(2);
			String date = r3.getString(3);
			int weight = r3.getInt(4);
			String health = r3.getString(5);
			System.out.printf("%4d\t\t%4d\t\t %-12s\t %-7d\t %s%n", A_ID, V_ID,
					date, weight, health);
		}
		System.out.println();
	}
	
	private static void displayRespFor(Statement s3, ResultSet r3) throws SQLException {
		r3 = s3.executeQuery("select * from responsible_for");
		System.out.printf("%n<Responsible For>%n%n%-10s\t %-15s%n",
				"Animal ID", "Vet ID");
		System.out
				.println("-------------------------------------------------------");
		while (r3.next()) {
			int A_ID = r3.getInt(1);
			int V_ID = r3.getInt(2);
			System.out.printf("%4d\t\t %4d\t\t %n", A_ID, V_ID);
		}
		System.out.println();
	}
	
	private static void deleteAnimal(Connection con2) throws SQLException {
		Console c2 = System.console();
		String cond = c2.readLine("Enter ID of the animal to be deleted: ");
		deleteTuple(con2, "animal", "a_id", cond);
	}
	
	private static void deleteTuple(Connection con3, String tableName, String colName, String condition) throws SQLException {
		Statement s2 = con3.createStatement();
		boolean temp = s2.execute("delete from " + tableName + " where "
								 + colName + " = " + condition);		
	}


}
