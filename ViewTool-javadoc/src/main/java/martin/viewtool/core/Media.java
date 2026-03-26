
package martin.viewtool.core;

/**
 * Represents a media entry returned by the remote API.
 * Fields are mapped directly from the API JSON response.
 *
 * @author cesar
 */
import com.fasterxml.jackson.annotation.JsonProperty;

public class Media {
    /** Unique identifier of the media on the server. */
    @JsonProperty("id")
    public int id;
    /** ID of the user who owns the media. */
    @JsonProperty("userId")
    public int userId;
    /** Original URL the media was downloaded from. */
    @JsonProperty("downloadedFromUrl")
    public String downloadedFromUrl;
    /** File name of the media. */
    @JsonProperty("mediaFileName")
    public String mediaFileName;
    /** MIME type of the media file. */
    @JsonProperty("mediaMimeType")
    public String mediaMimeType;
    /** Unique blob GUID used for cloud storage. */
    @JsonProperty("blobNameGuid")
    public String blobNameGuid;
    /** Public URL to the media blob in cloud storage. */
    @JsonProperty("blobUrl")
    public String blobUrl;

    @Override
    public String toString() {
        return String.format("Media{id=%d, userId=%d, file=%s, blob=%s}", id, userId, mediaFileName, blobNameGuid);
    }
}
