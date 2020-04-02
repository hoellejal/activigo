package com.example.activigo;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

public class AccessToDB {
    private final static String TAG = "DataBaseFirestore";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final static String USERS_DATABASE = "Users"; // METTRE UNE MAJUSCULE ICI
    private final static String ACTIVITY_DATABASE = "Activity";
    private final static String PROFILE_DATABASE = "Profil";
    public static String gen_id_user = null;
    private Profil p=new Profil();
    private static final int score_lieu=1000;
    private static final int score_budget=100;
    private static final int score_endroit=10;
    public AccessToDB(){
    }

    /**
     * Ajoute un utilisateur à la base de donnée
     * @param username le nom de l'utilisateur
     * @param password son mot de passe
     */
    public void addUserInDB(String username, String password, final onLoginSuccess onLoginSuccess){
        Map<String,Object> dataToAdd = new HashMap<>();

        dataToAdd.put("Username", username);
        dataToAdd.put("Password", password);

        db.collection(USERS_DATABASE).add(dataToAdd).addOnSuccessListener(documentReference -> {
            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

            gen_id_user =documentReference.getId();
            onLoginSuccess.perform(true,gen_id_user);
        }).addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    /**
     * Supprime un utilisateur de la base de donnée. Seulement disponible pour un mode administrateur.
     * @param username le nom de l'utilisateur
     * @param password son mot de passe
     */
    public void removeUserFromDB(final String username, final String password){
        db.collection(USERS_DATABASE).whereEqualTo("Username", username).whereEqualTo("Password",password).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    db.collection(USERS_DATABASE).document(document.getId()).delete().addOnSuccessListener(aVoid -> Log.d(TAG, "User successfully deleted!")).addOnFailureListener(e -> Log.w(TAG, "Error deleting user", e));
                }
            } else {
                Log.d(TAG, "Error getting user's profile: ", task.getException());
            }
        });
    }

    /**
     * Envoi une image sur Firebase storage et récupère son url de téléchargement
     * @param image l'url de l'image à envoyer
     * @param onUploadSuccess action à effectuer une fois que l'upload est fait
     */
    public void sendPicture(Uri image, final onUploadSuccess onUploadSuccess) {
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("images").child(Objects.requireNonNull(image.getLastPathSegment()));
        storageRef.putFile(image).addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnCompleteListener(task -> onUploadSuccess.perform(task.getResult())).addOnFailureListener(e -> Log.d("Erreur","impossible de récupérer l'adresse de téléchargement"))).addOnFailureListener(e -> Log.d("Erreur","transfert non effectué"));
    }

    /**
     * Authentifie l'utilisateur
     * @param username le nom de l'utilisateur
     * @param pass son mot de passe
     * @param onLoginSuccess l'action à effectuer si l'authentification fonctionne
     */
    public void connectionUserDB(final String username, final String pass, final onLoginSuccess onLoginSuccess) {
        db.collection(USERS_DATABASE).whereEqualTo("Username", username).whereEqualTo("Password",pass).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean authentified = false;
                String id=null;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Log.d(TAG, document.getId() + " => " + document.getData());
                    authentified = true;
                    id =document.getId();
                    break;
                }
                gen_id_user=id;
                onLoginSuccess.perform(authentified,id);
            } else {
                Log.w(TAG, "Error getting document.", task.getException());
            }
        });
    }

    /**
     * Ajoute une activité dans la base de données
     * @param myActivity l'activité à rajouter
     * @param onActivityCreated l'action à effectuer quand on a ajouté l'activité
     */
    public void addActivityInDB(Activite myActivity, final onActivityCreated onActivityCreated){
        Map<String, Object> dataAboutActivity = new HashMap<>();

        dataAboutActivity.put("Name",myActivity.getNomActivite());
        dataAboutActivity.put("Photo",myActivity.getPhotoActivite());
        dataAboutActivity.put("Description",myActivity.getDescription());
        dataAboutActivity.put("Longitude", myActivity.getLngActivite());
        dataAboutActivity.put("Lattitude", myActivity.getLattActivite());
        dataAboutActivity.put("Note", myActivity.getNoteActivite());
        dataAboutActivity.put("Popularity", myActivity.getPopularite());
        dataAboutActivity.put("Price", myActivity.getPrix());
        dataAboutActivity.put("Nature", myActivity.getNature());
        dataAboutActivity.put("Culture", myActivity.isCulture());
        dataAboutActivity.put("Group", myActivity.isGroupe());
        dataAboutActivity.put("Entertainment", myActivity.isDivertissement());
        dataAboutActivity.put("Place to discover", myActivity.isEndroits());
        dataAboutActivity.put("Gastronomy", myActivity.isGastronomie());
        dataAboutActivity.put("Sports", myActivity.isSport());
        dataAboutActivity.put("nb_note",0);

        db.collection(ACTIVITY_DATABASE).add(dataAboutActivity).addOnSuccessListener(documentReference -> {
            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
            onActivityCreated.perform(documentReference.getId());

        }).addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    /**
     * Récupère le profil de l'utilisateur courant
     */
    public void getProfil(onProfilComplete onProfilComplete){
        db.collection(PROFILE_DATABASE).whereEqualTo("id_user",gen_id_user).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Profil profil=new Profil();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> map = document.getData();
                    ArrayList<String> wishlist = null;
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        if (entry.getKey().equals("wishlist")) {
                            wishlist= (ArrayList<String>) entry.getValue();
                        }
                    }
                    profil.setIdProfil(document.getId());
                    profil.setWishlist(wishlist);
                    profil.setNature(safeLongToInt((long) document.get("Nature")));
                    profil.setUrbain(safeLongToInt((long) document.get("Urbain")));
                    profil.setPopulaire(safeLongToInt((long) document.get("Populaire")));
                    profil.setPeu_connu(safeLongToInt((long) document.get("Peu connu")));
                    profil.setBudget(safeLongToInt((long) document.get("Budget")));
                    profil.setCulture(safeLongToInt((long) document.get("Culturel")));
                    profil.setGroupe(safeLongToInt((long) document.get("Collectif")));
                    profil.setGastronomie(safeLongToInt((long) document.get("Gastronomique")));
                    profil.setDivertissement(safeLongToInt((long) document.get("Divertissement")));
                    profil.setSport( safeLongToInt((long) document.get("Sportif")));
                    break;
                }
                onProfilComplete.perform(profil);
            }
            else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }

    /**
     * Récupère une liste d'activité à partir d'une liste d'id
     * @param list la liste d'id d'activités
     * @param onActivityComplete l'action à effectuer quand on a récupéré la liste
     */
    public void getActivitiesById(ArrayList<String> list, final onActivityComplete onActivityComplete){
        final ArrayList<Activite> listActivite = new ArrayList<>();

        db.collection(ACTIVITY_DATABASE).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                ArrayList<Activite> activites = new ArrayList<>();
                for(QueryDocumentSnapshot document : task.getResult()) {
                    if(!list.contains(document.getId())){
                        continue;
                    }
                    System.out.println("ok");
                    Activite activite = new Activite(document.getId(), (String) document.get("Name"), (String) document.get("Photo"), (String) document.get("Description"),
                            Float.parseFloat(document.get("Lattitude").toString()),
                            Float.parseFloat(document.get("Longitude").toString()),
                            Float.parseFloat(document.get("Note").toString()),
                            safeLongToInt((long) document.get("Popularity")),
                            safeLongToInt((long) document.get("Price")),
                            safeLongToInt((long) document.get("Nature")),
                            (boolean) document.get("Culture"), (boolean) document.get("Group"), (boolean) document.get("Entertainment"), (boolean) document.get("Place to discover"), (boolean) document.get("Gastronomy"), (boolean) document.get("Sports"), 0);

                    activites.add(activite);
                    System.out.println("activites = " + activites);
                }
                Iterator it=activites.iterator();
                while (it.hasNext()){
                    listActivite.add((Activite) it.next());
                }
                System.out.println("listactivite in db= " + listActivite);
                onActivityComplete.perform(listActivite);
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    /**
     * Rajoute un profil à la base de données, lié à l'utilisateur authentifié
     * @param myProfil le profil à rajouter
     */
    public void addProfilInDB(Profil myProfil){
        Map<String, Object> dataAboutProfil = new HashMap<>();
        dataAboutProfil.put("wishlist",myProfil.getWishlist());
        dataAboutProfil.put("id_user",gen_id_user);
        dataAboutProfil.put("Nature", myProfil.getNature());
        dataAboutProfil.put("Urbain", myProfil.getUrbain());
        dataAboutProfil.put("Populaire", myProfil.getPopulaire());
        dataAboutProfil.put("Peu connu", myProfil.getPeu_connu());
        dataAboutProfil.put("Budget", myProfil.getBudget());
        dataAboutProfil.put("Culturel", myProfil.getCulture());
        dataAboutProfil.put("Collectif", myProfil.getGroupe());
        dataAboutProfil.put("Divertissement", myProfil.getDivertissement());
        dataAboutProfil.put("Gastronomique", myProfil.getGastronomie());
        dataAboutProfil.put("Sportif", myProfil.getSport());


        db.collection(PROFILE_DATABASE).add(dataAboutProfil).addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId())).addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    /**
     * Modifie un profil dans la base de données
     * @param myProfil le profil à modifier
     */
    public void changeProfil(Profil myProfil){
        Map<String, Object> dataAboutProfil = new HashMap<>();
        dataAboutProfil.put("wishlist",myProfil.getWishlist());
        dataAboutProfil.put("Nature", myProfil.getNature());
        dataAboutProfil.put("Urbain", myProfil.getUrbain());
        dataAboutProfil.put("Populaire", myProfil.getPopulaire());
        dataAboutProfil.put("Peu connu", myProfil.getPeu_connu());
        dataAboutProfil.put("Budget", myProfil.getBudget());
        dataAboutProfil.put("Culturel", myProfil.getCulture());
        dataAboutProfil.put("Collectif", myProfil.getGroupe());
        dataAboutProfil.put("Divertissement", myProfil.getDivertissement());
        dataAboutProfil.put("Gastronomique", myProfil.getGastronomie());
        dataAboutProfil.put("Sportif", myProfil.getSport());

        db.collection(PROFILE_DATABASE).document(myProfil.getIdProfil()).update(dataAboutProfil).addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    /**
     * Modifie une activité dans la base de données. Applicable pour la note seulement pour le mode utilisateur.
     * @param activite l'activité à modifier
     */
    public void changeActivity(final Activite activite){
        Map<String, Object> dataAboutActivity = new HashMap<>();
        dataAboutActivity.put("Name",activite.getNomActivite());
        dataAboutActivity.put("Photo",activite.getPhotoActivite());
        dataAboutActivity.put("Description",activite.getDescription());
        dataAboutActivity.put("Longitude", activite.getLngActivite());
        dataAboutActivity.put("Lattitude", activite.getLattActivite());
        dataAboutActivity.put("Note", activite.getNoteActivite());
        dataAboutActivity.put("Popularity", activite.getPopularite());
        dataAboutActivity.put("Price", activite.getPrix());
        dataAboutActivity.put("Nature", activite.getNature());
        dataAboutActivity.put("Culture", activite.isCulture());
        dataAboutActivity.put("Group", activite.isGroupe());
        dataAboutActivity.put("Entertainment", activite.isDivertissement());
        dataAboutActivity.put("Place to discover", activite.isEndroits());
        dataAboutActivity.put("Gastronomy", activite.isGastronomie());
        dataAboutActivity.put("Sports", activite.isSport());
        dataAboutActivity.put("nb_note",activite.getNbnote());

        db.collection(ACTIVITY_DATABASE).document(activite.getIdActivite()).update(dataAboutActivity).addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }

    /**
     * Récupère une liste d'activité selon de nombreux champs de recherche: les paramètres et le profil de l'utilisateur authentifié
     * @param lat lattitude de recherche
     * @param lng longitude de recherche
     * @param seekBarMap des informations de recherche
     * @param checkBoxMap d'autres informations de recherche
     * @param onActivityComplete l'action à effectuer quand on a récupéré la liste
     */
    public void getActivityFiltered(float lat, float lng,final HashMap<String, Integer> seekBarMap, final HashMap<String, Boolean> checkBoxMap, final onActivityComplete onActivityComplete){
        final HashMap<Integer,Activite> listofallactivities=new HashMap<>();
        final ArrayList<Activite> listActivite = new ArrayList<>();
        //envisager le fait qu'on ait pas le lieu, alors lat=lng=0

        // la recherche peut marcher même si les paramètres sont nuls: elle est alors basée seulement sur le profil
        // A priori, GeoPoint pas besoin sensible casse. On obtient un GeoPoint depuis la RechercheActivity
        /*
        if(!lieu.matches("")) {
            // correction d'erreurs sur le lieu: gestion casse et accents
            lieu = lieu.toUpperCase();
            lieu = Normalizer.normalize(lieu, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
        }*/
        //final String lieu_corrected = lieu;
        System.out.println(gen_id_user);
        getProfil(profil -> p=profil);
        System.out.println("id profil = "+p.getIdProfil());
        db.collection(ACTIVITY_DATABASE).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot document : task.getResult()){
                    String id=document.getId();
                    Activite activite = new Activite();
                    activite.setIdActivite(id);
                    activite.setNbnote(safeLongToInt((long)document.get("nb_note")));
                    int score=0;
                    activite.setNomActivite(document.get("Name").toString());
                    activite.setPhotoActivite(document.get("Photo").toString());
                    activite.setDescription(document.get("Description").toString());
                    float longitude = Float.parseFloat(document.get("Longitude").toString());
                    activite.setLngActivite(longitude);
                    float lattitude = Float.parseFloat(document.get("Lattitude").toString());
                    activite.setLattActivite(lattitude);
                    float note=Float.parseFloat(document.get("Note").toString());
                    activite.setNoteActivite(note);
                    long popularite=(long)document.get("Popularity");
                    activite.setPopularite(safeLongToInt(popularite));
                    long prix=(long)document.get("Price");
                    activite.setPrix(safeLongToInt(prix));
                    long nature=Long.parseLong(document.get("Nature").toString());
                    activite.setNature(safeLongToInt(nature));
                    activite.setCulture((boolean)document.get("Culture"));
                    activite.setGroupe((boolean)document.get("Group"));
                    activite.setDivertissement((boolean)document.get("Entertainment"));
                    activite.setEndroits((boolean)document.get("Place to discover"));
                    activite.setGastronomie((boolean)document.get("Gastronomy"));
                    activite.setSport((boolean)document.get("Sports"));

                    /*compute score for each activity*/
                    float distanceEntrePoints = (float) Math.sqrt((lat-lattitude)*(lat-lattitude)+(lng - longitude)*(lng-longitude));
                    if(distanceEntrePoints<1){
                        score += score_lieu;
                    } else {
                        if(distanceEntrePoints<2.5){
                            score += score_lieu/4;
                        }
                    }
                    score+=note;

                    if(checkBoxMap!=null) {
                        int culture = ((boolean) document.get("Culture") && checkBoxMap.get("culture")) ? 1 : 0;
                        int group = ((boolean) document.get("Group") && checkBoxMap.get("groupe")) ? 1 : 0;
                        int divert = ((boolean) document.get("Entertainment") && checkBoxMap.get("divertissement")) ? 1 : 0;
                        int gastronomy = ((boolean) document.get("Gastronomy") && checkBoxMap.get("gastronomie")) ? 1 : 0;
                        int sports = ((boolean) document.get("Sports") && checkBoxMap.get("sport")) ? 1 : 0;
                        int endroit = ((boolean) document.get("Place to discover") && checkBoxMap.get("endroits")) ? 1 : 0;
                        score += culture * p.getCulture();
                        score += group * p.getGroupe();
                        score += divert * p.getDivertissement();
                        score += gastronomy * p.getGastronomie();
                        score += sports * p.getSport();
                        score += endroit * score_endroit;
                    }
                    else {
                        int culture = (boolean) document.get("Culture") ? 1 : 0;
                        int group = (boolean) document.get("Group") ? 1 : 0;
                        int divert = (boolean) document.get("Entertainment") ? 1 : 0;
                        int gastronomy = (boolean) document.get("Gastronomy") ? 1 : 0;
                        int sports = (boolean) document.get("Sports") ? 1 : 0;
                        int endroit = (boolean) document.get("Place to discover") ? 1 : 0;
                        score += culture * p.getCulture();
                        score += group * p.getGroupe();
                        score += divert * p.getDivertissement();
                        score += gastronomy * p.getGastronomie();
                        score += sports * p.getSport();
                        score += endroit * score_endroit;
                    }
                    if(seekBarMap!=null) {
                        if (prix <= p.getBudget() && prix <= seekBarMap.get("prix")) {
                            score += score_budget;
                        }
                        if (nature <= seekBarMap.get("nature")) {
                            score += nature * p.getNature();
                        } else {
                            score += nature * p.getUrbain();
                        }
                        if (popularite <= seekBarMap.get("popularite")) {
                            score += popularite * p.getPeu_connu();
                        } else {
                            score += popularite * p.getPopulaire();
                        }
                    }
                    else{
                        if (prix <= p.getBudget()) {
                            score += score_budget;
                        }
                        if (nature <= 2) {
                            score += nature * p.getNature();
                        } else {
                            score += nature * p.getUrbain();
                        }
                        if (popularite <= 2) {
                            score += popularite * p.getPeu_connu();
                        } else {
                            score += popularite * p.getPopulaire();
                        }
                    }
                    listofallactivities.put(score,activite);
                }
                TreeMap sorted=new TreeMap(listofallactivities);
                NavigableMap a= sorted.descendingMap();
                Set set = a.entrySet();
                Iterator it=set.iterator();
                while (it.hasNext()){
                    Map.Entry m = (Map.Entry) it.next();
                    listActivite.add((Activite) m.getValue());
                }
                onActivityComplete.perform(listActivite);
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });
    }

    /**
     * Transforme un long en un int
     * @param l un long
     * @return un int
     */
    private static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }
}
