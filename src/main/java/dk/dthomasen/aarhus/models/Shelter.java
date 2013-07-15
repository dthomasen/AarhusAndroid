package dk.dthomasen.aarhus.models;

public class Shelter {

    String navn = "";
    String billede1 = "";
    String billede2 = "";
    String billede3 = "";
    String billede4 = "";
    String billede5 = "";

    @Override
    public String toString() {
        return "Shelter{" +
                "navn='" + navn + '\'' +
                ", billede1='" + billede1 + '\'' +
                ", billede2='" + billede2 + '\'' +
                ", billede3='" + billede3 + '\'' +
                ", billede4='" + billede4 + '\'' +
                ", billede5='" + billede5 + '\'' +
                ", billede6='" + billede6 + '\'' +
                ", beskrivelse='" + beskrivelse + '\'' +
                ", praktisk='" + praktisk + '\'' +
                ", vejledning='" + vejledning + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }

    String billede6 = "";
    String beskrivelse = "";

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

    public String getBillede5() {
        return billede5;
    }

    public void setBillede5(String billede5) {
        this.billede5 = billede5;
    }

    public String getBillede6() {
        return billede6;
    }

    public void setBillede6(String billede6) {
        this.billede6 = billede6;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public String getPraktisk() {
        return praktisk;
    }

    public void setPraktisk(String praktisk) {
        this.praktisk = praktisk;
    }

    public String getVejledning() {
        return vejledning;
    }

    public void setVejledning(String vejledning) {
        this.vejledning = vejledning;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    String praktisk = "";
    String vejledning = "";
    String latitude;
    String longitude;
}