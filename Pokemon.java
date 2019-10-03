import java.util.*;
//Abdullah Arif
//Pokemon
public class Pokemon{
	//variables
	private int hp,maxHp,mp,numAtk;
	private String name,type,resistance,weakness;
	private Attack[] attacks;//array list of attack objects
	private boolean stun,disable;
	public Pokemon(String stats){//constructer
		String[] items = stats.split(",");//break up string and assign variable 
		name=items[0];
		maxHp=hp=Integer.parseInt(items[1]);
		mp=50;
		type=items[2];
		resistance=items[3];
		weakness=items[4];
		numAtk=Integer.parseInt(items[5]);
		attacks= new Attack [numAtk];
		for (int i=0;i<numAtk;i++){//split attacks 4 field for attacks 
			attacks[i]=new Attack(items[6+4*i],Integer.parseInt(items[7+4*i]),Integer.parseInt(items[8+4*i]),items[9+4*i]);
		}
	}
	public void fixUp(){//after battle fix up
		this.hp=Math.min(hp+20,maxHp);
		this.mp=50;
		this.stun=false;
		this.disable=false;
	}
	public int getNumAtk(){
		return numAtk;
	}
	public String getName(){
		return name;
	}
	public void round(){
		mp=Math.min(mp+20,50);
	}
	public int getMp(){
		return mp=Math.min(mp,50);
	}
	public int getHp(){
		return hp=Math.max(hp,0);
	}
	
	public void dispAtk(){
		for (int i=0;i<attacks.length;i++){
			System.out.println(i+1+":"+attacks[i].stat());
		}
	}
	public String dispAtk(int num){
		return attacks[num].getName();
	}
	public boolean validAtk(int num){
		return num<=attacks.length && attacks[num].getCost()<=mp ;	
	}
	public boolean checkStun(){
		if(stun){
			stun=false;
			System.out.println(name+ " is stunned. Must pass turn");
			PokemonArena.pass();
		}
		return stun; 
	}
	public String stat(){//return all stat
		String stat="Name:"+name+" Hp:"+ hp+" Type:" +type;
		stat+=(weakness.equals(" "))?"":" Weakness:"+ weakness;
		return stat+=(resistance.equals(" "))?"":" Resistance:"+ resistance;
	}
	public String basicStat(){//return basic stat
		return  "Name:"+name+" Hp:"+ hp+" Type:" +type;
	}	
	public void attackCalc(int atkNum,Pokemon victim){//check for things that effect dmg 
		int temp=this.attacks[atkNum].getDmg();
		if(this.type.equals(victim.weakness)){
			temp*=2;
			System.out.println("It's super effective!");
		}
		else if(this.type.equals(victim.resistance)){
			temp/=2;
			System.out.println("It's not very effective...");
		}
		temp-=(this.disable)?10:0;//factor in disable 
		this.mp-=this.attacks[atkNum].getCost();//subtract energy
		Random coin = new Random();
		boolean flag = coin.nextBoolean();//many conditions have a 50% chance of happening
		victim.hp-=temp;//subtract hp
		if(this.attacks[atkNum].getSpec().equals("wild storm")){
			System.out.println("Wild Storm attack!");
			if(flag){
				while(flag){
					flag = coin.nextBoolean();
					victim.hp-=temp;
					System.out.println("Succeful hit!");
				}
			}
			else{
				victim.hp+=temp;
				System.out.println("Attack failed");
			}
		}
		if(this.attacks[atkNum].getSpec().equals("stun")&&flag){			
			victim.stun=true;//changes Pokemon stun status
			System.out.println(victim.name+" became stunned!"); 
		}
		if(this.attacks[atkNum].getSpec().equals("disable")&&flag){
			victim.disable=true;
			System.out.println(victim.name+" became disabled!");
		}
		if(this.attacks[atkNum].getSpec().equals("wild card")){
			if (flag){//if flag true wild card missed 50% chance so it doesn't matter 
				victim.hp+=temp;
				System.out.println("Attack missed");
			}
			else {
			System.out.println("Wild Card hit!");
			}
		}
		if(this.attacks[atkNum].getSpec().equals("recharge")){
			this.mp+=20;//recharge mp
			System.out.println(name +" recharged");
		}
	}
	public class Attack{//object attack in Pokemon
		private String name,spec;
		private int cost,dmg;
		public Attack(String name, int cost, int dmg, String spec){//constructer
			this.name=name;
			this.cost=cost;
			this.dmg=dmg;
			this.spec=spec;
		}
		public String stat(){
			String stat=("Name:"+name+" Cost:"+ cost+" Dmg:"+ dmg);
			return stat+=(spec.equals(" "))?"":" Spec:" +spec;
		}
		public String getName(){
			return name;
		}
		public int getCost(){
			return cost;
		}
		public int getDmg(){
			return dmg;
		}
		public String getSpec(){
			return spec;
		}	
	}
}