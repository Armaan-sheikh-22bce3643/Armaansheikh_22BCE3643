// Required Libraries: MongoDB Java Driver (https://mongodb.github.io/mongo-java-driver/)
// Add this JAR to Eclipse build path

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.util.Scanner;

public class LostFoundJavaApp {
    static MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    static MongoDatabase database = mongoClient.getDatabase("lostfound");
    static MongoCollection<Document> users = database.getCollection("users");
    static MongoCollection<Document> reports = database.getCollection("reports");

    static Scanner sc = new Scanner(System.in);
    static String loggedInUserId = null;

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n1. Register\n2. Login\n3. Report Lost/Found\n4. View My Reports\n5. Update Status\n6. Delete Report\n7. Exit");
            int ch = sc.nextInt(); sc.nextLine();
            switch (ch) {
                case 1: register(); break;
                case 2: login(); break;
                case 3: ifAuth(LostFoundJavaApp::reportItem); break;
                case 4: ifAuth(LostFoundJavaApp::viewReports); break;
                case 5: ifAuth(LostFoundJavaApp::updateStatus); break;
                case 6: ifAuth(LostFoundJavaApp::deleteReport); break;
                case 7: System.exit(0);
            }
        }
    }

    static void register() {
        System.out.print("Enter username: ");
        String user = sc.nextLine();
        System.out.print("Enter password: ");
        String pass = sc.nextLine();
        users.insertOne(new Document("username", user).append("password", pass));
        System.out.println("Registered successfully.");
    }

    static void login() {
        System.out.print("Username: ");
        String user = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();
        Document doc = users.find(Filters.and(Filters.eq("username", user), Filters.eq("password", pass))).first();
        if (doc != null) {
            loggedInUserId = doc.getObjectId("_id").toHexString();
            System.out.println("Login successful.");
        } else {
            System.out.println("Invalid credentials.");
        }
    }

    static void reportItem() {
        System.out.print("Item name: ");
        String item = sc.nextLine();
        System.out.print("Status (Lost/Found): ");
        String status = sc.nextLine();
        System.out.print("Location: ");
        String loc = sc.nextLine();
        System.out.print("Description: ");
        String desc = sc.nextLine();
        System.out.print("Date (YYYY-MM-DD): ");
        String date = sc.nextLine();
        reports.insertOne(new Document("itemName", item)
                .append("status", status)
                .append("location", loc)
                .append("description", desc)
                .append("date", date)
                .append("reportedBy", loggedInUserId));
        System.out.println("Item reported.");
    }

    static void viewReports() {
        for (Document doc : reports.find(Filters.eq("reportedBy", loggedInUserId))) {
            System.out.println(doc.toJson());
        }
    }

    static void updateStatus() {
        System.out.print("Enter Report ID to update: ");
        String id = sc.nextLine();
        System.out.print("New status (Lost/Found/Claimed): ");
        String status = sc.nextLine();
        reports.updateOne(Filters.and(Filters.eq("_id", new org.bson.types.ObjectId(id)), Filters.eq("reportedBy", loggedInUserId)),
                new Document("$set", new Document("status", status)));
        System.out.println("Status updated.");
    }

    static void deleteReport() {
        System.out.print("Enter Report ID to delete: ");
        String id = sc.nextLine();
        reports.deleteOne(Filters.and(Filters.eq("_id", new org.bson.types.ObjectId(id)), Filters.eq("reportedBy", loggedInUserId)));
        System.out.println("Report deleted.");
    }

    static void ifAuth(Runnable action) {
        if (loggedInUserId == null) {
            System.out.println("Please login first.");
        } else {
            action.run();
        }
    }
}
