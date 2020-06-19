package wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class ReduceSort extends Reducer<Text, Text, Text, Text> {

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {
        for (Text value : values) {
            String line = value.toString();
            String[] eventsList = line.split(" ");

            if (eventsList.length > 3) {
                for(int i = 0; i<eventsList.length-2; i++) {
                    context.write(new Text("("+eventsList[i]+"),"+"("+eventsList[i+1]+"),"+"("+eventsList[i+2]+")"), key);
                }
            }
            else {
                if (eventsList.length == 2) {
                    String[] eventSplit = eventsList[1].split(",");
                    eventSplit[1] = "0";
                    context.write(new Text("("+eventsList[0]+"),"+"("+eventsList[1]+")"+"("+eventSplit[0]+","+eventSplit[1]+")"), key);
                } else if (eventsList.length == 1) {
                    String[] eventSplit = eventsList[0].split(",");
                    eventSplit[1] = "0";
                    context.write(new Text("("+eventsList[0]+"),"+"("+eventSplit[0]+","+eventSplit[1]+"),"+"("+eventSplit[0]+","+eventSplit[1]+")"), key);

                } else {
                    context.write(new Text("("+eventsList[0]+"),"+"("+eventsList[1]+"),"+"("+eventsList[2]+")"), key);
                }
            }
        }
    }
}

