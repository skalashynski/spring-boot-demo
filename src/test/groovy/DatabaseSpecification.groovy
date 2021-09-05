
import groovy.sql.Sql
import org.testcontainers.containers.PostgreSQLContainer
import spock.lang.Shared
import spock.lang.Specification


class DatabaseSpecification extends Specification{
    @Shared
    static def container = new PostgreSQLContainer("postgres:13.3")

    @Shared
    Sql sql

    void setupSpec() {
        sql = Sql.newInstance(
                "jdbc:postgresql://localhost:${container.getMappedPort(5432)}/test",
                "test",
                "test"
        )
    }

    void cleanupSpec(){
        sql.close()
    }
}
