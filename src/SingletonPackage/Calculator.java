package SingletonPackage;

public class Calculator {
int a;
int b;

// private static Calculator obj = new Calculator();  if we use this it will store the object in memory even without calling the object for use,,thats why we used this private static Calculator obj ;..and when there is a need only object will create,only after getInstance is called,This process is called lazy initialization,object creates only when it gets called( getInstance) rather than simply creating and waste of memory.
private static Calculator obj ; //here we used static coz,if there is no static to create object it requires pre-existing obj and for that object needs pre -existing object so it get error,thats why we need static.

// here we used private constructor because ,it helps to prevent creating object in main as new object which violets the singleton rule.
//A private constructor ensures that nobody outside the class can create a new object using new
// if we not use constructor ,default public constructor will use,and using public makes to create new object which violites the singleton pattern,
private Calculator() {                    // why static not needs pre existing objects because ,for static the object is referenced to class level and for non static that object is referenced to that particular object level
	  System.out.println("Instance created");                                    // why class level no need pre- existing object,coz class is already in memory so no need create any obj.
	                                       // non static is object level,needs object to creating another object,so we use static .
}
public int sum() {
	return a+b;
}


 /*public static Calculator getInstance() {   
	                                           //here used static ,coz if it non static then this method can run only after creating object,so even before calling object to run the method we need static
	  if(obj==null) {
		  obj = new Calculator();
	  }
	 
	 return  obj;*/
	 
	 
	/* public synchronized static Calculator getInstance() {      // actually when we use multi threading ,due to parellelism ,two objects will create,as two threads will perform task,rather than waiting,to avoid this we use synchonized ,it helps in waiting other threads while one thread is busy.
if(obj==null) {
obj = new Calculator();
}

return  obj;
}*/
	 
	 public static Calculator getInstance() {      // so here we removed synchronized in method level because when there are mutiple threads and after creating in obj after first thread still remaining threads will be in synchroination ,coming one after other ,if there are multiple threads it leads to effect on the performance.
       if(obj==null) {                            // Thats why we used synchonization in block level where multipe threads come at condtion level and checks the conidtion if the obj is created already then bypass the creating objecr and return same object,also we used condition check if object is null or not because if object is already created no need to even keep the synchronized.
        synchronized(Calculator.class) {
        	if(obj==null) {                      // This concept is called Double check locking.
        		obj = new Calculator();
        	}
        }
       }


return  obj;
}
}


//static variables and methods don't wait for an object to be created—they actually exist in memory before any object is ever made.


//A constructor is a special block of code in Java that runs automatically the exact moment a new object is born.

/*Main Uses of a Constructor
 * To initialize variables (give objects a starting state):
 * public Calculator() {
    a = 5;  // Default starting value
    b = 10; // Default starting value
}

To control object creation (like in your Singleton):
 * 
 * 
 * 
 */
