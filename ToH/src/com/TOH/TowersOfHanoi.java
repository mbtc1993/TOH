package com.TOH;


import java.util.Scanner;
import java.util.ArrayList;
import java.util.Stack;



public class TowersOfHanoi {
    private int num_disks;
    private int num_towers;
    private int start_tower;
    private int end_tower;
    private ArrayList<Integer> aux;
    private ArrayList<Stack<Integer>> towers;

    public TowersOfHanoi(int num_disks, int num_towers) {
        this.num_disks=num_disks;
        this.num_towers=num_towers;
        this.start_tower=1;
        this.end_tower=num_towers;
        this.aux=new ArrayList<Integer>();
        for(int i=2;i<num_towers;i++) {
            this.aux.add(i);
        }
        this.towers=new ArrayList<Stack<Integer>>(num_towers);
        //towers[i] refers to tower i+1 bc of zero indexing
        for (int i = 0; i < num_towers; i++) {
            this.towers.add(new Stack<Integer>());
        }
        for(int i=num_disks;i>0;i--)
            this.towers.get(0).push(i);

    }

    public int partition(int n, int r)
    {
        //Pick integer k, where 0<=k<n
        //such that 2H(k,r)+H(n-k,r-1) is minimized
        //For C(r-3+x,r-2)<n<=C(r-2+x,r-2),
        //Any k satisfying C(r-4+x,r-2)<=k<=C(r-3+x,r-2)
        //Is an optimal partition number
        //Observe that C(r-2+x,r-2)=1 when x=0
        //and C(r-2+(x+1),r-2)=C(r-2+x,r-2)*(r-2+(x+1)/(x+1))
        //
        int upper_bd=1;
        int x=0;
        while (n>upper_bd){
                x++;
                upper_bd=upper_bd*(r-2+x)/x;//n is always >1 here, so we don't have to worry about division by 0
        }
        //now we have C(r-3+x,r-2)<n<=upper_bd=C(r-2+x,r-2)
        //we want to pick k=C(r-3+x,r-2)
        //we get the desired value by multiply upper_bd by (x/r-2+x)
        return upper_bd*x/(r-2+x);
    }

    public void solveTowersOfHanoi(){
        printTowers();
        doSolveTowersOfHanoi(num_disks,num_towers,start_tower,end_tower,aux);
    }

    public void move(int fromTower, int toTower){
        int disc=towers.get(fromTower-1).pop();
        towers.get(toTower-1).push(disc);
        System.out.println("Moved disc "+disc+" from Tower "+fromTower+" to Tower "+toTower);
        printTowers();
    }

    public void printTowers(){
        for(int i=0;i<num_towers;i++){
            System.out.print("Tower "+(i+1)+": ");
            towers.get(i).forEach(result -> System.out.print(result + " "));
            System.out.println();
        }
    }

    public void doSolveTowersOfHanoi(int n, int r, int startTower, int destTower, ArrayList<Integer> aux)
    {
        //if there are no disks to be moved we do nothing; this is a possible case because partition can return 0
        if(n==0){
            return;
        }

        // If only one disk, move it
        if(n==1)
        {
            move(startTower,destTower);
            return;
        }


        //We will move the top k disks from startTower to an auxillary tower using r towers
        //Move n-k  disks to destTower using r-1 towers
        //Finally move k disks from the auxillary tower to destTower
        //Letting H(x,y) denote the number of steps required to move x disks using y towers
        //We know the total number of steps required will be
        //2H(k,r)+H(n-k,r-1)
        //So we choose k to minimize the above expression
        //We also note that H(y,2) is only finite when y=1, so for r=3, we set k=n-1
        int k;
        if(r<4)
            k=n-1;
        else
            k=partition(n,r);
        // Move top k disks from tower 1 to aux[0] using all towers
        ArrayList<Integer> aux1 =  (ArrayList<Integer>) aux.clone();
        int nDest=aux1.get(0);
        aux1.set(0,destTower);
        doSolveTowersOfHanoi(k,r,startTower,nDest,aux1);

        //Move remaining n-k disks from tower to dest using r-1. Can't use nDest
        ArrayList<Integer> aux2 =  (ArrayList<Integer>) aux.clone();
        aux2.remove(0);
        doSolveTowersOfHanoi(n-k,r-1,startTower,destTower,aux2);


        // Move k disks from tower aux[0] to dest using all towers
        ArrayList<Integer> aux3 =  (ArrayList<Integer>) aux.clone();
        aux3.set(0,startTower);
        doSolveTowersOfHanoi(k,r,nDest,destTower, aux3);

    }


    public static void main(String args[])
    {


        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter number of disks, min 3, max 40:- ");

        int n = scanner.nextInt();

        System.out.println("Enter number of towers, min 5, max 10:- ");

        int r =scanner.nextInt();

        scanner.close();
        TowersOfHanoi obj = new TowersOfHanoi(n,r);


        obj.solveTowersOfHanoi();


    }
}
