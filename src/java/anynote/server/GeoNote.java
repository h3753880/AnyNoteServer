/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package anynote.server;

/**
 *
 * @author tony
 */
public class GeoNote extends Note {

    public double longitude;
    public double latitude;
    public String timeStart;
    public String timeEnd;
    public double range;
    public boolean getIn;
    public boolean getOut;
    public int status;
    public int noteType;
    public String city;
public GeoNote()
{
     this.noteId = 0;
        this.userId = "";
        this.friends = "";
        this.title = "";
        this.content = "";
        this.longitude = 0;
        this.latitude = 0;
        this.timeStart ="";
        this.timeEnd ="";
        this.range = 0;
        this.getIn = false;
        this.getOut = false;
        this.status = -1;
        this.noteType =-1;
        this.img = "";
        this.city="";
    
}
  public GeoNote(int noteId, String userId, String friends, String title,
            String content, double longitude, double latitude, String timeStart,
            String timeEnd, double range, boolean getIn, boolean getOut, int status, int noteType, String img,String userName,String sound,String city) {
        this.noteId = noteId;
        this.userId = userId;
        this.friends = friends;
        this.userName=userName;
        this.title = title;
        this.content = content;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.range = range;
        this.getIn = getIn;
        this.getOut = getOut;
        this.status = status;
        this.noteType = noteType;
        this.img = img;
        this.sound=sound;
        this.city=city;
    }
    public GeoNote(int noteId, String userId, String friends, String title,
            String content, double longitude, double latitude, String timeStart, String timeEnd, double range, boolean getIn,
            boolean getOut, int status, int noteType, String img,String sound,String city) {
        this.noteId = noteId;
        this.userId = userId;
        this.friends = friends;
        this.title = title;
        this.content = content;
        this.longitude = longitude;
        this.latitude = latitude;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.range = range;
        this.getIn = getIn;
        this.getOut = getOut;
        this.status = status;
        this.noteType = noteType;
        this.img = img;
        this.sound=sound;
        this.city=city;
    }
}