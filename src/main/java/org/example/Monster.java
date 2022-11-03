package org.example;

import org.sql2o.Connection;

import java.security.Timestamp;
import java.util.List;
import java.util.Objects;

public class Monster {
    private Timestamp birthday;
    private Timestamp lastSlept;
    private Timestamp lastFeed;
    private Timestamp lastPlayed;
    private String name;
    private int personId;

    private int id;
    private int foodLevel;
    private int sleepLevel;
    private int playLevel;
//    private int allLevels;



    public static final int MAX_FOOD_LEVEL = 3;
    public  static final int MAX_SLEEP_LEVEL = 8;
    public static final int MAX_PLAY_LEVEL = 12;
    public static final int MIN_ALL_LEVELS = 0;
    public Monster(String name, int personId) {
        this.name = name;
        this.personId = personId;
        this.playLevel = MAX_PLAY_LEVEL/2;
        this.sleepLevel = MAX_SLEEP_LEVEL/2;
        this.foodLevel = MAX_FOOD_LEVEL/2;
//        this.allLevels = MIN_ALL_LEVELS /0;
    }

    public String getName() {
        return name;
    }

    public int getPersonId() {
        return personId;
    }

    public int getPlayLevel() {
        return playLevel;
    }

    public int getSleepLevel() {
        return sleepLevel;
    }

    public int getFoodLevel() {
        return foodLevel;
    }

//    public int getAllLevels() {
//        return allLevels;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Monster monster)) return false;
        return personId == monster.personId && Objects.equals(name, monster.name);
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(name, personId);
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

    public int getId() {
        return id;
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
    public boolean isAlive(){
        if (foodLevel <= MIN_ALL_LEVELS || sleepLevel<= MIN_ALL_LEVELS ||
                playLevel<= MIN_ALL_LEVELS){
            return false;
        }
        return true;
    }
    public void depleteLevels(){
        playLevel--;
        foodLevel--;
        sleepLevel--;
    }
    public void play(){
        if (playLevel >= MAX_PLAY_LEVEL){
            throw  new UnsupportedOperationException("you cannot play with monster any more");
        }
        try (Connection conn = DB.sql2o.open()){
            String sql = " UPDATE monster SET lastPlayed = now() WHERE id = :id";
            conn.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();

        }
        playLevel++;
    }
    public void sleep(){
        if (sleepLevel >= MAX_SLEEP_LEVEL){
            throw new UnsupportedOperationException("you cannot sleep your monster anymore");
        }
        try (Connection conn = DB.sql2o.open()){
    String sql = "UPDATE monster SET lastSlept = now() WHERE id = :id";
    conn.createQuery(sql)
            .addParameter("id", id)
            .executeUpdate();

        }
        sleepLevel++;
    }
    public void feed(){
        if (foodLevel >= MAX_FOOD_LEVEL){
            throw new UnsupportedOperationException("you cannot feed your monster anymore!");
        }
        try(Connection conn = DB.sql2o.open()) {
            String sql = "UPDATE monster SET lastFeed = now() WHERE id = :id";
            conn.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();

        }
        foodLevel++;
    }
    public Timestamp getBirthday(){
        return birthday;
    }

    public Timestamp getLastSlept() {
        return lastSlept;
    }

    public Timestamp getLastPlayed() {
        return lastPlayed;
    }

    public Timestamp getLastFeed() {
        return lastFeed;
    }
}

