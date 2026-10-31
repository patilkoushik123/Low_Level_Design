package SingletonPackage;

public class SingletonClass {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
     Calculator c1 = Calculator.getInstance();
     Calculator c2 = Calculator.getInstance();  // we dont need static here while creating obj in main,coz it is local variable and  temporary and lives only when main is running and when we run again it get refesh
     c1.a=10;
     c1.b=5;
     
     c2.a = 7;
     c2.b = 3;             // if we  mention only with c1 object,then last values will override and give sum 10 for sum1 and sum2,so to get different value sum we need different objects,but we should use single objects ,here singleton pattern design comes.
     System.out.print("sum 1: "+ c1.sum());
     System.out.print("sum 2: "+ c2.sum());

	}

}
