//package com.example.cook.report.annotations;
//
//import com.fasterxml.jackson.databind.introspect.Annotated;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class HashAnnotationIntrospector {
//    private static final long serialVersionUID = 1L;
//
//    @Override
//    public Object findSerializer(Annotated am) {
//        Hash hashAnnotation = am.getAnnotation(Hash.class);
//        if (hashAnnotation != null) {
//            return HashJsonSerialization.class;
//        }
//        return null;
//    }
//}
