package SingletonBillPugh;

public class TenantRegistry {

    // 1. Private constructor prevents anyone else from calling 'new'
    private TenantRegistry() {
        System.out.println("--> [JVM] TenantRegistry constructor executed! (Object created NOW)");
    }

    // 2. The static inner class (The safe)
    private static class SingletonHolder {
        private static final TenantRegistry INSTANCE = new TenantRegistry();
    }

    // 3. Global access point
    public static TenantRegistry getInstance() {
        return SingletonHolder.INSTANCE;                      // if we use this bill pugh in our project we have to call getInstance() method in another class,also no need to create new again as vilotes the singleton rule anyhow private constructor will take care of not creating the object,
    }
}                                                           