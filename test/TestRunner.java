import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import osu.beatmap.BeatmapTestSuite;
import server.TestServerSuite;

public class TestRunner {
   public static void main(String[] args) {
      Result result = JUnitCore.runClasses(BeatmapTestSuite.class, TestServerSuite.class);

      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
		
      System.out.println("tests passed: " + result.wasSuccessful());
   }
} 