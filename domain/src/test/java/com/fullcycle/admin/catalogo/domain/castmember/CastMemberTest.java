package com.fullcycle.admin.catalogo.domain.castmember;

import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static java.lang.Thread.sleep;

public class CastMemberTest {


    @Test
    public void givenAValidParams_whenCallsNewMember_thenInstantiateACastMember(){

        final var expectedName =  "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var actualMember = CastMember.newMember(expectedName, expectedType);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertNotNull(actualMember.getCreatedAt());
        Assertions.assertNotNull(actualMember.getUpdatedAt());
        Assertions.assertEquals(actualMember.getCreatedAt(), actualMember.getUpdatedAt());

    }
    @Test
    public void givenAInvalidNullName_whenCallsNewMomber_shouldReceiveANotificationException(){
        final String expectedName =  null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }
    @Test
    public void givenAInvalidEmptyName_whenCallsNewMomber_shouldReceiveANotificationException(){
        final String expectedName =   " ";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    public void givenAnInvalidLengthLessThan3_whenCallNewCastMemberAndValidate_thenShouldReceiveError() {

        final var expectedName = "Vi ";
         final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedType = CastMemberType.ACTOR;

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    public void givenAnInvalidLengthMoreThan255_whenCallNewCastMemberAndValidate_thenShouldReceiveError() {
        final var expectedType = CastMemberType.ACTOR;
        final var expectedName = "Do mesmo modo, a execução dos pontos do programa assume importantes posições no estabelecimento de todos os recursos funcionais envolvidos. Não obstante, a expansão dos mercados mundiais apresenta tendências no sentido de aprovar a manutenção do sistema de formação de quadros que corresponde às necessidades.  ";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }


    @Test
    public void givenAnInvalidNullType_whenCallNewCastMemberAndValidate_thenShouldReceiveError() {
        final CastMemberType expectedType = null;
        final var expectedName = "Vin Diesel";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    public void givenAValidCastMember_whenCallUpdate_shouldReceiveUpdated() throws InterruptedException {


        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;
        final var actualMember = CastMember.newMember("vin ", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());

        final var actualCreatedAt = actualMember.getCreatedAt();
        final var actualupdatedAt = actualMember.getUpdatedAt();

        sleep(1000);
        actualMember.update(expectedName, expectedType);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertNotNull(actualMember.getCreatedAt());
        Assertions.assertNotNull(actualMember.getUpdatedAt());
        Assertions.assertEquals(actualMember.getCreatedAt(), actualCreatedAt);
        Assertions.assertTrue(actualMember.getUpdatedAt().isAfter(actualupdatedAt));
    }

    @Test
    public void givenAValidCastMember_whenCallUpdateWithInvalidNullName_shouldReceiveNotification(){


        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var actualMember = CastMember.newMember("vin ", CastMemberType.DIRECTOR);
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> actualMember.update(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidCastMember_whenCallUpdateWithInvalidEmptyName_shouldReceiveNotification(){


        final var expectedName = " ";
        final var expectedType = CastMemberType.ACTOR;
        final var actualMember = CastMember.newMember("vin ", CastMemberType.DIRECTOR);
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> actualMember.update(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidCastMember_whenCallUpdateWithInvalidNullType_shouldReceiveNotification(){


        final var expectedName = "Vin Disel";
        final CastMemberType expectedType = null;
        final var actualMember = CastMember.newMember("vin ", CastMemberType.DIRECTOR);
        final var expectedErrorMessage = "'type' should not be null";
        final var expectedErrorCount = 1;

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> actualMember.update(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }


    @Test
    public void givenAValidCastMember_whenCallUpdateWithInvalidLengthName_shouldReceiveNotification(){


        final var expectedName = "Do mesmo modo, a execução dos pontos do programa assume importantes posições no estabelecimento de todos os recursos funcionais envolvidos. Não obstante, a expansão dos mercados mundiais apresenta tendências no sentido de aprovar a manutenção do sistema de formação de quadros que corresponde às necessidades.";
        final var expectedType = CastMemberType.ACTOR;
        final var actualMember = CastMember.newMember("vin ", CastMemberType.DIRECTOR);
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.getId());

        final var actualException = Assertions.assertThrows(NotificationException.class, () -> actualMember.update(expectedName, expectedType));

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

}
