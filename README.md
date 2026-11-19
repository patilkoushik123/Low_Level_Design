Real World Use Cases of Singleton Pattern:
1) Logging System: A single logger instance writes logs throughout the application.
2) Database Connection Manager: Only one object manages DB connections across the application.
3) Configuration/Properties Manager: Reads application configuration once and shares it everywhere.

Enum Singleton:

Disadvantages with enum:
1. Cant be final as enum runs first with non argumented construtor, to solve issue of final assigiing ,we should mention direct value of final or we have to mention new in the constructor for refering class.

case A:   public enum AppConfig {
    INSTANCE;

    private final String currency;

    AppConfig() {
        this.currency = "INR";   // works fine
    }
}

case B: class SimpleTenantStore {              // a normal class
    public String find(String id) {
        return "data for " + id;
    }
}

public enum TenantRegistryEnum {
    INSTANCE;

    private final SimpleTenantStore store;

    TenantRegistryEnum() {
        this.store = new SimpleTenantStore();   // creates a real object, final is assigned
    }
}

case C: public enum TenantRegistryEnum {
    INSTANCE;

    private final TenantRepository tenantRepository;

    TenantRegistryEnum() {
        this.tenantRepository = ???;
    }
}

case D; public enum TenantRegistryEnum {
    INSTANCE;

    private TenantRepository tenantRepository;   // not final

    public void setTenantRepository(TenantRepository repo) {
        this.tenantRepository = repo;             // assigned later, by hand
    }
}   // here no constructor involed for field assigning and no final also,and in hand we kept value for field,which works everything fine.

case E: @Component
public class TenantRegistry {

    private final TenantRepository tenantRepository;   // final

    public TenantRegistry(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;       // assigned once, in the constructor
    }
}  // spring handles this cases, so while spring creating instance at that time constructor gets called, here constructor has arguments which helps to pass into contsructor and helps in assigning.// basically it is constructor injection.
// when it comes to enum part,jvm runs the instance and no argument constructor so at this   this.tenantRepository = ??? for no argument constructor dont know where the value comes from and it will be error..and no final assigning happens.












