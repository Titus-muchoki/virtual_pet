package org.example;

import org.sql2o.Connection;

import java.security.Timestamp;
import java.util.List;
import java.util.Objects;

public class Monster {

        private String name;

        private int personId;

        private int id;
    private int foodLevel;
    private int sleepLevel;
    private int playLevel;

    private Timestamp birthday;
    private Timestamp lastSlept;
    private Timestamp lastAte;
    private Timestamp lastPlayed;

    public static final int MAX_FOOD_LEVEL = 3;
    public static final int MAX_SLEEP_LEVEL = 8;
    public static final int MAX_PLAY_LEVEL = 12;
    public static final int MIN_ALL_LEVELS = 0;


    public Monster(String name, int personId) {
    this.name = name;
    this.personId = personId;
    this.playLevel = MAX_PLAY_LEVEL / 2;
    this.sleepLevel = MAX_SLEEP_LEVEL / 2;
    this.foodLevel = MAX_FOOD_LEVEL / 2;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Monster)) return false;
        Monster monster = (Monster) o;
        return personId == monster.personId && id == monster.id && foodLevel == monster.foodLevel && sleepLevel == monster.sleepLevel && playLevel == monster.playLevel && Objects.equals(name, monster.name) && Objects.equals(birthday, monster.birthday) && Objects.equals(lastSlept, monster.lastSlept) && Objects.equals(lastAte, monster.lastAte) && Objects.equals(lastPlayed, monster.lastPlayed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, personId, id, foodLevel, sleepLevel, playLevel, birthday, lastSlept, lastAte, lastPlayed);
    }

    public String getName() {
        return name;
    }

//    public void setName(String name) {
//        this.name = name;
//    }

    public int getPersonId() {
        return personId;
    }

//    public void setPersonId(int personId) {
//        this.personId = personId;
//    }

    public int getId() {
        return id;
    }

//    public void setId(int id) {
//        this.id = id;
//    }

    public int getPlayLevel() {
        return playLevel;
    }

//    public void setPlayLevel(int playLevel) {
//        this.playLevel = playLevel;
//    }

    public int getSleepLevel() {
        return sleepLevel;
    }

//    public void setSleepLevel(int sleepLevel) {
//        this.sleepLevel = sleepLevel;
//    }

    public int getFoodLevel() {
        return foodLevel;
    }

//    public void setFoodLevel(int foodLevel) {
//        this.foodLevel = foodLevel;
//    }

    public Timestamp getBirthday() {
        return birthday;
    }

//    public void setBirthday(Timestamp birthday) {
//        this.birthday = birthday;
//    }

    public void save() {
    try(Connection con = DB.sql2o.open()) {
        String sql = "INSERT INTO monsters (name, personId, birthday) VALUES (:name, :personId, now())";
        this.id = (int) con.createQuery(sql, true)
                .addParameter("name", this.name)
                .addParameter("personId", this.personId)
                .executeUpdate()
                .getKey();
    }
}
    public static List<Monster> all() {
        String sql = "SELECT * FROM monsters";
        try(Connection con = DB.sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(Monster.class);
        }
    }
    public static Monster find(int id) {
        try(Connection con = DB.sql2o.open()) {
            String sql = "SELECT * FROM monsters where id=:id";
            Monster monster = con.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Monster.class);
            return monster;
        }
    }
    public boolean isAlive() {
        if (foodLevel <= MIN_ALL_LEVELS ||
                playLevel <= MIN_ALL_LEVELS ||
                sleepLevel <= MIN_ALL_LEVELS) {
            return false;
        }
        return true;
    }
    public void depleteLevels(){
        playLevel--;
        foodLevel--;
        sleepLevel--;
    }

    public void sleep(){
        if (sleepLevel >= MAX_SLEEP_LEVEL){
            throw new UnsupportedOperationException("You cannot make your monster sleep anymore!");
        }
        sleepLevel++;
    }
public void play(){
    if (playLevel >= MAX_PLAY_LEVEL){
        throw new UnsupportedOperationException("You cannot play with monster anymore!");
    }
    playLevel++;
}
    public void feed(){
        if (foodLevel >= MAX_FOOD_LEVEL){
            throw new UnsupportedOperationException("You cannot feed your monster anymore!");
        }
        foodLevel++;
    }
}


