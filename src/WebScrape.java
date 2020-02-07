import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import java.util.Scanner;

public class WebScrape {
    public static void main(String[] args) {
        String keyWord;
        Scanner input = new Scanner(System.in);
        System.out.println("What product would you like to query on Qoo10?");
        keyWord = input.nextLine();
        String priceRange;
        System.out.println("Price range:");
        priceRange = input.nextLine();
        double lower, higher;
        if (priceRange.contains("$")) {
            priceRange = priceRange.replace("$", "");
        }
        if (priceRange.contains("-")) {
            lower = Double.parseDouble(priceRange.substring(0, priceRange.indexOf("-")));
            higher = Double.parseDouble(priceRange.substring(priceRange.indexOf("-")+1));
        } else {
            lower = 0;
            higher = Double.parseDouble(priceRange);
        }
        final String url = "https://www.qoo10.sg/s/?keyword=" + keyWord + "&keyword_auto_change=";

        try {
            int count = 0;
            final Document document = Jsoup.connect(url).get();
            for (Element row : document.select(
                    "tbody#search_result_item_list tr"
            )) {
                final String name =
                        row.select("div.sbj").text();
                final String price =
                        row.select("td:nth-child(3) > div:nth-child(1) > strong:nth-child(1)").text();
                final String linkURL = row.select("a:nth-child(1)").attr("abs:href");
                double nominalisedPrice = Double.parseDouble(price.substring(price.indexOf("S$")+2));
                if (nominalisedPrice >= lower && nominalisedPrice <= higher) {
                    count++;
                    System.out.println(count + ": " + name + ": " + price);
                    System.out.println(linkURL + "\n");
                }
            }
            if (count == 0) {
                System.out.println("Sorry, no matching results.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}