package domein;

import java.sql.Date;

public class OVChipkaart {
    int kaart_id;
    Date einddatum;
    int klasse;
    double saldo;
    Reiziger reiziger;

    public OVChipkaart(int kaart_id, Date einddatum, int klasse,
                       double saldo, Reiziger reiziger){
        this.kaart_id = kaart_id;
        this.einddatum = einddatum;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
    }

    public int getKaart_id() {
        return kaart_id;
    }

    public void setKaart_id(int kaart_id) {
        this.kaart_id = kaart_id;
    }

    public Date getEinddatum() {
        return einddatum;
    }

    public void setEinddatum(Date einddatum) {
        this.einddatum = einddatum;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public int getReiziger_ID() {
        return reiziger.getId();
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    @Override
    public String toString() {
        return "OVChipkaart{" +
                "kaart_id=" + kaart_id +
                ", einddatum=" + einddatum +
                ", klasse=" + klasse +
                ", saldo=" + saldo +
                ", reiziger=" + reiziger.getVoorletters() +
                " " + reiziger.getTussenvoegsel() +
                " " + reiziger.getAchternaam() +
                '}';
    }
}
