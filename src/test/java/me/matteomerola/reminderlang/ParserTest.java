package me.matteomerola.reminderlang;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import me.matteomerola.reminderlang.Lexer.Token;
import me.matteomerola.reminderlang.exceptions.ParseException;
import me.matteomerola.reminderlang.model.Reminder;
import me.matteomerola.reminderlang.model.Repeat;

/**
 * Unit test for simple App.
 */
public class ParserTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ParserTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(ParserTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testParser() throws ParseException, IOException {
        List<List<String>> grid = this.readCsv("test.csv");
        for (List<String> row : grid) {
            String cmd = row.get(0);
            List<Token> tokens = Lexer.lex(cmd);
            Parser parser = new Parser(cmd, tokens);
            Reminder reminder = parser.parse();
            String msg = row.get(1);
            assertEquals(msg, reminder.getText());
            long when = Long.parseLong(row.get(2));
            assertEquals(new Long(when), new Long(reminder.getWhen()));
            if (!row.get(3).equalsIgnoreCase("null")) {
                Repeat repeat = reminder.getRepeat();
                assertNotNull(repeat);
                long every = Long.parseLong(row.get(3));
                assertEquals(new Long(every), new Long(repeat.getEvery()));
                if (!row.get(4).equalsIgnoreCase("null")) {
                    long until = Long.parseLong(row.get(4));
                    assertEquals(new Long(until), new Long(repeat.getUntil()));
                }
            }
        }
    }

    private List<List<String>> readCsv(String filename) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        String line = "";
        String cvsSplitBy = ";";
        List<List<String>> grid = new ArrayList<List<String>>();
        BufferedReader br = new BufferedReader(new FileReader(classLoader.getResource(filename).getFile()));
        while ((line = br.readLine()) != null) {
            List<String> row = new ArrayList<String>();
            // use comma as separator
            String[] bits = line.split(cvsSplitBy);
            for (String bit : bits) {
                row.add(bit);
            }
            grid.add(row);
        }
        br.close();
        return grid.subList(1, grid.size());
    }
}
