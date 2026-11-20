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

IN DETAIL:

# Enum Singleton vs Spring Bean: Final Field Cases

## Core idea

> Spring injects only into objects **Spring creates**.
> An enum is created by the **JVM**, so Spring can't pass anything into it.

---

## 1. Who creates the object?

| | Spring bean (`@Component`) | Enum singleton |
|---|---|---|
| Created by | Spring container | JVM (at class loading) |
| Constructor called by | Spring | JVM |
| Constructor arguments | Supplied by Spring (injected beans) | None, unless hardcoded in `INSTANCE(...)` |
| Constructor runs? | Yes | Yes (it is never skipped) |

---

## 2. What `final` means

- A `final` field must be assigned **exactly once**, inside the constructor (or at declaration).
- It happens **during** object creation, not after.
- After the constructor ends, the value is locked.
- If a `final` field is never assigned, the code **won't compile**.

---

## 3. All cases

### Case 1: Spring bean with `final` field (works)

    @Component
    public class TenantRegistry {
        private final TenantRepository tenantRepository;

        public TenantRegistry(TenantRepository tenantRepository) {
            this.tenantRepository = tenantRepository;
        }
    }

- Spring calls the constructor and passes the repository.
- `final` is assigned once.

### Case 2: Enum with a hardcoded value (works)

    public enum AppConfig {
        INSTANCE;
        private final String currency;

        AppConfig() {
            this.currency = "INR";
        }
    }

- Value is known at class-loading time.

### Case 3: Enum with a constructor parameter (works)

    public enum AppConfig {
        INSTANCE("INR");
        private final String currency;

        AppConfig(String currency) {
            this.currency = currency;
        }
    }

- Value is supplied in the declaration: `INSTANCE("INR")`.
- Parameter `currency` is copied into field `this.currency`.

### Case 4: Enum creating a plain Java object with `new` (works)

    public enum TenantRegistryEnum {
        INSTANCE;
        private final SimpleTenantStore store;

        TenantRegistryEnum() {
            this.store = new SimpleTenantStore();
        }
    }

- Works because `SimpleTenantStore` is an ordinary class with no Spring involved.

### Case 5: `this.currency = currency;` with no parameter (compile error)

    AppConfig() {
        this.currency = currency;   // field assigned to itself
    }

- Right-hand `currency` is the unassigned field, not a value. Like `x = x;`.
- Error: "variable might not have been initialized".

### Case 6: Enum with Spring `TenantRepository` and `final` (not possible)

    public enum TenantRegistryEnum {
        INSTANCE;
        private final TenantRepository tenantRepository;

        TenantRegistryEnum() {
            this.tenantRepository = ???;   // no valid value
        }
    }

- Constructor has no arguments, since the JVM calls it.
- `TenantRepository` is an **interface**, so `new TenantRepository()` is impossible.
- Only Spring can generate its implementation, and Spring starts **after** the enum is created.
- Only options: `null` (stays null forever, since `final`) or no assignment (compile error).

### Case 7: `INSTANCE(???)` with `TenantRepository` (not possible)

    INSTANCE(???);   // what goes here?

- The argument must be written in the declaration, evaluated at class loading.
- The repository doesn't exist yet, so there is nothing to pass.

### Case 8: `@Autowired` on an enum field (does nothing)

    public enum TenantRegistryEnum {
        INSTANCE;

        @Autowired
        private TenantRepository tenantRepository;   // stays null
    }

- Spring processes `@Autowired` only on objects **it created**.
- The JVM created `INSTANCE`, so Spring never injects. Field stays `null`.
- It also can't be `final`.

### Case 9: Setter workaround (works but not recommended)

    INSTANCE.setTenantRepository(repo);

- Field is mutable and not `final`.
- `NullPointerException` if used before the setter runs.
- Cannot be mocked easily in tests.

---

## 4. Field vs parameter

| Name | What it is | Lifetime |
|---|---|---|
| `this.currency` | Field of the object | As long as the object lives |
| `currency` (alone) | Constructor parameter | Only inside the constructor |

- `this` means "the object being created".
- Names can differ: `this.currency = value;` is the same thing.

---

## 5. Timeline

