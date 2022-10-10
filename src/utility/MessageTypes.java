package utility;

import com.google.gson.annotations.SerializedName;

public enum MessageTypes {
    @SerializedName("CONNECT")
    CONNECT,
    @SerializedName("MESSAGE")
    MESSAGE,
    @SerializedName("DISCONNECT")
    DISCONNECT
}
