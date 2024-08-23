package com.mychallenge.wishlist;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.Architectures;

@AnalyzeClasses(
    packagesOf = { WishlistArchitectureTest.class },
    importOptions = { ImportOption.DoNotIncludeJars.class, ImportOption.DoNotIncludeTests.class }
)
public class WishlistArchitectureTest {

    @ArchTest
    public void layersShouldBeIntegrity(JavaClasses classes) {
        Architectures.layeredArchitecture()
            .consideringAllDependencies()
            .withOptionalLayers(true)
            .layer("application")
            .definedBy("com.mychallenge.wishlist.application..")
            .layer("domain")
            .definedBy("com.mychallenge.wishlist.domain..")
            .layer("infrastructure")
            .definedBy("com.mychallenge.wishlist.infrastructure..")
            .whereLayer("application")
            .mayNotBeAccessedByAnyLayer()
            .whereLayer("domain")
            .mayOnlyBeAccessedByLayers("application", "infrastructure")
            .whereLayer("infrastructure")
            .mayOnlyBeAccessedByLayers("application")
            .allowEmptyShould(true)
            .check(classes);
    }
}
