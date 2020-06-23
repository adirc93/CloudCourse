package wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class map4 extends Mapper<LongWritable, Text, LongWritable, Text> {

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString();
        String[]    lineSplited = line.split("\t");
        String      event = lineSplited[0];
        long        amount = Long.parseLong(lineSplited[1]);
        amount = amount*(-1);
        context.write(new LongWritable(amount), new Text(event));
    }
}