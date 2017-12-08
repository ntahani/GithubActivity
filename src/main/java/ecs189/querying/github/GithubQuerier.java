package ecs189.querying.github;

import ecs189.querying.Util;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Vincent on 10/1/2017.
 */
public class GithubQuerier {

    private static final String BASE_URL = "https://api.github.com/users/";

    public static String eventsAsHTML(String user) {
        StringBuilder sb = new StringBuilder();

        try {

            List<JSONObject> response = getEvents(user);
            sb.append("<div>");
            for (int i = 0; i < response.size(); i++) {
                JSONObject event = response.get(i);
                // Get event type
                String type = event.getString("type");

                // Get created_at date, and format it in a more pleasant style
                String creationDate = event.getString("created_at");
                SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'");
                SimpleDateFormat outFormat = new SimpleDateFormat("dd MMM, yyyy");
                Date date = inFormat.parse(creationDate);
                String formatted = outFormat.format(date);

                // Add type of event as header
                sb.append("<h3 class=\"type\">");
                sb.append(type);
                sb.append("</h3>");
                // Add formatted date
                sb.append(" on ");
                sb.append(formatted);
                sb.append("<br />");

                JSONObject payload = event.getJSONObject("payload");
                JSONArray commits = payload.getJSONArray("commits");

                if (commits.length() > 0) {
                    // Add collapsible JSON textbox (don't worry about this for the homework; it's just a nice CSS thing I like)
                    sb.append("<a data-toggle=\"collapse\" href=\"#event-" + i + "\">Commit(s)</a>");
                    sb.append("<div id=event-" + i + " class=\"collapse\" style=\"height: auto;\">");
                    sb.append("<table>");
                    sb.append("<tr>");
                    sb.append("<th> SHA </th> <th> Message </th>");
                    sb.append("</tr>");
                    for (int j = 0; j < commits.length(); j++) {
                        sb.append("<tr>");
                        JSONObject js = commits.getJSONObject(j);
                        String sha = js.getString("sha");
                        String msg = js.getString("message");

                        sb.append("<td>" + sha.substring(0,7) + "</td><td>" + msg + "</td>");
                        sb.append("</tr>");
                    }
                    sb.append("</table>");
                    //sb.append(event.toString());
                    sb.append("</div>");
                } else {
                    sb.append("No commit for this event, yet.");
                }
            }
            sb.append("</div>");

        } catch (ParseException exp) {
            sb.append("Parsing exception. Try again.");
        }
        return sb.toString();
    }

    private static List<JSONObject> getEvents(String user) {
        List<JSONObject> eventList = new ArrayList<JSONObject>();

        try {
            String url = BASE_URL + user + "/events";

            JSONObject json = Util.queryAPI(new URL(url));
            System.out.println(json);
            JSONArray events = json.getJSONArray("root");
            //for (int i = 0; i < events.length() && i < 10; i++) {
            for (int i = 0; i < events.length(); i++) {
                JSONObject jsonObject = events.getJSONObject(i);
                if (jsonObject.getString("type").equalsIgnoreCase("PushEvent")) {
                    eventList.add(events.getJSONObject(i));
                }
            }
        } catch (IOException exp) {
            return null;
        }
        if (eventList.size() > 10) {
            return eventList.subList(0,9);
        }
        return eventList;
    }
}