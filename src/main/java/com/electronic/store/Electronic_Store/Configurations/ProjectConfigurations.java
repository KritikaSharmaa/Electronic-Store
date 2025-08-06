package com.electronic.store.Electronic_Store.Configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfigurations{
    @Bean
    public ModelMapper mapper(){
        return new ModelMapper();
    }
}
