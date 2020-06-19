package wordcount;

import java.lang.String;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class map3 extends Mapper<LongWritable, Text, Text, IntWritable> {

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        //example:
        //(234,0);(6963,0);(5874,0)	24040
        String line = value.toString();
        String[] words = line.split(" ");
        String[] events = words[0].split(";");
        // events[i] = (xx,yy)
        String[] eventCut1 = events[1].split(",");
        String[] eventCut2 = events[2].split(",");
        ///eventCut = (ppid

        int len = events[2].length();
        if(events[2].charAt(len - 1) == '1') {
            int eCut1 = eventCut1[0].length()-1;
            int eCut2 = eventCut2[0].length()-1;
            context.write(new Text(eventCut1[0].substring(1,eCut1)+ ";"+eventCut2[0].substring(1,eCut2)), new IntWritable(1));
        }
    }
}