Enum:

    1. JVM loads enum class
    2. JVM creates INSTANCE and runs the constructor (no arguments)
    3. Spring starts later and creates TenantRepository
    4. Spring has no hook into step 2

Spring bean:

    1. Spring creates TenantRepository
    2. Spring calls new TenantRegistry(tenantRepository)

---

## 6. Common misunderstandings

- "The constructor is skipped." No, it always runs.
- "`final` is skipped." No, the assignment runs, but there may be no valid value.
- "The JVM skips `@Autowired`." No, the JVM doesn't know it exists.
- "`final` can't be used in enums." No, it works fine when the value is available at class loading.

---

## 7. Rules of thumb

- Plain Java: use an **enum** (or Bill Pugh holder) for a singleton.
- Spring app: use **`@Component` / `@Service`**; Spring guarantees one instance per application context.
- Need injected dependencies (repository, other beans): use a **Spring bean**, never an enum.
- `final` works in enums for values known at class-loading time, not for Spring-created objects.

  CORE EXPLANATION OF WHY USE FINAL:

  ## 1. `final` in Constructor Injection (Spring Boot)

**Purpose:** To prevent reassignment of an injected dependency after the object is constructed.

### Example: RealRepository and DummyRepository

Suppose we have two repository implementations:

- RealRepository – Contains actual application data.
- DummyRepository – Contains dummy/test data.

We inject RealRepository into our service through the constructor.

```java
@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}
```

### What happens without `final`?

```java
private UserRepository repository;
```

- Initially, Spring injects RealRepository.
- Later, we can reassign the reference to another compatible repository object.
- The service could then start using a different repository and retrieve different data.

```java
this.repository = anotherRepository; // Allowed
```

### What happens with `final`?

```java
private final UserRepository repository;
```

- Spring injects the repository through the constructor.
- Once initialized, the reference cannot be reassigned.
- It continues pointing to the originally injected repository.
- Any attempt to reassign it causes a compilation error.

```java
this.repository = anotherRepository; // Compilation error
```

### Important Points

1. `final` ensures that the injected dependency reference cannot be changed after constructor initialization.
2. It helps maintain dependency consistency.
3. It makes the reference immutable, not the repository's internal data.
4. `final` does not decide which repository Spring injects; it only prevents reassignment.

**Remember:** We use `final` in constructor injection to ensure that once a dependency is injected, its reference remains unchanged throughout the object's lifetime.

WHEN OBJECT,INSTANCE CREATION HAPPENS IN SPRING:

At the time of @service , @Component ,@Repositor spring creates an object,instance so while at the time injecting into different class should create constructor and inject that object 

example:
@Component
public class TenantRegistry { ... }              // Spring creates this once

@Service
public class BillingService {
    private final TenantRegistry tenantRegistry; // final: assigned once

    public BillingService(TenantRegistry tenantRegistry) {   // Spring injects it here
        this.tenantRegistry = tenantRegistry;
    }
}   


About @primary and @Qualifer:

We use @Qualifier and @Primary in Spring Boot when multiple beans of the same type are available, and Spring needs to know which one to inject.

1. The problem: Multiple implementations
Suppose we have an interface:
public interface UserRepository {
    String getData();
}


Two implementations:
@Repository
public class RealRepository implements UserRepository {
    public String getData() {
        return "Real Repository Data";
    }
}


@Repository
public class DummyRepository implements UserRepository {
    public String getData() {
        return "Dummy Repository Data";
    }
}


Now, inject the interface into a service:
@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}

What is the problem?

Spring finds two beans of type UserRepository:
- RealRepository
- DummyRepository
Which one should Spring inject?

Spring gets confused because there are two candidates. It throws NoUniqueBeanDefinitionException

Solution: 
1.Use @Primary when you want one implementation to be the default choice.
@Repository
@Primary
public class RealRepository implements UserRepository {

    public String getData() {
        return "Real Repository Data";
    }
}

Now Spring understands:
- RealRepository → Default choice
- DummyRepository → Secondary choice

2. qualifier
    @Service
public class UserService {

    private final UserRepository repository;

    public UserService(
        @Qualifier("dummyRepository") UserRepository repository) {

        this.repository = repository;
    }
}

Now Spring injects DummyRepository, even if RealRepository is marked @Primary.















