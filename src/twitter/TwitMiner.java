package twitter;

import java.util.ArrayList;
import java.util.List;

import csv.CSVFileWriter;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

public class TwitMiner {
	static Twitter twitter;

	public static void sleep(int duration) throws InterruptedException {
		for (int i = 0; i < duration; ++i) {
			Thread.sleep(1000);
			System.out.print(".");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		try {
			twitter       = new TwitterFactory().getInstance();
			Search search = new Search(twitter, "pokemon");
			List<List<String>> tweets = new ArrayList<List<String>>();

			CSVFileWriter csv = new CSVFileWriter(";");
			if (!csv.open("pokemon.csv")) {
				return;
			}

			int limit  = 10000;
			int number = 0;
			tweets  = search.getAfter();
    		for (List<String> status : tweets) {
    			csv.writeSet(status);
    			++number;
    		}

			while (true) {
				if (number >= limit) {
					break;
				}

	    		// Sleep for 10 seconds = 90 requests per 15 minutes
	    		System.out.println(number + "/" + limit);
	    		sleep(10);

				tweets = search.getBefore();
	    		for (List<String> status : tweets) {
	    			csv.writeSet(status);
	    			++number;
	    		}

			}
			csv.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
