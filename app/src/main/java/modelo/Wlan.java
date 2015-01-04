package modelo;

/**
 * Created by JairSteve on 14/11/2014.
 */
public class Wlan {

    private int id_wlan;
    private String bssid;
    private String id_vendor;
    private String ssid;
    private String capabilities;
    private int frequency;
    private int level;
    private int timestamp;

    private int wlan_type;
    private String password;
    private int current;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getId_wlan() {
        return id_wlan;
    }

    public void setId_wlan(int id_wlan) {
        this.id_wlan = id_wlan;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getId_vendor() {
        return id_vendor;
    }

    public void setId_vendor(String id_vendor) {
        this.id_vendor = id_vendor;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public int getWlan_type() {
        return wlan_type;
    }

    public void setWlan_type(int wlan_type) {
        this.wlan_type = wlan_type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
