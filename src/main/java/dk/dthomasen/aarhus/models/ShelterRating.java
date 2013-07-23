package dk.dthomasen.aarhus.models;

public class ShelterRating {

    private String shelter_id;
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

    public String getShelter_id() {
        return shelter_id;
    }

    public void setShelter_id(String shelter_id) {
        this.shelter_id = shelter_id;
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
        return "ShelterRating{" +
                "shelter_id='" + shelter_id + '\'' +
                ", name='" + name + '\'' +
                ", comment='" + comment + '\'' +
                ", rating='" + rating + '\'' +
                ", dato='" + dato + '\'' +
                '}';
    }
}