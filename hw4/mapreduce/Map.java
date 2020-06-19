package wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class Map extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString();
        String[] words = line.split(",");
	//example:
	//line = 2020060318572.162482,88478,1947,0
	//key = 88478
	//value = 2020060318572.162482,1947,0;
        context.write(new Text(words[1]), new Text(words[0]+","+ words[2]+","+words[3]));
        //{time stamp,page id,buyFlag}
    }
}
