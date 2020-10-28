package com.ms.WebPlatform.utility;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper; 

@Converter
public class JsonToMapConverter  implements AttributeConverter<String, Map<String, Object>> {
	
	
	    //private static final Logger LOGGER = LoggerFactory.getLogger(JsonToMapConverter.class);
	//Logger log = (Logger) LoggerFactory.getLogger(JsonToMapConverter.class);

		    @Override
	    @SuppressWarnings("unchecked")
	    public Map<String, Object> convertToDatabaseColumn(String attribute)
	    {
	        if (attribute == null) {
	           return new HashMap<>();
	        }
	        try
	        {
	            ObjectMapper objectMapper = new ObjectMapper();
	            return objectMapper.readValue(attribute, HashMap.class);
	        }
	        catch (IOException e) {
	        	System.err.println("Convert error while trying to convert string(JSON) to map data structure.");
	        	//log.error();
	        }
	        return new HashMap<>();
	    }

	    @Override
	    public String convertToEntityAttribute(Map<String, Object> dbData)
	    {
	        try
	        {
	            ObjectMapper objectMapper = new ObjectMapper();
	            return objectMapper.writeValueAsString(dbData);
	        }
	        catch (JsonProcessingException e)
	        {
	          //  LOGGER.error("Could not convert map to json string.");
	            System.err.println("Could not convert map to json string.");
	            return null;
	        }
	    }
	}
	


