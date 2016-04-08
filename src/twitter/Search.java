package twitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class Search {
	Twitter twitter = null;
	String  query   = "";
	long    sinceId = 0l;
	long    maxId   = 0l;

	public Search(Twitter twitter, String query) {
		this.twitter = twitter;
		this.query   = query;
	}

	public List<String> formatStatus(Status status) {
    	List<String> format = new ArrayList<String>();

    	// Add the tweet's metadata
    	format.add(status.getCreatedAt().toString());
    	format.add(status.getLang());
    	format.add("@" + status.getUser().getScreenName());

    	// Add the tweet's content
    	List<String> words = Arrays.asList(status.getText().split(" "));
    	Collections.sort(words, String.CASE_INSENSITIVE_ORDER);
    	format.addAll(words);

    	return format;
	}

	private List<List<String>> process(Query search) {
	    List<List<String>> tweets  = new ArrayList<List<String>>();
    	QueryResult queryResult;

    	try {
    		queryResult = twitter.search(search);
    		List<Status> result = queryResult.getTweets();

    		if (result.size() > 0) {
    			/*
    			 * Update this.maxId and this.sinceId
    			 */
    			sinceId= result.get(0).getId();
    			maxId  = result.get(result.size()-1).getId();

    			/*
    			 * Add the result
    			 */
        		for (Status status : result) {
        			tweets.add(formatStatus(status));
        		}
    		}
		} catch (TwitterException te) {
			te.printStackTrace();
		}
		return tweets;
	}

	public List<List<String>> getBefore(long maxId) {
		this.maxId = maxId;
	    Query search  = new Query(query);
    	search.setCount(100);
   		search.setMaxId(maxId);
	    return process(search);
	}

	public List<List<String>> getAfter(long sinceId) {
		this.sinceId = sinceId;
	    Query search  = new Query(query);
    	search.setCount(100);
   		search.setSinceId(sinceId);
	    return process(search);
	}

	public List<List<String>> getBefore() {
		return getBefore(maxId);
	}

	public List<List<String>> getAfter() {
		return getAfter(sinceId);
	}
}
