package dk.dthomasen.aarhus.models;

public class FitnessRating {

    private String fitness_id;
    private String name;
    private String comment;
    private String rating;
    private String dato;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFitness_id() {
        return fitness_id;
    }

    public void setFitness_id(String fitness_id) {
        this.fitness_id = fitness_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    @Override
    public String toString() {
        return "fitnessRating{" +
                "fitness_id='" + fitness_id + '\'' +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", rating='" + rating + '\'' +
                ", dato='" + dato + '\'' +
                '}';
    }
}