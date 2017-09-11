package anynote.server;

public class Note {

    public int noteId;
    public String userName;
    public String userId;
    public String friends;
    public String title;
    public String content;
    public String img;
    public String sound;
    public Note() {
        noteId = 0;
        title = "";
        friends = "";
        userId = "";
        content = "";

    }
}