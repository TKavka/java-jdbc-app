package com.botscrew.university;

public enum Degree {

    ASSISTANT ("assistant"),
    ASSOCIATE_PROFESSOR("associate professor"),
    PROFESSOR("professor");

    private String degree;

    Degree(String degree) {
        this.degree = degree;
    }

    public String getDegree() {
        return degree;
    }

    @Override
    public String toString() {
        return "Degree{" +
                "degree='" + degree + '\'' +
                '}';
    }
}
