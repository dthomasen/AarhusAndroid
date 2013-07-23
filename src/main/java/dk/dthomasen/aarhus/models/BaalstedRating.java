package dk.dthomasen.aarhus.models;

public class BaalstedRating {

    private String baalsted_id;
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

    public String getBaalsted_id() {
        return baalsted_id;
    }

    public void setBaalsted_id(String baalsted_id) {
        this.baalsted_id = baalsted_id;
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
                "baalsted_id='" + baalsted_id + '\'' +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", rating='" + rating + '\'' +
                ", dato='" + dato + '\'' +
                '}';
    }
}