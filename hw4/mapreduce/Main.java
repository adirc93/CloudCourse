package wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.util.Random;

public class Main extends Configured implements Tool {

    // ------------- DRIVER ------------
    @Override
    public int run(String[] args) throws Exception {
        Path tempDir = new Path("data/temp-" + Integer.toString(new Random().nextInt(Integer.MAX_VALUE)));
        Path tempDir2 = new Path("data/temp2-" + Integer.toString(new Random().nextInt(Integer.MAX_VALUE)));
        Configuration conf = getConf();
        FileSystem.get(conf).delete(new Path("data/output"), true);

        try {
// ----------- FIRST JOB -----------
            System.out.println("-------FIRST JOB-------");
            Job wordCountJob = Job.getInstance(conf);
            wordCountJob.setJobName("wordcount");
            wordCountJob.setJarByClass(Main.class);

            wordCountJob.setMapOutputValueClass(Text.class);
            wordCountJob.setOutputKeyClass(Text.class);
            wordCountJob.setOutputValueClass(Text.class);

            wordCountJob.setMapperClass(Map.class);
            wordCountJob.setReducerClass(Reduce.class);

            Path inputFilePath = new Path("data/input/");
            Path outputFilePath = tempDir;

            FileInputFormat.addInputPath(wordCountJob, inputFilePath);
            FileOutputFormat.setOutputPath(wordCountJob, outputFilePath);

            if (!wordCountJob.waitForCompletion(true)) {
                return 1;
            }

// ----------- SECOND JOB -----------
            System.out.println("-------SECOND JOB-------");
            Job filterJob = Job.getInstance(conf);
            filterJob.setJobName("Filter");

            FileInputFormat.setInputPaths(filterJob, tempDir);
            FileOutputFormat.setOutputPath(filterJob,tempDir2);
         //   FileOutputFormat.setOutputPath(filterJob, new Path("data/output"));

            filterJob.setMapOutputValueClass(Text.class);
            filterJob.setOutputKeyClass(Text.class);
            filterJob.setOutputValueClass(Text.class);

            filterJob.setMapperClass(MapSort.class);
            filterJob.setReducerClass(ReduceSort.class);

           // Path inputFilePath2 = new Path("data/input/");
         //   Path outputFilePath2 = tempDir2;


            if (!filterJob.waitForCompletion(true)) {
                return 1;
            }

            // ----------- Third JOB -----------

            System.out.println("-------Third JOB-------");
            conf = new Configuration();
            Job countJob = Job.getInstance(conf);
            countJob.setJobName("count");

            countJob.setJarByClass(Main.class);
            FileInputFormat.setInputPaths(countJob, tempDir2);
            FileOutputFormat.setOutputPath(countJob, new Path("data/output/"));

            countJob.setMapOutputValueClass(IntWritable.class);
            countJob.setOutputKeyClass(Text.class);
            countJob.setOutputValueClass(Text.class);

            countJob.setMapperClass(map3.class);
            countJob.setReducerClass(reduce3.class);

            return countJob.waitForCompletion(false) ? 0 : 1;

        } finally {
            FileSystem.get(conf).delete(tempDir2, true);
            FileSystem.get(conf).delete(tempDir, true);
        }
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new Main(), args);
        System.exit(exitCode);
    }
}
