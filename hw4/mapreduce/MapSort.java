package wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class MapSort extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        // TimeStamp,SID,
        String      line = value.toString();
        String[]    lineSplit = line.split("\t");
        String      userId = lineSplit[0];
        String[]    events = lineSplit[1].split(" ");

		String 	    pageId = "";
		String 	    purchaseFlag = "";
        // each event here is in the form of TimeStamp,SID,PurchaseFlag
        // we want to take each event ant cut from it the time stamp
        String      fullStr = "";
        for (String event : events) {
            String[] eventSplit = event.split(",");
            pageId = eventSplit[1];
            purchaseFlag = eventSplit[2];
            fullStr = fullStr + pageId + "," + purchaseFlag + " ";
        }

        context.write(new Text(userId), new Text(fullStr));

    }
}
