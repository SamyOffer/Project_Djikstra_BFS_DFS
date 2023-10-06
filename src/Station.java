import java.util.Objects;

public class Station {

    private final String nomStation;
    private int tempsPourArriver;

    public Station(String nomStation) {
        this.nomStation = nomStation;
        this.tempsPourArriver = 0;
    }

    public Station(String nomStation, int tempsPourArriver) {
        this.nomStation = nomStation;
        this.tempsPourArriver = tempsPourArriver;
    }

    public String getNomStation() {
        return nomStation;
    }

    public int getTempsPourArriver() {
        return tempsPourArriver;
    }

    public void setTempsPourArriver(int tempsPourArriver) {
        this.tempsPourArriver = tempsPourArriver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(nomStation, station.nomStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nomStation);
    }

    @Override
    public String toString() {
        return nomStation;
    }
}
