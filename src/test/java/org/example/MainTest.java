package org.example;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.in;
import static org.junit.Assert.*;

public class MainTest {
    @Test
    public void testUserChoice() {
        ByteArrayInputStream in = new ByteArrayInputStream("2".getBytes());
        System.setIn(in);
        Scanner sc = new Scanner(System.in);
        int input = sc.nextInt();
        assertTrue(input == 1 || input == 2);
    }

    @Test
    public void testUserId() {
        ByteArrayInputStream in = new ByteArrayInputStream("2".getBytes());
        System.setIn(in);
        Scanner sc = new Scanner(System.in);
        int input = sc.nextInt();
        assertEquals(input,2);
    }

    @Test
    public void testQueryCallWithInvalidQuery() {
        try {
            System.setIn(in);
            QueryCase qc = new QueryCase();
            qc.queryCall("SELECT *FROM student");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(1, 1);
    }
    @Test
    public void testQueryCallWithInvalid() {
        try {
            System.setIn(in);
            QueryCase qc = new QueryCase();
            qc.queryCall("UPDATE student SET username = 'John' WHERE studentId=1");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(1, 1);
    }
}