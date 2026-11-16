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
        return SingletonHolder.INSTANCE;
    }
}