package com.mycompany.mavenproject6;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.Scanner;
public class Mavenproject6 {

static final int MAX_BOOKS = 20;
static String[] catalogTitles = new String[MAX_BOOKS];
static String[] catalogAuthorsOrPrices = new String[MAX_BOOKS];
static String[] catalogUrls = new String[MAX_BOOKS];
static int catalogCount = 0;

  static String[] readingTitles = new String[MAX_BOOKS];
  static String[] readingAuthorsOrPrices = new String[MAX_BOOKS];
  static String[] readingUrls = new String[MAX_BOOKS];
    static int readingCount = 0;

    static Scanner scanner = new Scanner(System.in);
    static final String BASE_URL = "https://books.toscrape.com/";

    public static void main(String[] args) {
        int choice = -1;

        do {
System.out.println("\n***** BOOKS TO SCRAPE EXPLORER *****");
System.out.println("1. Explore Books");
System.out.println("2. Search by Title");
System.out.println("3. Search by Price/Info (Author slot)");
System.out.println("4. Add Book to Reading List");
  System.out.println("5. View Reading List");
 System.out.println("6. Remove Book");
 System.out.println("7. Display Book Details");
 System.out.println("0. Exit");
System.out.print("Enter your choice: ");

if (scanner.hasNextInt()) {
 choice = scanner.nextInt();
scanner.nextLine();
} else {
    System.out.println("Invalid input Please enter the number.");
scanner.nextLine();
continue;
  }

 switch (choice) {
 case 1:
 exploreBooks();
  break;
 case 2:
 System.out.print("Enter book title keyword: ");
 String titleKeyword = scanner.nextLine();
 searchBook(titleKeyword);
 break;
case 3:
System.out.print("Enter price or info keyword (e.g. £51): ");
   String authorKeyword = scanner.nextLine();
  searchBook(authorKeyword, true);
                    break;
case 4:
addBook();
break;
case 5:
viewReadingList();
break;
case 6:
removeBook();
break;
 case 7:
 displayBookDetails();
break;
case 0:
System.out.println("Exiting application. Goodbye!");
 break;
 default:
 System.out.println("Invalid choice. Please select between 0 and 7.");
 }
} while (choice != 0);
}
static void exploreBooks() {
        System.out.println("Connecting to " + BASE_URL + " ...");
        try {
            Document doc = Jsoup.connect(BASE_URL).get();
            Elements books = doc.select("article.product_pod");

            catalogCount = 0;
            for (Element book : books) {
                if (catalogCount >= MAX_BOOKS) break;

                Element linkEl = book.selectFirst("h3 > a");
                Element priceEl = book.selectFirst("p.price_color");

                String title = (linkEl != null) ? linkEl.attr("title") : "Unknown Title";
                String url = (linkEl != null) ? linkEl.absUrl("href") : "";
                String price = (priceEl != null) ? priceEl.text() : "N/A";

 catalogTitles[catalogCount] = title;
 catalogAuthorsOrPrices[catalogCount] = price;
catalogUrls[catalogCount] = url;
 catalogCount++;
            }

System.out.println("\n--- Retrieved " + catalogCount + " Books ---");
for (int i = 0; i < catalogCount; i++) {
 System.out.println((i + 1) + ". " + catalogTitles[i] + " | Price: " + catalogAuthorsOrPrices[i]);
            }

} catch (Exception e) {
           System.out.println("Connection error: " + e.getMessage());
        }
    }

    static void searchBook(String title) {
        searchBook(title, false);
    }

    static void searchBook(String keyword, boolean searchByAuthor) {
        if (catalogCount == 0) {
            System.out.println("Catalog is empty! Please run option 1 (Explore Books) first.");
            return;
        }

        boolean found = false;
        String searchLower = keyword.toLowerCase().trim();
        System.out.println("\n--- Search Results ---");

        for (int i = 0; i < catalogCount; i++) {
            String target = searchByAuthor ? catalogAuthorsOrPrices[i] : catalogTitles[i];
            if (target.toLowerCase().contains(searchLower)) {
                System.out.println((i + 1) + ". " + catalogTitles[i] + " (" + catalogAuthorsOrPrices[i] + ")");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No books matched your search: \"" + keyword + "\"");
        }
    }

    static void addBook() {
        if (catalogCount == 0) {
            System.out.println("No books loaded yet. Run 'Explore Books' (Option 1) first.");
            return;
        }
if (readingCount >= MAX_BOOKS) {
System.out.println("Reading list is full! (Max " + MAX_BOOKS + " books).");
return;
}

System.out.print("Enter the book number from the explored list (1 to " + catalogCount + "): ");
if (!scanner.hasNextInt()) {
System.out.println("Invalid input.");
scanner.nextLine();
return;
}
int index = scanner.nextInt() - 1;
scanner.nextLine();

        if (index < 0 || index >= catalogCount) {
            System.out.println("Invalid book number.");
            return;
        }
readingTitles[readingCount] = catalogTitles[index];
readingAuthorsOrPrices[readingCount] = catalogAuthorsOrPrices[index];
readingUrls[readingCount] = catalogUrls[index];
readingCount++;

System.out.println("Added \"" + catalogTitles[index] + "\" to your reading list.");
}
static void viewReadingList() {
if (readingCount == 0) {
System.out.println("Your reading list is currently empty.");
return;
}
System.out.println("\n--- Your Personal Reading List (" + readingCount + "/" + MAX_BOOKS + ") ---");
for (int i = 0; i < readingCount; i++) {
System.out.println((i + 1) + ". " + readingTitles[i] + " | Price: " + readingAuthorsOrPrices[i]);
 }
    }

static void removeBook() {
if (readingCount == 0) {
System.out.println("Your reading list is empty. Nothing to remove.");
return;
}
viewReadingList();
System.out.print("Enter the number of the book to remove (1 to " + readingCount + "): ");
if (!scanner.hasNextInt()) {
System.out.println("Invalid input.");
scanner.nextLine();
return;
}
int index = scanner.nextInt() - 1;
scanner.nextLine();

if (index < 0 || index >= readingCount) {
System.out.println("Invalid book number.");
 return;
        }
String removedTitle = readingTitles[index];
for (int i = index; i < readingCount - 1; i++) {
readingTitles[i] = readingTitles[i + 1];
readingAuthorsOrPrices[i] = readingAuthorsOrPrices[i + 1];
readingUrls[i] = readingUrls[i + 1];
        }
readingCount--;
System.out.println("Removed \"" + removedTitle + "\" from your reading list.");
    }
static void displayBookDetails() {
if (catalogCount == 0) {
System.out.println("No books loaded yet. Run 'Explore Books' (Option 1) first.");
return;
        }

System.out.print("Enter book number from explored list (1 to " + catalogCount + "): ");
        if (!scanner.hasNextInt()) {
            System.out.println("Invalid input.");
            scanner.nextLine();
            return;
        }
int index = scanner.nextInt() - 1;
scanner.nextLine();
        if (index < 0 || index >= catalogCount) {
System.out.println("Invalid book number.");
return;
}
System.out.println("\n--- Book Details ---");
System.out.println("Title      : " + catalogTitles[index]);
System.out.println("Price/Info : " + catalogAuthorsOrPrices[index]);
System.out.println("URL        : " + catalogUrls[index]);
    }
}
