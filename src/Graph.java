import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Graph {

  private Map<Integer, Ligne> mapIdVersLigne;
  private Map<Station, Set<Troncon>> mapStationVersTroncons;
  private Map<String, Station> mapNomStationVerStation;

  private Scanner scannerTroncons, scannerLigne;


  public Graph(File lignes, File troncons) {
    this.mapIdVersLigne = new HashMap<>();
    this.mapStationVersTroncons = new HashMap<>();
    this.mapNomStationVerStation = new HashMap<>();
    try {
      scannerLigne = new Scanner(lignes);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
    try {
      scannerTroncons = new Scanner(troncons);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }

    while (scannerLigne.hasNext()) {
      creerLigne(scannerLigne.nextLine());
    }

    while (scannerTroncons.hasNext()) {
      creerTroncon(scannerTroncons.nextLine());
    }
  }

  private void creerLigne(String stringLigneACreer) {
    String[] datas = stringLigneACreer.split(",");
    int identifiantLigne = Integer.parseInt(datas[0]);
    String numeroLigne = datas[1];
    String stationDepart = datas[2];
    String stationArrivee = datas[3];
    String typeTransport = datas[4];
    int attenteMoyenne = Integer.parseInt(datas[5]);

    Ligne ligne = new Ligne(identifiantLigne, numeroLigne, stationDepart, stationArrivee, typeTransport, attenteMoyenne);
    mapIdVersLigne.put(identifiantLigne, ligne);

  }

  private void creerTroncon(String stringTronconACreer) {
    String[] datas = stringTronconACreer.split(",");
    int idLigne = Integer.parseInt(datas[0]);
    String nomStationDepart = datas[1];
    String nomStationArrivee = datas[2];
    int dureeTroncon = Integer.parseInt(datas[3]);

    Ligne ligne = mapIdVersLigne.get(idLigne);
    if(!mapNomStationVerStation.containsKey(nomStationDepart)) {
      creerStation(nomStationDepart);
    }

    Station stationDepart = mapNomStationVerStation.get(nomStationDepart);
    if(!mapNomStationVerStation.containsKey(nomStationArrivee)) {
      creerStation(nomStationArrivee);
    }
    Station stationArrivee = mapNomStationVerStation.get(nomStationArrivee);


    Troncon troncon = new Troncon(ligne, stationDepart, stationArrivee, dureeTroncon);
    if(!mapStationVersTroncons.containsKey(stationDepart)){
      mapStationVersTroncons.put(stationDepart, new HashSet<>());
    }
    mapStationVersTroncons.get(stationDepart).add(troncon);

  }

  private void creerStation(String nomStation) {
    Station station = new Station(nomStation);
    mapNomStationVerStation.put(nomStation, station);
  }

  public void calculerCheminMinimisantNombreTroncons(String nomStationDepart, String nomStationArrivee){
    ArrayDeque<Station> stationArrayDeque = new ArrayDeque<>(); // file
    Set<Station> stationsDejaParcourues = new HashSet<>(); // ensemble deja parcouru
    Station stationDepart = mapNomStationVerStation.get(nomStationDepart);
    Station stationFin = mapNomStationVerStation.get(nomStationArrivee);

   HashMap<Station, Troncon> parcoursDesStations = new HashMap<>(); // affichage on sen fous


    stationArrayDeque.add(stationDepart); // j'ajoute dans la file
    stationsDejaParcourues.add(stationDepart);// j'ajoute dans l'ensemble
    boolean trouver = false;

    while(trouver == false && !stationArrayDeque.isEmpty()){

      var stationPoll = stationArrayDeque.pollFirst();
      var tronconStationPoll = mapStationVersTroncons.get(stationPoll);

      // parcours tous les tronçons de la station qui vient d'être pris de la pile.
      for (Troncon troncon : tronconStationPoll) {

        if(!stationsDejaParcourues.contains(troncon.getStationArrivee())){
          stationsDejaParcourues.add(troncon.getStationArrivee());
          stationArrayDeque.add(troncon.getStationArrivee());
           parcoursDesStations.put(troncon.getStationArrivee(), troncon);
        }
        trouver = troncon.getStationArrivee().equals(stationFin);

      }
    }

    /**
     * 1° : Affichage avec une ArrayList -> On spécifie que la station de fin ce met à l'index 0 et cela déplace tous les éléments vers la droite.
     */
    List<Troncon> path = new ArrayList<>();
    Station current = stationFin;

    int nombresDeTroncon = 0, dureeTransport = 0, dureeTotale = 0;

    while (parcoursDesStations.get(current) != null) {
      if(parcoursDesStations.get(current).getStationArrivee().equals(stationFin)){
        dureeTotale += parcoursDesStations.get(current).getLigne().getAttenteMoyenne();
      }
      Troncon ancienTroncon = parcoursDesStations.get(current);
      dureeTransport += parcoursDesStations.get(current).getDuree();
      path.add(0,parcoursDesStations.get(current));
      nombresDeTroncon++;
      current = parcoursDesStations.get(current).getStationDepart();
      if(parcoursDesStations.get(current) != null && !ancienTroncon.getLigne().equals(parcoursDesStations.get(current).getLigne())){
        dureeTotale += parcoursDesStations.get(current).getLigne().getAttenteMoyenne();
      }
    }
    dureeTotale += dureeTransport;

    for (Troncon troncon :path) {
      System.out.println(troncon.toString());
    }

    System.out.println("nbTroncons=" + nombresDeTroncon);
    System.out.println("dureeTransport=" + dureeTransport);
    System.out.println("dureeTotale=" + dureeTotale);



    /**
     * 2° : Affichage avec une LinkedList -> LIFO : on met depuis la station de fin donc on prends le dernier qui à été ajouté (la station de départ).
     */
    /*
    LinkedList<Station> path = new LinkedList<>();
    Station current = stationFin;

    while (current != null) {
      path.add(current);
      current = parcoursDesStations.get(current);
    }

    while(!path.isEmpty()){
      System.out.println(path.removeLast());
    }
     */

    /**
     * 3° : Utiliser la fonction "affichage()" fais en BONUS, qui fonctionne pour calculerCheminMinimisantNombreTroncons et calculerCheminMinimisantTempsTransport
     */

  }

  public void calculerCheminMinimisantTempsTransport(String nomStationDepart, String nomstationArrivee) {
    Set<Station> etiquettesDefinitives= new HashSet<>();
    // TreeSet de Station avec un comparator sur le temps pour arriver a la station,
    // quand appelle de pollFirst ça me donne la station avec le temps le plus court
    TreeSet<Station> etiquettesProvisoires = new TreeSet<>(
        Comparator.comparing(Station::getTempsPourArriver).thenComparing(Station::getNomStation));

    // pour affichage.
    HashMap<Station, Troncon> parcoursDesStations = new HashMap<>();


    Station stationDepart = mapNomStationVerStation.get(nomStationDepart);
    Station stationFin = mapNomStationVerStation.get(nomstationArrivee);

    stationDepart.setTempsPourArriver(0);
    etiquettesProvisoires.add(stationDepart);

    while(!etiquettesProvisoires.isEmpty()){
      var stationEnCours = etiquettesProvisoires.pollFirst();
      etiquettesDefinitives.add(stationEnCours);
      if(stationEnCours.equals(stationFin)){
        break;
      }
      var ensemblesTronconsSortantsDeLaStationLaPlusRapide = mapStationVersTroncons.get(stationEnCours);
      for (Troncon troncon : ensemblesTronconsSortantsDeLaStationLaPlusRapide) {
        var stationArriveeTronconCourant = troncon.getStationArrivee();
        int tempsPourStationSuivant = stationEnCours.getTempsPourArriver() + troncon.getDuree();
        if( !etiquettesDefinitives.contains(stationArriveeTronconCourant) &&  (!etiquettesProvisoires.contains(stationArriveeTronconCourant) || tempsPourStationSuivant < stationArriveeTronconCourant.getTempsPourArriver())){
          etiquettesProvisoires.remove(stationArriveeTronconCourant);
          stationArriveeTronconCourant.setTempsPourArriver(tempsPourStationSuivant);
          etiquettesProvisoires.add(stationArriveeTronconCourant);
          parcoursDesStations.put(stationArriveeTronconCourant, troncon);
        }
      }
    }
    /**
     * 1° : Affichage
     */
    List<Troncon> path = new ArrayList<>();
    Station current = stationFin;

    int nombresDeTroncon = 0, dureeTransport = 0, dureeTotale = 0;

    while (parcoursDesStations.get(current) != null) {
      if(parcoursDesStations.get(current).getStationArrivee().equals(stationFin)){
        dureeTotale += parcoursDesStations.get(current).getLigne().getAttenteMoyenne();
      }
      Troncon ancienTroncon = parcoursDesStations.get(current);
      dureeTransport += parcoursDesStations.get(current).getDuree();
      path.add(0,parcoursDesStations.get(current));
      nombresDeTroncon++;
      current = parcoursDesStations.get(current).getStationDepart();
      if(parcoursDesStations.get(current) != null && !ancienTroncon.getLigne().equals(parcoursDesStations.get(current).getLigne())){
        dureeTotale += parcoursDesStations.get(current).getLigne().getAttenteMoyenne();
      }
    }
    dureeTotale += dureeTransport;

    for (Troncon troncon :path) {
      System.out.println(troncon.toString());
    }

    System.out.println("nbTroncons=" + nombresDeTroncon);
    System.out.println("dureeTransport=" + dureeTransport);
    System.out.println("dureeTotale=" + dureeTotale);

    /**
     * 2° : Utiliser la fonction "affichage()" fais en BONUS, qui fonctionne pour calculerCheminMinimisantNombreTroncons et calculerCheminMinimisantTempsTransport
     */
  }

  public void affichage(Station stationDepart, Station arrivee,
      HashMap<Station, Troncon> parcoursDesStations) {
    int dureeTotale = 0;
    int dureeTransport = 0;
    int nbTroncons = 0;
    Station retracageStation = arrivee;
    ArrayList<Troncon> tracageParcours = new ArrayList<>();
    while (!retracageStation.equals(stationDepart)) {
      Troncon tronconRetracage = parcoursDesStations.get(retracageStation);
      Troncon tronconSuivant = parcoursDesStations.get(tronconRetracage.getStationDepart());
      Troncon tronconGroupe = new Troncon(tronconRetracage.getLigne(),
          tronconRetracage.getStationDepart(), tronconRetracage.getStationArrivee(),
          tronconRetracage.getDuree());
      int idLigne = tronconRetracage.getLigne().getId();

      dureeTotale += tronconRetracage.getLigne().getAttenteMoyenne();

      if (tronconSuivant != null && tronconSuivant.getLigne().getId() == idLigne) {
        tronconGroupe.setDuree(0);
        while (idLigne == tronconSuivant.getLigne().getId()) {
          tronconGroupe.setStationDepart(tronconRetracage.getStationDepart());
          tronconGroupe.setDuree(tronconGroupe.getDuree() + tronconRetracage.getDuree());
          tronconSuivant = parcoursDesStations.get(tronconRetracage.getStationDepart());
          tronconRetracage = tronconSuivant;
          nbTroncons++;
        }
      } else {
        nbTroncons++;
      }

      tracageParcours.add(tronconGroupe);
      dureeTransport += tronconGroupe.getDuree();
      retracageStation = tronconGroupe.getStationDepart();
    }
    dureeTotale += dureeTransport;

    Collections.reverse(tracageParcours);
    for (Troncon troncon : tracageParcours) {
      System.out.println(troncon);
    }

    System.out.println("NbrTroncons=" + nbTroncons);
    System.out.println("dureeTransport=" + dureeTransport + "  dureeTotale=" + dureeTotale);
  }






}
