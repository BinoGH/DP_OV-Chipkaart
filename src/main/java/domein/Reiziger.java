package domein;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Reiziger{
    private int id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private Date geboortedatum;
    private Adres adres = null;
    private List<OVChipkaart> ovChipkaartList= new ArrayList<>();

    public Reiziger(int id, String voorletters, String tussenvoegsel,
                    String achternaam, Date geboortedatum){
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaam() {
        return (voorletters + tussenvoegsel + achternaam);
    }

    public String getVoorletters() {
        return voorletters;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(Date geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public List<OVChipkaart> getOvChipkaartList() {
        return ovChipkaartList;
    }

    public void setOvChipkaartList(List<OVChipkaart> ovChipkaartList) {
        this.ovChipkaartList = ovChipkaartList;
    }

    public void addOVChipkaart(OVChipkaart ovchip){
        ovChipkaartList.add(ovchip);
    }

    public String toString() {
        StringBuilder ovchipInfo = new StringBuilder();
        if (ovChipkaartList != null){
            for (OVChipkaart ovchip : ovChipkaartList){
                ovchipInfo.append("OVCHIP ").append(ovchip.getKaart_id()).append(" einddatum=")
                        .append(ovchip.getEinddatum()).append(", saldo=").append(ovchip.getSaldo());
        }}
        if (adres != null && ovChipkaartList != null) {
            return "Reiziger{" +
                    "id=" + id +
                    ", voorletters='" + voorletters + '\'' +
                    ", tussenvoegsel='" + tussenvoegsel + '\'' +
                    ", achternaam='" + achternaam + '\'' +
                    ", geboortedatum=" + geboortedatum +
                    ", Adres{" +
                    "id=" + id +
                    ", postcode='" + adres.getPostcode() + '\'' +
                    ", huisnummer='" + adres.getHuisnummer() + '\'' +
                    ", straat='" + adres.getStraat() + '\'' +
                    ", woonplaats='" + adres.getWoonplaats() + '\'' +
                    ovchipInfo +
                    "}}";
        }
        return "Reiziger{" +
                "id=" + id +
                ", voorletters='" + voorletters + '\'' +
                ", tussenvoegsel='" + tussenvoegsel + '\'' +
                ", achternaam='" + achternaam + '\'' +
                ", geboortedatum=" + geboortedatum +
                "}";
    }
}
