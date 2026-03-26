
package martin.viewtool.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a user returned by the remote API.
 *
 * @author cesar
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Usuari {
    /** Unique identifier of the user. */
    @JsonProperty("id")
    public int id;

    /** Email address of the user. */
    @JsonProperty("email")
    public String email = "";

    /** Hashed password. */
    @JsonProperty("passwordHash")
    public String passwordHash;

    /** Nickname of the user. */
    @JsonProperty("nickName")
    public String nickName;

    /** Profile picture. */
    @JsonProperty("picture")
    public byte[] picture;

    /** Original file name of the profile picture. */
    @JsonProperty("pictureFileName")
    public String pictureFileName;

    /** Date of birth. */
    @JsonProperty("dateOfBirth")
    public String dateOfBirth;

    /** Registration timestamp. */
    @JsonProperty("registeredAt")
    public String registeredAt;

    @Override
    public String toString() {
        return String.format("Usuari{id=%d, email=%s, nickName=%s}", id, email, nickName);
    }
}