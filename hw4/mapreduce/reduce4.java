package wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class reduce4 extends Reducer<LongWritable, Text, Text, LongWritable> {

    @Override
    public void reduce(LongWritable key, Iterable<Text> events, Context context)
            throws IOException, InterruptedException {
        long key2 = key.get();
        key2 = key2*(-1);
        for (Text text : events) {
            context.write(new Text(text), new LongWritable(key2));
        }
        //context.write(key, new Text("FINISH COUNT"));

    }
}