/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package anynote.server;

/**
 *
 * @author Howard
 */
public class FavPlace {
    private String place;
    private String userId;
    private int count;
    
    public FavPlace(String place,int count)
    {
        this.place=place;
        this.count=count;
    }
    
    public FavPlace(String userId,String place)
    {
        this.userId=userId;
        this.place=place;
    }
    
    public String getPlace(){
        return place;
    }
    
    public String getUserId(){
        return userId;
    }
    
    public int getCount(){
        return count;
    }
}
