/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package anynote.server;

/**
 *
 * @author Howard
 */
public class City {
    private String city;
    private int number;
    
    public City(String city, int number)
    {
        this.city=city;
        this.number=number;
    }
    
    public String getCity()
    {
        return city;
    }
    
    public int getNumber()
    {
        return number;
    }
    
}
