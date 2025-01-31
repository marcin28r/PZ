Aplikacja Spring Boot dla systemu zarządzania książkami w bibliotece.
Kluczową koncepcja w frameworku Spring, która umożliwia zarządzanie zależnościami
między komponentami aplikacji jest wstrzykiwanie zależności (ang. Dependency Injection,
DI). Dzięki wstrzykiwaniu zależności, Spring automatycznie dostarcza wymagane obiekty
(zależności) do klas, które ich potrzebują, co prowadzi do bardziej modularnego,
testowalnego i łatwiejszego w utrzymaniu kodu. Projekt obejmuje wykorzystanie adnotacji
kluczowych dla tej koncepcji takich jak @Bean, @Autowired, @Configuration czy
@Component.
Pakiety book, category, color, comment, image, price, rent, slider, user reprezentują encje w
systemie. Każdy z pakietów posiada klasę obiektu, kontroller, serwis i repozytorium. Klasy
są oznaczona adnotacją @Entity, co oznacza, że są mapowane na tabele w bazie danych.
Każda encja posiada własny serwis @Service, który zawiera logikę biznesową związaną z
daną encją i repozytorium (konkretny interfejs rozszerza JpaRepository, co zapewnia
podstawowe operacje CRUD ). Ostatecznie wszystkie encje posiadają kontroller
@RestController, który obsługuje żądania HTTP związane z danymi encjami.
Klasy wykorzystują adnotacje Lomboka (@Data, @Builder, @NoArgsConstructor,
@AllArgsConstructor) do automatycznego generowania getterów, setterów, konstruktorów i
wzorca budowniczego.
Klasy aby uzyskać odpowiednio skonfigurowane encje wykorzystują adnotacje JPA (Java
Persistence API) takie jak np: @Id, @GeneratedValue, @ManyToMany, @JoinTable.
Pakiet dto zawierają klasy implementujące wzorzec projektowy DTO(Data Transfer Object) .
Jest on używany w aplikacjach do przenoszenia danych między warstwami aplikacji, np.
między warstwą kontrolera a warstwą serwisu. W kontekście Springa, DTO jest często
używany do izolowania warstwy prezentacji od warstwy domenowej, co pozwala na większą
elastyczność i bezpieczeństwo.
Pakiet przedstawia implementację mapowania między obiektami domenowymi a obiektami
DTO przy użyciu ręcznej implementacji mapperów, co znacznie redukuje ilość boilerplate
kodu związanego z ręcznym mapowaniem.
JsonRequestGenerator to singletonowa klasa odpowiedzialna za automatyczne
generowanie szablonów żądań HTTP w formacie JSON na podstawie kontrolerów Spring
Boot. Wykorzystuje do tego głównie mechanizm refleksji. Klasa analizuje adnotacje metod
(@GetMapping, @PostMapping itp.), parametry żądań (@RequestParam, @RequestBody,
@PathVariable), a następnie zapisuje wygenerowane informacje do pliku tekstowego w
folderze JsonEndpoints.
Implementacja zabezpieczeń w aplikacji Spring Boot, wykorzystującą JWT (JSON Web
Token) do uwierzytelniania użytkowników znajduje się w pakiecie config.
ApplicationConfig.java - klasa ta konfiguruje podstawowe komponenty związane z
uwierzytelnianiem w aplikacji.
1. UserDetailsService jest odpowiedzialny za ładowanie danych użytkownika na
podstawie nazwy użytkownika. Wykorzystuje do tego UserRepository, które jest
interfejsem dostępu do danych użytkowników.
2. AuthenticationProvider jest skonfigurowany do użycia DaoAuthenticationProvider,
który integruje się z UserDetailsService i PasswordEncoder do uwierzytelniania
użytkowników.
3. PasswordEncoder wykorzystuje BCryptPasswordEncoder do hashowania haseł.
4. AuthenticationManager jest dostarczany przez AuthenticationConfiguration i
zarządza procesem uwierzytelniania.
JwtAuthenticationFilter.java - filtr jest wywoływany przy każdym żądaniu HTTP i sprawdza
obecność tokenu JWT w nagłówku Authorization.
1. Jeśli token jest obecny i poprawny, filtr wyodrębnia nazwę użytkownika z tokenu,
ładuje odpowiednie dane użytkownika za pomocą UserDetailsService, a następnie
ustawia kontekst bezpieczeństwa Spring Security.
2. Filtr ten rozszerza OncePerRequestFilter, co gwarantuje, że jest on wykonywany
tylko raz dla każdego żądania.
3. JwtService.java - klasa zawiera logikę związaną z generowaniem, parsowaniem i
walidacją tokenów JWT.
4. extractUsername i extractClaim służą do wyodrębniania informacji z tokenu JWT.
5. generateJwtToken generuje nowy token JWT na podstawie danych użytkownika i
dodatkowych danych.
6. isTokenValid sprawdza, czy token jest ważny i czy odpowiada danemu
użytkownikowi. Klucz do podpisywania tokenów jest zakodowany w formacie Base64
i przechowywany jako stała.
SecurityConfiguration.java - konfiguruje globalne ustawienia bezpieczeństwa dla aplikacji.
1. SecurityFilterChain definiuje, które ścieżki są publiczne, a które wymagają
uwierzytelnienia.
2. Sesje są ustawione jako bezstanowe (STATELESS), co oznacza, że serwer nie
przechowuje stanu sesji między żądaniami, a każdy token JWT musi być
dostarczony z każdym żądaniem.
3. Filtr JwtAuthenticationFilter jest dodawany przed standardowym filtrem
UsernamePasswordAuthenticationFilter, aby zapewnić przetwarzanie tokenów JWT
przed tradycyjnym uwierzytelnianiem.
