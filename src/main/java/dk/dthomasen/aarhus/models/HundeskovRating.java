package dk.dthomasen.aarhus.models;

public class HundeskovRating {

    private String hundeskov_id;
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

    public String getHundeskov_id() {
        return hundeskov_id;
    }

    public void setHundeskov_id(String hundeskov_id) {
        this.hundeskov_id = hundeskov_id;
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
        return "HundeskovRating{" +
                "hundeskov_id='" + hundeskov_id + '\'' +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", rating='" + rating + '\'' +
                ", dato='" + dato + '\'' +
                '}';
    }
}