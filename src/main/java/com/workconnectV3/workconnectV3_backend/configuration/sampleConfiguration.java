// package com.workconnectV3.workconnectV3_backend.configuration;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// @Configuration
// public class sampleConfiguration implements WebMvcConfigurer {

//     @Override
//     public void addCorsMappings(CorsRegistry registry) {
//         // Allow all origins to access the API
//         registry.addMapping("/api/**")  // Apply to all /api/** endpoints
//                 .allowedOrigins("*")  // Specify allowed origins (e.g., localhost or a domain)
//                 .allowedMethods("GET", "POST", "PUT", "DELETE")  // Allowed HTTP methods
//                 .allowedHeaders("*")  // Allow all headers
//                 .allowCredentials(true);  // Allow credentials like cookies or HTTP authentication
//     }
// }
