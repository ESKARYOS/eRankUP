package br.com.eskaryos.rankup.data;

import br.com.eskaryos.rankup.ranks.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Profile {

    private UUID uuid;
    private Rank rank;
    private Rank next;


    public Profile(UUID uuid, Rank rank) {
        this.uuid = uuid;
        this.rank = rank;
    }

    public Player getPlayer(){
        return Bukkit.getPlayer(getUUID());
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public Rank getNext() {
        return next;
    }

    public void setNext(Rank next) {
        this.next = next;
    }
}
