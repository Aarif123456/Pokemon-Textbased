import java.io.*;
import java.util.*;
//Abdullah Arif
public class PokemonArena{
	private static ArrayList<Pokemon> pokeList = new ArrayList<Pokemon>(),userList = new ArrayList<Pokemon>();//userList is list of user Pokemon 
	//the rest become enemies
	private static Scanner kb=new Scanner(System.in);
	private static Pokemon enemy;//the current enemy
	private static int cur,num;//cur=current option for battle ,num is place holder variable to hold number when needed
	private static boolean turn,round;
	private static ArrayList<Integer> ranAtk= new ArrayList<Integer>();
	private static Random coin = new Random();
	
	public static void main(String args[])throws IOException{
		//setup
		loadPokemon();
		userPick();//user picks Pokemon rest are bad guy
		System.out.println("You must defeat all enemy Pokemon and become Trainer Supreme\nDon't worry. You have to only fight one Pokemon at a time\n"+
			"Also, your Pokemon(s) will recover after every battle\nGood luck on your quest\nPrepare for your first battle");
		int count=1;//counter for battle
		while(pokeList.size()>0 && userList.size()>0){//game loop
			count+=1;
			System.out.println("enemy:"+pokeList.get(0).basicStat()+"\n"+"Available Pokemon");
			for (int i=0;i<userList.size();i++){//create Pokemon function 
				userList.get(i).fixUp();//recover after battle
				System.out.println(i+1+": "+userList.get(i).basicStat());
			}
			enemy=pokeList.get(0);//enemy is static variable that holds current enemy
			battle();//takes care of battle
			if(userList.size()>0){
				if(pokeList.size()>1){
					System.out.println(pokeList.get(0).getName()+" has fainted!\nCongratulations! Proceed to next round\n\nPrepare for battle NO."+count);
					pokeList.remove(0);
				}
				else{
					System.out.println("Congratulation, You have defeated all enemies, You are now Trainer Supreme");//winner statement
				}
			}
		}
	}

	public static void loadPokemon()throws IOException {//create Pokemon from text file
	Scanner inFile =new Scanner(new BufferedReader(new FileReader("pokemon.txt")));
	num=inFile.nextInt();
	inFile.nextLine();//dummy call
	for (int i=0;i<num;i++){
		pokeList.add(new Pokemon(inFile.nextLine()));//initializing object Pokemon		
		}
	}
	public static void userPick(){
		for(int j=0;j<pokeList.size();j++){				
			System.out.println(j+1+ ":"+ pokeList.get(j).stat());//displaying Pokemon
		}
		ArrayList<Integer> choices = new ArrayList<Integer>();//list of user choices
		System.out.print("Pick 4 different Pokemon\n");
		while(choices.size()<4){
			System.out.print("Enter number:");
			num=kb.nextInt()-1;
			if(num<pokeList.size()&&choices.contains(num)==false){
				choices.add(num);
				userList.add(pokeList.get(num));
			}
		}
		choices.sort(Collections.reverseOrder());//delete from end 
		for(int i:choices){
			pokeList.remove(i);
		}
		Collections.shuffle(pokeList);//shuffle enemy
	}

	public static void battle(){
		turn =coin.nextBoolean();//Randomly determine whether the computer or the user goes first. 
		System.out.println("Choose Pokemon");//option display in Arena 
		cur=kb.nextInt()-1;
		System.out.println(userList.get(cur).getName()+" I choose you");
		ranAtk= new ArrayList<Integer>();
		for (int i=0; i < enemy.getNumAtk(); i++){
			ranAtk.add(i);
		}
		round=turn;	
		while(enemy.getHp() > 0 && userList.size() > 0){
			if (userList.get(cur).getHp() <= 0){//if pokemon faints two option
				faint();
			}
			if (turn&&!userList.get(cur).checkStun()){
				userTurn();	
			}	
			else if(!enemy.checkStun()) {
				enemyTurn();			
			}
			if(round==turn){
				roundRecover();
			}
		}
	}
	
	public static void userTurn(){
		System.out.println("Choose option\n1.Fight\n2.Retreat\n3.Pass\n");
		int option=kb.nextInt();
		if(option==1){
			fight();
		}
		else if(option==2) {
			retreat();//switch pokemon
		}
		else{
			pass();
		} 	 
	}
	public static void fight(){
		System.out.println("\nChoose attack\n\n0.to go back");
		userList.get(cur).dispAtk();
		int option=kb.nextInt()-1;
		if (option==-1){
			userTurn();
		}
		else if (userList.get(cur).validAtk(option)){
			System.out.println(userList.get(cur).getName()+" uses " +userList.get(cur).dispAtk(option));
			userList.get(cur).attackCalc(option,enemy);
			pass();
		}
		else{
			System.out.println("Invalid choice");
			fight();
		}
	}
	public static void retreat(){
		System.out.println("Choose Pokemon(0 to go back)");
		for (int i=0;i<userList.size();i++){
			System.out.println(i+1+": "+userList.get(i).getName());
		}
		int option=kb.nextInt()-1;
		if (option==-1){
			cur=Math.min(cur,userList.size()-1);//safe guard if option 0 is picked when Pokemon faint
			userTurn();
		}
		else {
			cur=Math.min(cur,userList.size()-1);
			System.out.println("Get em'" +userList.get(cur).getName());
			pass();
		}
	}
	public static void pass(){
		if (turn){
			turn=false;
			System.out.println(enemy.getName()+" has "+enemy.getHp()+" hp remaining and "+enemy.getMp()+" energy remaining");
			}
		else{
			turn=true;
			System.out.println(userList.get(cur).getName()+" has "+userList.get(cur).getHp()+" hp remaining and "+userList.get(cur).getMp()+" energy remaining");
		}	
					
	}
	public static void faint(){//two option
		if(userList.size() == 1){//lose game
			lose();
		}
		else{//or replace Pokemon that fainted
			replace();
		}
	}
	public static void lose(){//losing statement
		userList.remove(cur);
		System.out.println("Game over, You have been defeated");
	}
	public static void replace(){//replacing Pokemon
		System.out.println(userList.get(cur).getName()+" has fainted.\nPlease choose another Pokemon");
		userList.remove(cur);
		retreat();
	}
	public static void enemyTurn(){
		Collections.shuffle(ranAtk);
		for(int i:ranAtk){
		if (enemy.validAtk(i)){//goes through all attack in random order 
			System.out.println(enemy.getName()+" uses " +enemy.dispAtk(i));
			enemy.attackCalc(i,userList.get(cur));
			break;
			}
		}
		pass();
	}
	public static void roundRecover(){
		for(int i=0; i<userList.size(); i++){
			userList.get(i).round();
		}
		enemy.round();
	}
}
	
