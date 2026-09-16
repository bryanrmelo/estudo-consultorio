package com.example.consultorio.common;

import com.example.consultorio.common.exception.NegocioException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    private ProblemDetail problem(HttpStatus status, String title, String detail) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        return problem;
    }

    private ProblemDetail problem(HttpStatus status, String title, String detail,
                                  Map<String, String> campos) {
        ProblemDetail problem = problem(status, title, detail);
        problem.setProperty("campos", campos);
        return problem;
    }

    @ExceptionHandler(NegocioException.class)
    public ProblemDetail handleNegocio(NegocioException e) {
        return problem(e.getStatus(), e.getTitle(), e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleNotFound(EntityNotFoundException e) {
        log.debug("Entidade não encontrada", e);
        return problem(HttpStatus.NOT_FOUND, "Recurso não encontrado",
                "O recurso solicitado não foi encontrado");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleConflict(DataIntegrityViolationException e) {
        log.warn("Violação de integridade", e);
        return problem(HttpStatus.CONFLICT, "Conflito de dados",
                "Registro conflita com dados já existentes");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> erros = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(erro ->
                erros.merge(erro.getField(), erro.getDefaultMessage(),
                        (a, b) -> a + "; " + b));
        return problem(HttpStatus.BAD_REQUEST, "Erro de validação",
                "Um ou mais campos são inválidos", erros);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ProblemDetail handleParamValidation(HandlerMethodValidationException e) {
        Map<String, String> erros = new HashMap<>();
        e.getParameterValidationResults().forEach(resultado -> {
            String nome = resultado.getMethodParameter().getParameterName();
            resultado.getResolvableErrors().forEach(erro ->
                    erros.merge(nome, erro.getDefaultMessage(), (a, b) -> a + "; " + b));
        });
        return problem(HttpStatus.BAD_REQUEST, "Parâmetro inválido",
                "Um ou mais parâmetros são inválidos", erros);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        Class<?> tipo = e.getRequiredType();

        String esperado = (tipo != null && tipo.isEnum())
                ? Arrays.stream(tipo.getEnumConstants())
                .map(Object::toString)
                .collect(Collectors.joining(", "))
                : (tipo != null ? tipo.getSimpleName() : "desconhecido");

        String mensagem = "valor '%s' é inválido; esperado: %s"
                .formatted(String.valueOf(e.getValue()), esperado);

        return problem(HttpStatus.BAD_REQUEST, "Parâmetro inválido",
                "O parâmetro '%s' recebeu um valor inválido".formatted(e.getName()),
                Map.of(e.getName(), mensagem));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ProblemDetail handleMissingParam(MissingServletRequestParameterException e) {
        return problem(HttpStatus.BAD_REQUEST, "Parâmetro obrigatório ausente",
                "O parâmetro '%s' é obrigatório".formatted(e.getParameterName()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleUnreadable(HttpMessageNotReadableException e) {
        log.debug("Corpo da requisição ilegível", e);
        return problem(HttpStatus.BAD_REQUEST, "Requisição malformada",
                "O corpo da requisição está ausente ou em formato inválido");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        log.debug("Método indisponível", e);
        return problem(HttpStatus.METHOD_NOT_ALLOWED, "Método indisponível",
                "Esse método não está disponível para esse recurso");
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception e) {
        log.error("Erro não tratado", e);
        return problem(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno",
                "Ocorreu um erro inesperado ao processar a requisição");
    }
}