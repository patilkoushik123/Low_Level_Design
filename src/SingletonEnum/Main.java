package SingletonEnum;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Starting Enum Singleton Test ===\n");

        // 1. Accessing immutable configuration fields
        System.out.println("Environment: " + TenantRegistryEnum.INSTANCE.getEnvironment());

        // 2. Reading pre-loaded data from the ConcurrentHashMap
        System.out.println("Initial Lookup for 'tenant_alpha': " + TenantRegistryEnum.INSTANCE.getDbCluster("tenant_alpha"));
        System.out.println("Initial Lookup for 'tenant_beta': " + TenantRegistryEnum.INSTANCE.getDbCluster("tenant_beta"));

        // 3. Registering a new tenant dynamically
        System.out.println("\n--> Registering 'tenant_gamma'...");
        TenantRegistryEnum.INSTANCE.registerTenant("tenant_gamma", "db_cluster_3");

        // 4. Verifying the state change works globally
        System.out.println("Lookup for newly added 'tenant_gamma': " + TenantRegistryEnum.INSTANCE.getDbCluster("tenant_gamma"));

        // 5. Proving Singleton behavior (Memory address check)
        TenantRegistryEnum instanceOne = TenantRegistryEnum.INSTANCE;
        TenantRegistryEnum instanceTwo = TenantRegistryEnum.INSTANCE;

        System.out.println("\n=== Singleton Proof ===");
        System.out.println("Are both references pointing to the exact same memory object? " + (instanceOne == instanceTwo));
        System.out.println("Hash code of instanceOne: " + instanceOne.hashCode());
        System.out.println("Hash code of instanceTwo: " + instanceTwo.hashCode());
    }
}