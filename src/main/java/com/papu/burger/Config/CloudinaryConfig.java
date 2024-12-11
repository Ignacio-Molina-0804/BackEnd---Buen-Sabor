package com.papu.burger.Config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = ObjectUtils.asMap(
                "cloud_name", "dwa9ptfoa",
                "api_key", "362886161316371",
                "api_secret", "9uCff30H9GcORXthQPw4aU8Ctms",
                "secure", true
        );
        return new Cloudinary(config);
    }
}

