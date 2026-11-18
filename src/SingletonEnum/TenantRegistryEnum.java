package SingletonEnum;



import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
  // Note : Important: Basically In General spring projects we use enum for default values ,by creating INSTANCE and managaing enum is not needed in spring as @service ,@Conroller or @Component will handle of creating single instance,thats why most of projects we wont include business methods of mutable fields in enum,just we use for default values .
// Here we use general java,no use spring ,so we mentioned Instance and we included mutable and non mutable fields.

/* In Spring Boot: Enums are your type-safe constants and statuses (like OrderStatus.PENDING), while Spring’s container manages your mutable business logic and data layers via @Service and @Component singletons.

In Pure Java / Non-Spring: The Enum Singleton pattern (INSTANCE) is a powerful, zero-boilerplate way to guarantee a single, thread-safe instance with mutable states (like our TenantRegistryEnum) without needing a framework.

The Dependency Injection Rule: Because enums cannot be wired with @Autowired (they can't inject databases or repositories), Spring-managed beans are the natural choice for real-world enterprise applications.*/

/* In pure Java: The JVM guarantees only one instance exists when you use an enum with INSTANCE.

In Spring Boot: The Spring IoC Container guarantees only one instance exists when you annotate a class with @Service, @Component, or @RestController.*/

// thats why as @Service, @Component, or @RestController will provide single instance hence we gonna include mutable fileds and methods here only and default fields in e num,,in core java there are in spring container and no @Service, @Component, or @RestController..thats why we use INSTANCE also can perform mutable and non mutable fields and methods in enum only also concurrent hashmap(for thread safety) and this (instance.) helps to call method and fields which are in enum with single created instance .and spring projects mostly wont use to keep mutable and immuatble f8ields and methods as no mention of INSTANCE in enum as spring handles the singleton instance by @Service, @Component, or @RestController.   
public enum TenantRegistryEnum {
    
    // The single JVM-managed instance
    INSTANCE;

    // 1. Immutable configuration fields (Safe across threads)
    private final String environment = "PRODUCTION";
    private final int maxAllowedTenants = 500;

    // 2. Mutable data structure (Must be thread-safe because multiple threads access it!)
    private final Map<String, String> tenantRoutingCache = new ConcurrentHashMap<>();
    /**
     * Uses a ConcurrentHashMap to ensure thread-safe concurrent access across multiple threads without full-map locking.
     * Leverages lock-free reads and fine-grained bucket-level locking or CAS updates to maximize write performance.
     * Prevents race conditions and data corruption when multiple requests read or register tenants simultaneously.
     */
    
    //For empty buckets or atomic updates, ConcurrentHashMap uses CAS (Compare-And-Swap) CPU-level instructions.
    //It attempts to insert the data directly. If another thread didn't touch that exact spot in the nanosecond it took to write, the operation succeeds instantly with no locks at all.
    
    /*
     * Summary
Because your tenantRoutingCache is a ConcurrentHashMap, your Singleton enum can safely handle hundreds of incoming web requests from different threads simultaneously.

Reads never block anyone.

Writes only lock the specific bucket being modified, preventing race conditions, data corruption, and desynchronization without slowing down your app
     */
    
    /**
     * WARNING: A standard HashMap is not thread-safe and causes race conditions, silent data loss, 
     * and memory invisibility issues when accessed concurrently by multiple threads.
     * Concurrent structural modifications can corrupt internal pointers, triggering infinite loops 
     * that can peg CPU utilization at 100%.
     */
    // Constructor runs exactly once when the enum is loaded
    TenantRegistryEnum() {
        System.out.println("--> [Enum] Initializing global tenant registry...");
        // Pre-load some default data if needed
        tenantRoutingCache.put("tenant_alpha", "db_cluster_1");
        tenantRoutingCache.put("tenant_beta", "db_cluster_2");
    }

    // Methods to interact with your fields
    public void registerTenant(String tenantId, String dbCluster) {
        tenantRoutingCache.put(tenantId, dbCluster); // ConcurrentHashMap handles thread safety
    }

    public String getDbCluster(String tenantId) {
        return tenantRoutingCache.get(tenantId);
    }

    public String getEnvironment() {
        return environment;
    }
}