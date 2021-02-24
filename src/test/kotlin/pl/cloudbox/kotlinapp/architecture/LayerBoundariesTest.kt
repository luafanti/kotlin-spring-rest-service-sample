package pl.cloudbox.kotlinapp.architecture

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.junit.ArchUnitRunner
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.jupiter.api.TestInstance
import org.junit.runner.RunWith
import pl.cloudbox.kotlinapp.ClientBalanceApp


@RunWith(ArchUnitRunner::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AnalyzeClasses(packagesOf = [ClientBalanceApp::class])
internal class LayerBoundariesTest {

    @ArchTest
    val `Repository layer should has no dependency with higher layers` =
        noClasses()
            .that()
            .resideInAPackage("$REPOSITORY_PACKAGE..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("$SERVICE_PACKAGE..", "$ENDPOINT_PACKAGE..")


    @ArchTest
    val `Endpoint layer should not access repository layer` =
        noClasses()
            .that()
            .resideInAPackage("$ENDPOINT_PACKAGE..")
            .should()
            .accessClassesThat()
            .resideInAnyPackage("$REPOSITORY_PACKAGE..")


    @ArchTest
    val `Any layer should not access endpoint layer` =
        noClasses()
            .that()
            .resideInAnyPackage("$REPOSITORY_PACKAGE..", "$SERVICE_PACKAGE..")
            .should()
            .accessClassesThat()
            .resideInAnyPackage("$ENDPOINT_PACKAGE..")


    companion object {
        private val BASE_PACKAGE = ClientBalanceApp::class.java.`package`.name
        private val SERVICE_PACKAGE = "$BASE_PACKAGE.service"
        private val REPOSITORY_PACKAGE = "$BASE_PACKAGE.repository"
        private val ENDPOINT_PACKAGE = "$BASE_PACKAGE.endpoint"
    }
}