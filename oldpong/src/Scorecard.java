public class Scorecard {
    int sc1;
    int sc2;
    public Scorecard(){
        sc1 = 0;
        sc2 = 0;
    }
    void increaseScore(char x){
        if(x=='1')
          sc1++;
        if(x=='2')
          sc2++;
    }
}
