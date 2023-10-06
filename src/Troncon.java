public class Troncon {
    private Ligne ligne;
    private Station stationDepart;
    private Station stationArrivee;
    private int duree;

    public Troncon(Ligne ligne, Station stationDepart, Station stationFin, int duree) {
        this.ligne = ligne;
        this.stationDepart = stationDepart;
        this.stationArrivee = stationFin;
        this.duree = duree;
    }

    public Station getStationDepart() {
        return stationDepart;
    }

    public Station getStationArrivee() {
        return stationArrivee;
    }

    public int getDuree() {
        return duree;
    }

    public Ligne getLigne() {
        return ligne;
    }

    public void setLigne(Ligne ligne) {
        this.ligne = ligne;
    }

    public void setStationDepart(Station stationDepart) {
        this.stationDepart = stationDepart;
    }

    public void setStationArrivee(Station stationArrivee) {
        this.stationArrivee = stationArrivee;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    @Override
    public String toString() {
        return "Troncon[" +  "Depart=" + stationDepart + ", Arrivee=" + stationArrivee + ", duree=" + duree + ", ligne=["+ ligne + "]" + ']';
    }
}
