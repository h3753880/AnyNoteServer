/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package anynote.server;

/**
 *
 * @author Howard
 */
public class CityMap {
    private String city;
    private double latitude;
    private double longitude;
    private String title;
    private String content;
    
    public String userName="";
    public int noteId;
    public String userId="";
    public String friendName="";
    //大城市代表
    public CityMap(String city,double latitude,double longitude)
    {
        this.city=city;
        this.latitude=latitude;
        this.longitude=longitude;
    }
    //每個提醒
    public CityMap(String city,double latitude,double longitude,String title,String content,String userName,int noteId,String userId)
    {
        this.userName=userName;
        this.noteId=noteId;
        this.userId=userId;
        this.title=title;
        this.content=content;
        this.city=city;
        this.latitude=latitude;
        this.longitude=longitude;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public String getContent()
    {
        return content;
    }
    
    public String getCity(){
        return city;
    }
    
    public double getLongitude(){
        return longitude;
    }
    
    public double getLatitude(){
        return latitude;
    }
}
