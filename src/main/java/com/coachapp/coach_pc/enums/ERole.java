package com.coachapp.coach_pc.enums;

// Unused imports (The Java compiler has to process each import statement, even if the imported class is not used.)
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


// ENUMS are generally understood to be upper case variables for readability
public enum ERole {
    athlete,
    coach
}
