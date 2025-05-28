//package com.example.cook.report.logging;
//
//import com.fasterxml.jackson.databind.AnnotationIntrospector;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.Builder;
//import lombok.Data;
//import lombok.SneakyThrows;
//import net.logstash.logback.argument.StructuredArgument;
//import net.logstash.logback.marker.MapEntriesAppendingMarker;
//import com.example.cook.report.annotations.HashAnnotationIntrospector;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.StringJoiner;
//
//
//@Data
//@Builder
//public class ComplexLog {
//
//    private static final ObjectMapper objectMapper;
//    static{
//        objectMapper = new ObjectMapper();
//        objectMapper.setAnnotationIntrospector(AnnotationIntrospector.pair(
//                new HashAnnotationIntrospector(),
//                objectMapper.getSerializationConfig().getAnnotationIntrospector()
//        ));
//    }
//
//    private String key;
//    private String logType;
//    private String stage;
//    private Object data;
//
//    public StructuredArgument toMap(){
//        Map myMap = new HashMap();
//        myMap.put("key", key);
//        myMap.put("logType", logType);
//        myMap.put("stage", stage);
//        if(null != data){
//            try {
//                myMap.put("data", objectMapper.writeValueAsString(data));
//            } catch (Exception e){
//                e.printStackTrace();
//            }
//
//        }
//
//        return new MapEntriesAppendingMarker(myMap);
//    }
//
//    @SneakyThrows
//    @Override
//    public String toString() {
//        StringJoiner stringJoiner =  new StringJoiner(",")
//                .add("\"key\":\"" + key + "\"")
//                .add("\"logType\":\"" + logType + "\"")
//                .add("\"stage\":\"" + stage + "\"")
//                ;
//        if(null != data){
//            stringJoiner.add("\"data\": " + objectMapper.writeValueAsString(data));
//        }
//        return stringJoiner.toString();
//    }
//}
