package com.school.poderossa.bean;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PostBean {
    String postId;
    String name;
    String title;
    String content;
    Boolean status; // (si esta publicado 1, sino 0 ),,,,
    Integer rango; // rango del que postea 0 es el rango mayor
    Boolean pinned; // si es true aparecera en el top de la lista de posts
    String postPhoto;
    String writerPhoto;
    String publishFecha;
    String updateFecha;
    String updateTime;
}
