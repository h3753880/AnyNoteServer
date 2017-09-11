package anynote.server;

public class TimeNote extends Note {
	public String time="";
	public int cycle=0;
        public int status=0;
        public int noteType=0;
        public String friendName="";
        
        public TimeNote(int noteId,String userName, String userId,String friends, String title, String content, String time, int cycle)
        {
            this.userName=userName;
            this.userId=userId;
            this.noteId=noteId;
            this.friends=friends;
            this.title=title;
            this.content=content;
            this.time=time;
            this.cycle=cycle;
        }
        public TimeNote(int noteId,String userName,String userId, String friends, String title, 
                String content, String time, int cycle,int status,int noteType,String img,String sound)
        {
            this.userId=userId;
            this.noteId=noteId;
            this.userName=userName;
            this.friends=friends;
            this.title=title;
            this.content=content;
            this.time=time;
            this.cycle=cycle;
            this.noteType=noteType;
            this.status=status;
            this.img=img;
            this.sound=sound;
        }        
        public TimeNote(int noteId,String userId, String friends, String title, 
                String content, String time, int cycle,int status,int noteType,String img,String sound)
        {
            this.userId=userId;
            this.noteId=noteId;
            this.friends=friends;
            this.title=title;
            this.content=content;
            this.time=time;
            this.cycle=cycle;
            this.noteType=noteType;
            this.status=status;
            this.img=img;
            this.sound=sound;
        }
  
        public TimeNote()
        {
            this.userId="";
            this.noteId=-1;
            this.friends="";
            this.title="";
            this.content="";
            this.time="";
            this.cycle=0;
            this.noteType=0;
            this.status=0;
            this.img="";
        }
        
	public void setTime(int year,int month,int day,int hourOfDay,int minute,int second){
		time+=Integer.toString(year)+"-"+Integer.toString(month)+"-"+Integer.toString(day)
			+"-"+Integer.toString(hourOfDay)+"-"+Integer.toString(minute)+"-"+Integer.toString(second);
	}
}
