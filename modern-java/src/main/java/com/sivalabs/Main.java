package com.sivalabs;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        // Text blocks
        String sql = """
            SELECT * FROM users
            WHERE id = 1
            """;
        System.out.println("sql: "+ sql);

        String bio = """
            My name is %s
            I am a %s
            I am from %s
            """.formatted("Siva", "Software Developer", "India");
        System.out.println(bio);


        // List.of() factory method, type inference with var
        var skills = List.of("Java", "Spring", "Hibernate", "Angular");
        System.out.println(skills);

        // Record to JSON and JSON to Record
        ObjectMapper objectMapper = new ObjectMapper();
        var person = new PersonRecord(1L, "Siva", "siva@gmail.com");
        var json = objectMapper.writeValueAsString(person);
        System.out.println("json = " + json);

        PersonRecord personRecord = objectMapper.readValue(json, PersonRecord.class);
        System.out.println("personRecord = " + personRecord);
    }
}