package com.example.drawMe;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DrawMeApplicationTests {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@After
	public void restoreStreams() {
		System.setOut(originalOut);
		System.setErr(originalErr);
	}

	@Test
	public void test() {
		try {
			URL url = Thread.currentThread().getContextClassLoader().getResource("testInput.txt");
			File file = new File(url.getPath());
			DrawMeApplication.setReader(new FileReader(file));
			DrawMeApplication.setTerminate(false);
			DrawMeApplication.main(null);

			File outputFile = new File("testOutput.txt");
			FileInputStream fis = new FileInputStream(outputFile);
			byte[] data = new byte[(int) outputFile.length()];
			fis.read(data);
			fis.close();

			String str = new String(data, "UTF-8");
			assertEquals(str, outContent.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testInvalidParams() {
		try {
			URL url = Thread.currentThread().getContextClassLoader().getResource("testInputInvalidParams.txt");
			File file = new File(url.getPath());
			DrawMeApplication.setReader(new FileReader(file));
			DrawMeApplication.setTerminate(false);
			DrawMeApplication.main(null);

			File outputFile = new File("testOutputInvalidParams.txt");
			FileInputStream fis = new FileInputStream(outputFile);
			byte[] data = new byte[(int) outputFile.length()];
			fis.read(data);
			fis.close();

			String str = new String(data, "UTF-8");
			assertEquals(str, outContent.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
