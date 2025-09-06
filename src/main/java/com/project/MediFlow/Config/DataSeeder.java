package com.project.MediFlow.Config;

import com.project.MediFlow.Repository.FieldAliasMappingRepository;
import lombok.Data;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


// this class is to act as a dictionary for headers to be compared to so they can be transformed to fixed headers of correct respective entity

@Component
public class DataSeeder implements CommandLineRunner {

    private FieldAliasMappingRepository fieldAliasMappingRepository;
    public DataSeeder(FieldAliasMappingRepository fieldAliasMappingRepository){
        this.fieldAliasMappingRepository=fieldAliasMappingRepository;

    }
    @Override
    public void run(String... args) throws Exception {
                // this class always runs on startup so we have to make sure it does not  replicate data
        if()

}
