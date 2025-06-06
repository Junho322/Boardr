package ca.mcgill.ecse321.boardr.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


/**
 * Represents a player role associated with a user account.
 * 
 * @author Junho, Jione, David Zhou
 * @version 1.0
 * @since 2023-10-05
 */
@Entity
@DiscriminatorValue("PLAYER")
public class Player extends UserRole {


    public Player() {
    }

    public Player(UserAccount userAccount) {
        super(userAccount);
    }
}