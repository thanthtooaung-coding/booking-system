package com.vinn.bookingSystem.config.initializer;

import com.vinn.bookingSystem.features.classcategory.entity.ClassCategory;
import com.vinn.bookingSystem.features.classcategory.repository.ClassCategoryRepository;
import com.vinn.bookingSystem.features.classes.entity.ClassEntity;
import com.vinn.bookingSystem.features.classes.repository.ClassRepository;
import com.vinn.bookingSystem.features.classschedule.entity.ClassSchedule;
import com.vinn.bookingSystem.features.classschedule.repository.ClassScheduleRepository;
import com.vinn.bookingSystem.features.country.entity.Country;
import com.vinn.bookingSystem.features.country.repository.CountryRepository;
import com.vinn.bookingSystem.features.instructor.entity.Instructor;
import com.vinn.bookingSystem.features.instructor.repository.InstructorRepository;
import com.vinn.bookingSystem.features.packages.dto.PackageRequest;
import com.vinn.bookingSystem.features.packages.entity.PackageEntity;
import com.vinn.bookingSystem.features.packages.repository.PackageRepository;
import com.vinn.bookingSystem.features.packages.service.PackageService;
import com.vinn.bookingSystem.features.paymentcard.entity.PaymentCard;
import com.vinn.bookingSystem.features.paymentcard.repository.PaymentCardRepository;
import com.vinn.bookingSystem.features.paymenttransactions.entity.PaymentTransaction;
import com.vinn.bookingSystem.features.paymenttransactions.repository.PaymentTransactionRepository;
import com.vinn.bookingSystem.features.user.entity.User;
import com.vinn.bookingSystem.features.user.repository.UserRepository;
import com.vinn.bookingSystem.features.userpackages.entity.UserPackage;
import com.vinn.bookingSystem.features.userpackages.repository.UserPackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CountryRepository countryRepository;
    private final PackageService packageService;

    private final UserRepository userRepository;
    private final UserPackageRepository userPackageRepository;
    private final PaymentCardRepository paymentCardRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final PackageRepository packageRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClassCategoryRepository classCategoryRepository;
    private final ClassRepository classRepository;
    private final ClassScheduleRepository classScheduleRepository;
    private final InstructorRepository instructorRepository;

    @Override
    public void run(String... args) throws Exception {

        final ClassCategory danceCategory = this.classCategoryRepository.findByName("Dance").orElseGet(() ->
                classCategoryRepository.save(ClassCategory.builder()
                        .name("Dance")
                        .description("Dance and rhythm training")
                        .build()));

        final ClassCategory codingCategory = classCategoryRepository.findByName("Coding").orElseGet(() ->
                classCategoryRepository.save(ClassCategory.builder()
                        .name("Coding")
                        .description("Programming and software development")
                        .build()));

        if (this.packageService.getAllActivePackagesByCountry(true).isEmpty()) {
            final Country sg = this.countryRepository.findByCode("SG").orElseGet(() ->
                    countryRepository.save(Country.builder().name("Singapore").code("SG").build()));

            final Country mm = this.countryRepository.findByCode("MM").orElseGet(() ->
                    countryRepository.save(Country.builder().name("Myanmar").code("MM").build()));

            final Country jp = this.countryRepository.findByCode("JP").orElseGet(() ->
                    countryRepository.save(Country.builder().name("Japan").code("JP").build()));

            final Instructor instructorOne = this.instructorRepository.findByEmail("johndoe@vinn.com").orElseGet(() ->
                    instructorRepository.save(Instructor.builder()
                            .name("John Doe")
                            .email("johndoe@vinn.com")
                            .bio("Certified trainer")
                            .country(jp)
                            .build()));

            final ClassEntity javaBootcampJP = classRepository.save(ClassEntity.builder()
                    .classCategory(codingCategory)
                    .country(jp)
                    .name("Java Bootcamp")
                    .description("Java programming fundamentals")
                    .requiredCredits(4)
                    .active(true)
                    .build());

            final Country th = this.countryRepository.findByCode("TH").orElseGet(() ->
                    countryRepository.save(Country.builder().name("Thailand").code("TH").build()));

            final Instructor instructorTwo = instructorRepository.findByEmail("jane@vinn.com").orElseGet(() ->
                    instructorRepository.save(Instructor.builder()
                            .name("Jane Smith")
                            .email("jane@vinn.com")
                            .bio("Dance and fitness expert")
                            .country(th)
                            .build()));

            final ClassEntity salsaClassTH = this.classRepository.save(ClassEntity.builder()
                    .classCategory(danceCategory)
                    .country(th)
                    .name("Salsa Dance")
                    .description("Beginner salsa dancing class")
                    .requiredCredits(2)
                    .active(true)
                    .build());

            this.classScheduleRepository.save(ClassSchedule.builder()
                    .classEntity(salsaClassTH)
                    .instructor(instructorOne)
                    .startTime(LocalDateTime.now().plusDays(4).withHour(17).withMinute(0))
                    .endTime(LocalDateTime.now().plusDays(4).withHour(18).withMinute(30))
                    .totalSlots(25)
                    .availableSlots(25)
                    .location("TH Dance Hall")
                    .status(1)
                    .build());

            this.classScheduleRepository.save(ClassSchedule.builder()
                    .classEntity(javaBootcampJP)
                    .instructor(instructorTwo)
                    .startTime(LocalDateTime.now().plusDays(5).withHour(10).withMinute(0))
                    .endTime(LocalDateTime.now().plusDays(5).withHour(12).withMinute(0))
                    .totalSlots(30)
                    .availableSlots(30)
                    .location("JP Tech Room")
                    .status(1)
                    .build());

            this.packageService.createPackage(new PackageRequest("Basic Package SG", "Basic package for SG", new BigDecimal("9.99"), 5, 30, sg.getId()));
            this.packageService.createPackage(new PackageRequest("Premium Package SG", "Premium package for SG", new BigDecimal("19.99"), 10, 60, sg.getId()));

            this.packageService.createPackage(new PackageRequest("Basic Package MM", "Basic package for MM", new BigDecimal("6.99"), 4, 30, mm.getId()));
            this.packageService.createPackage(new PackageRequest("Premium Package MM", "Premium package for MM", new BigDecimal("16.99"), 8, 60, mm.getId()));

            this.packageService.createPackage(new PackageRequest("Basic Package JP", "Basic package for JP", new BigDecimal("12.99"), 5, 30, jp.getId()));
            this.packageService.createPackage(new PackageRequest("Basic Package TH", "Basic package for TH", new BigDecimal("7.99"), 5, 30, th.getId()));

            final User[] users = {
                    this.createOrGetUser("alice@vinn.com", "Alice", 0),
                    this.createOrGetUser("bob@vinn.com", "Bob", 1),
                    this.createOrGetUser("charlie@vinn.com", "Charlie", 1)
            };

            // Cards, Packages, UserPackages and Transactions
            for (User user : users) {
                this.createCardsForUser(user.getName());

                List<PackageEntity> packages = this.packageRepository.findAll();
                for (int i = 0; i < 2 && i < packages.size(); i++) {
                    PackageEntity pkg = packages.get(i);

                    final UserPackage userPackage = userPackageRepository.save(UserPackage.builder()
                            .user(user)
                            .packageEntity(pkg)
                            .remainingCredits(pkg.getCredits())
                            .purchaseDate(LocalDateTime.now().minusDays(5 + i))
                            .status(1)
                            .build());

                    this.paymentTransactionRepository.save(PaymentTransaction.builder()
                            .amount(pkg.getPrice())
                            .packageEntity(pkg)
                            .status(1)
                            .transactionReference("TXN-" + UUID.randomUUID())
                            .transactionDate(LocalDateTime.now().minusDays(5 + i))
                            .paymentMethod("CARD")
                            .build());
                }
            }
        }
    }

    private User createOrGetUser(String email, String name, int gender) {
        return this.userRepository.findByEmail(email).orElseGet(() ->
                this.userRepository.save(User.builder()
                        .name(name)
                        .email(email)
                        .password(passwordEncoder.encode("Password123!"))
                        .emailVerified(true)
                        .gender(gender)
                        .build()));
    }

    private void createCardsForUser(String cardHolder) {
        for (int i = 1; i <= 2; i++) {
            this.paymentCardRepository.save(PaymentCard.builder()
                    .cardNumber("41111111111111" + String.format("%02d", i))
                    .cardHolder(cardHolder)
                    .expiryDate("12/2" + (i + 3))
                    .cvv("1" + i + i)
                    .defaultFlag(i == 1)
                    .build());
        }
    }
}
