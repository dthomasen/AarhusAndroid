package dk.dthomasen.aarhus.models;

public class Fitness {

    String navn = "";
    String billede1 = "";
    String billede2 = "";
    String billede3 = "";
    String billede4 = "";
    String beskrivelse = "";
    String praktisk = "";

    @Override
    public String toString() {
        return "Baalplads{" +
                "navn='" + navn + '\'' +
                ", billede1='" + billede1 + '\'' +
                ", billede2='" + billede2 + '\'' +
                ", billede3='" + billede3 + '\'' +
                ", billede4='" + billede4 + '\'' +
                ", beskrivelse='" + beskrivelse + '\'' +
                ", praktisk='" + praktisk + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }

    String latitude;
    String longitude;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getPraktisk() {
        return praktisk;
    }

    public void setPraktisk(String praktisk) {
        this.praktisk = praktisk;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getBillede1() {
        return billede1;
    }

    public void setBillede1(String billede1) {
        this.billede1 = billede1;
    }

    public String getBillede2() {
        return billede2;
    }

    public void setBillede2(String billede2) {
        this.billede2 = billede2;
    }

    public String getBillede3() {
        return billede3;
    }

    public void setBillede3(String billede3) {
        this.billede3 = billede3;
    }

    public String getBillede4() {
        return billede4;
    }

    public void setBillede4(String billede4) {
        this.billede4 = billede4;
    }
}