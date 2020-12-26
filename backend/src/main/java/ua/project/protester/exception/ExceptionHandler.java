package ua.project.protester.exception;

import org.openqa.selenium.WebDriverException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ua.project.protester.exception.executable.action.ActionNotFoundException;
import ua.project.protester.exception.executable.compound.InnerCompoundException;
import ua.project.protester.exception.executable.scenario.UsedTestScenarioDeleteException;
import ua.project.protester.exception.result.RunResultNotFoundException;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @NonNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                 @NonNull HttpHeaders headers,
                                                                 @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        body.put("errors", errors);

        return new ResponseEntity<>(body, headers, status);

    }

    @org.springframework.web.bind.annotation.ExceptionHandler(FaultPermissionException.class)
    public ResponseEntity<Object> faultPermissionException(FaultPermissionException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "You don`t have enough permission! Naughty boy!!!");

        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(EmailDuplicateException.class)
    public ResponseEntity<Object> handleEmailDuplicateException(
            EmailDuplicateException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Email is already exist!");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UsernameDuplicateException.class)
    public ResponseEntity<Object> handleUsernameDuplicateException(
            UsernameDuplicateException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Username is already exist!");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(DisabledUserException.class)
    public ResponseEntity<Object> handleDeactivatedUserException(
            DisabledUserException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Username is deactivated! Please,contact administrator!");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ActionMapperException.class)
    public ResponseEntity<Object> handleActionMapperException(
            ActionMapperException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Can not find implementation in BaseAction class");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ActionNotFoundException.class)
    public ResponseEntity<Object> handleActionNotFoundException(
            ActionNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Can not find action");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<Object> handleUnauthorizedUserException(
            UnauthorizedUserException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "User was`nt authorized!");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserFoundException.class)
    public ResponseEntity<Object> handleUserFoundException(
            UserFoundException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "User was`nt found!");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Object> handleRoleNotFoundException(
            RoleNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Role not found!");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ProjectAlreadyExistsException.class)
    public ResponseEntity<Object> handleProjectAlreadyExistsException(
            ProjectAlreadyExistsException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Project already exists!");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<Object> handleProjectNotFoundException(
            ProjectNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Project not found!");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(LibraryAlreadyExistsException.class)
    public ResponseEntity<Object> handleLibraryAlreadyExistsException(
            LibraryAlreadyExistsException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Library already exists!");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(LibraryNotFoundException.class)
    public ResponseEntity<Object> handleLibraryNotFoundException(
            LibraryNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Library not found!");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(WebDriverException.class)
    public ResponseEntity<Object> handleWebDriverException(
            WebDriverException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getClass().getName());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(TestCaseNotFoundException.class)
    public ResponseEntity<Object> handleTestCaseNotFoundException(
            TestCaseNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Test not found!");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(TestCaseCreateException.class)
    public ResponseEntity<Object> handleTestCaseCreateException(
            TestCaseCreateException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Can not create Test case");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }


    @org.springframework.web.bind.annotation.ExceptionHandler(RunResultNotFoundException.class)
    public ResponseEntity<Object> handleRunResultNotFoundException(
            TestCaseNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", "Run result not found!");
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(InnerCompoundException.class)
    public ResponseEntity<?> handleInnerCompoundDeleteException(InnerCompoundException e) {
        return new ResponseEntity<>(
                Map.of(
                        "timestamp", OffsetDateTime.now(),
                        "message", e.getMessage(),
                        "outerComponents", e.getOuterComponents()),
                HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UsedTestScenarioDeleteException.class)
    public ResponseEntity<?> handleUsedTestScenarioDeleteException(UsedTestScenarioDeleteException e) {
        return new ResponseEntity<>(
                Map.of(
                        "timestamp", OffsetDateTime.now(),
                        "message", e.getMessage(),
                        "testCases", e.getTestCases()),
                HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(EnvironmentNotFoundException.class)
    public ResponseEntity<?> environmentNotFoundException(EnvironmentNotFoundException e) {
        return new ResponseEntity<>(
                Map.of(
                        "timestamp", OffsetDateTime.now(),
                        "message", e.getMessage(),
                        "environmentId", e.getEnvironmentId()),
                HttpStatus.NOT_FOUND);
    }
}
